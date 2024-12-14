import pandas as pd
import torch
import numpy as np
from torch.utils.data.dataset import Dataset
from torch.utils.data import DataLoader
import pymysql
from flask import Flask, request, jsonify
from flask_cors import CORS
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import random

# Khởi tạo Flask app
app = Flask(__name__)
CORS(app)  # Cho phép CORS cho tất cả các endpoint

# Kết nối tới cơ sở dữ liệu MySQL
def get_data_from_mysql():
    connection = pymysql.connect(
    host='localhost',
    user='root',
    password='hoang198767',
    database='leherryacademy',
    port=3306, 
    )
    # Đọc bảng courses
    courses_df = pd.read_sql('SELECT * FROM courses', connection)
    
    # Đọc bảng ratings và đổi user_id thành person_id
    ratings_df = pd.read_sql('SELECT person_id, course_id, rating, timestamp FROM ratings', connection)

    connection.close()
    return courses_df, ratings_df

# Mô hình Matrix Factorization
class MatrixFactorization(torch.nn.Module):
    def __init__(self, n_users, n_items, n_factors=20):
        super().__init__()
        self.user_factors = torch.nn.Embedding(n_users, n_factors)
        self.item_factors = torch.nn.Embedding(n_items, n_factors)
        self.user_factors.weight.data.uniform_(0, 0.05)
        self.item_factors.weight.data.uniform_(0, 0.05)

    def forward(self, data):
        users, items = data[:, 0], data[:, 1]
        return (self.user_factors(users) * self.item_factors(items)).sum(1)


class Loader(Dataset):
    def __init__(self, ratings_df):
        self.ratings = ratings_df.copy()
        users = ratings_df.person_id.unique()
        courses = ratings_df.course_id.unique()

        self.userid2idx = {o: i for i, o in enumerate(users)}
        self.courseid2idx = {o: i for i, o in enumerate(courses)}
        self.idx2userid = {i: o for o, i in self.userid2idx.items()}
        self.idx2courseid = {i: o for o, i in self.courseid2idx.items()}

        self.ratings.course_id = ratings_df.course_id.apply(lambda x: self.courseid2idx[x])
        self.ratings.person_id = ratings_df.person_id.apply(lambda x: self.userid2idx[x])

        self.x = self.ratings.drop(['rating', 'timestamp'], axis=1).values
        self.y = self.ratings['rating'].values
        self.x, self.y = torch.tensor(self.x), torch.tensor(self.y)

    def __getitem__(self, index):
        return (self.x[index], self.y[index])

    def __len__(self):
        return len(self.ratings)

n_factors = 8 

def train_model(ratings_df):
    n_users = ratings_df.person_id.nunique()
    n_items = ratings_df.course_id.nunique()
    model = MatrixFactorization(n_users, n_items, n_factors=n_factors)

    
    train_set = Loader(ratings_df)
    train_loader = DataLoader(train_set, batch_size=128, shuffle=True)

    
    num_epochs = 128
    loss_fn = torch.nn.MSELoss()
    optimizer = torch.optim.Adam(model.parameters(), lr=1e-3)

    for it in range(num_epochs):
        losses = []
        for x, y in train_loader:
            optimizer.zero_grad()
            outputs = model(x)
            loss = loss_fn(outputs.squeeze(), y.type(torch.float32))
            losses.append(loss.item())
            loss.backward()
            optimizer.step()
        print(f"iter #{it} Loss: {sum(losses) / len(losses)}")

            
    return model, train_set

# Hàm gợi ý khóa học cho người dùng
def recommend_courses_for_user(person_id, model, train_set, ratings_df, top_n=8):
    user_idx = train_set.userid2idx[person_id]
    user_embedding = model.user_factors.weight.data[user_idx].cpu().numpy()
    
    course_embeddings = model.item_factors.weight.data.cpu().numpy()
    scores = np.dot(course_embeddings, user_embedding)
    
    recommended_courses_idx = np.argsort(scores)[::-1]
    
    recommended_course_ids = [train_set.idx2courseid[idx] for idx in recommended_courses_idx]
    
    rated_courses = ratings_df[ratings_df['person_id'] == person_id]['course_id'].tolist()
    recommended_course_ids = [course_id for course_id in recommended_course_ids if course_id not in rated_courses]
    
    recommended_course_ids = [int(course_id) for course_id in recommended_course_ids[:top_n]]
    
    return recommended_course_ids

# Tạo endpoint cho việc gợi ý khóa học dựa trên lọc cộng tác
@app.route('/collaborative-recommend', methods=['GET'])
def collaborative_recommend():
    person_id = request.args.get('person_id', type=int)
    
    if person_id is None:
        return jsonify({"error": "person_id is required"}), 400
    
    try:
        courses_df, ratings_df = get_data_from_mysql()
        model, train_set = train_model(ratings_df)  # Huấn luyện lại mô hình mỗi lần có yêu cầu
        recommended_course_ids = recommend_courses_for_user(person_id, model, train_set, ratings_df)
        return jsonify(recommended_course_ids), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

### Phần 2: Lọc nội dung ###

# Cập nhật danh sách đặc trưng cho phương pháp lọc nội dung
features = ['course_title', 'level']

# Hàm kết hợp các đặc trưng
def combineFeatures(row):
    return str(row['course_title']) + " " + str(row['level'])

# Hàm gợi ý khóa học dựa trên lọc nội dung
@app.route('/content-recommend', methods=['GET'])
def content_recommend():
    # Đọc dữ liệu
    courses_df, _ = get_data_from_mysql()
    
    # Áp dụng hàm combineFeatures để tạo ra cột đặc trưng kết hợp
    courses_df['combinedFeatures'] = courses_df.apply(combineFeatures, axis=1)
    
    # Tạo ma trận TF-IDF từ cột kết hợp
    tf = TfidfVectorizer()
    tfMatrix = tf.fit_transform(courses_df['combinedFeatures'])
    
    # Tính toán độ tương đồng cosine
    similar = cosine_similarity(tfMatrix)
    
    course_title = request.args.get('course_title')
    
    if course_title not in courses_df['course_title'].values:
        return jsonify({'error': 'title is not valid'}), 400
    
    indexProduct = courses_df[courses_df['course_title'] == course_title].index[0]
    similarProduct = list(enumerate(similar[indexProduct]))

    # Lọc sản phẩm có độ tương đồng > 0.2 và không bao gồm sản phẩm gốc
    filteredProducts = [(i, score) for i, score in similarProduct if score > 0.2 and i != indexProduct]
    
    number = 5
    if len(filteredProducts) == 0:
        return jsonify({'error': 'No similar products found with a similarity greater than 0.2.'}), 404

    # Chọn ngẫu nhiên 5 khóa học từ danh sách đã lọc
    random_products = random.sample(filteredProducts, min(number, len(filteredProducts)))

    # Thay đổi hàm lay_ten để trả về course_id và chuyển đổi kiểu dữ liệu
    def lay_course_id(index):
        return int(courses_df.loc[index]['course_id'])  # Chuyển đổi kiểu int64 sang int

    # Lấy danh sách course_id và đảm bảo kiểu dữ liệu là int
    ket_qua = [lay_course_id(product[0]) for product in random_products]

    return jsonify(ket_qua), 200


### Phần vô hiệu hóa caching ###
@app.after_request
def add_header(response):
    response.headers['Cache-Control'] = 'no-store'
    return response

# Chạy Flask app
if __name__ == '__main__':
    app.run(debug=True, port=5001)  # Thay 5001 bằng cổng mà bạn muốn sử dụng
