import os
import time
import google.generativeai as genai
from flask import Flask, request, jsonify
from flask_cors import CORS

# Cấu hình khóa API của Google AI

genai.configure(api_key=os.environ["AI_API_KEY"])

# Khởi tạo Flask
app = Flask(__name__)
CORS(app)  # Thêm CORS vào Flask

# Hàm upload file lên Gemini
def upload_to_gemini(path, mime_type=None):
    file = genai.upload_file(path, mime_type=mime_type)
    print(f"Uploaded file '{file.display_name}' as: {file.uri}")
    return file

# Hàm chờ các file được xử lý xong
def wait_for_files_active(files):
    print("Waiting for file processing...")
    for name in (file.name for file in files):
        file = genai.get_file(name)
        while file.state.name == "PROCESSING":
            print(".", end="", flush=True)
            time.sleep(5)
            file = genai.get_file(name)
        if file.state.name != "ACTIVE":
            raise Exception(f"File {file.name} failed to process")
    print("...all files ready")
    print()

# Cấu hình mô hình Generative
generation_config = {
    "temperature": 0.2,
    "top_p": 0.95,
    "top_k": 32,
    "max_output_tokens": 4096,
    "response_mime_type": "text/plain",
}

model = genai.GenerativeModel(
    model_name="gemini-2.0-flash-exp",
    generation_config=generation_config,
)

# Tạo API với hai tham số ngành và cấp độ
@app.route('/get_courses', methods=['POST'])
def get_courses():
    data = request.json
    industry = data.get("industry", "front-end")
    level = data.get("level", "cơ bản")

    # Upload file lên Gemini
    files = [
        upload_to_gemini("course.csv", mime_type="text/csv"),
    ]

    # Chờ file sẵn sàng
    wait_for_files_active(files)

    # Tạo session chat
    chat_session = model.start_chat(
        history=[
            {
                "role": "user",
                "parts": [
                    files[0],
                ],
            },
        ]
    )

    # Tạo câu hỏi dựa trên ngành và cấp độ
    query = f"From the data I provided, briefly list 5 course_title for 1 learning path {industry} {level}"
    
    # Gửi câu hỏi và nhận câu trả lời từ AI
    response = chat_session.send_message(query)
    
    # Trả về kết quả dưới dạng JSON
    return jsonify({"response": response.text})

# Chạy API
if __name__ == '__main__':
    app.run(debug=True)
