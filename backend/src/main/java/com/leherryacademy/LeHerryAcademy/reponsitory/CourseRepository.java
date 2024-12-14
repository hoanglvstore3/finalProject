package com.leherryacademy.LeHerryAcademy.reponsitory;

import com.leherryacademy.LeHerryAcademy.model.Courses;
import com.leherryacademy.LeHerryAcademy.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Courses, Integer> {
    @Query("SELECT AVG(r.rating) FROM Ratings r WHERE r.id.courseId = ?1")
    Double findAverageRatingByCourseId(int courseId);

    @Query("SELECT c, AVG(r.rating) AS averageRating " +
            "FROM Courses c LEFT JOIN Ratings r ON c.courseId = r.id.courseId " +
            "WHERE c.courseId IN :courseIds " +
            "GROUP BY c.courseId")
    List<Object> findCoursesWithAverageRating(@Param("courseIds") List<Integer> courseIds);

    @Query("SELECT c, AVG(r.rating) AS averageRating " +
            "FROM Courses c LEFT JOIN Ratings r ON c.courseId = r.id.courseId " +
            "WHERE c.courseId = :courseId " +
            "GROUP BY c.courseId")
    Object findCourseWithAverageRatingByID(@Param("courseId") int courseId);

    @Query("SELECT c, AVG(r.rating) AS averageRating " +
            "FROM Courses c LEFT JOIN Ratings r ON c.courseId = r.id.courseId " +
            "WHERE c.courseTitle = :courseTitle " +
            "GROUP BY c.courseId")
    Object findCourseWithAverageRatingByCourseTitle(String courseTitle);


    @Query("SELECT c, AVG(r.rating) AS averageRating " +
            "FROM Courses c LEFT JOIN Ratings r ON c.courseId = r.id.courseId " +
            "WHERE c.price = 'Free' " +
            "GROUP BY c.courseId " +
            "ORDER BY RAND()"
            + "LIMIT 8"
    )
    List<Object> findCourseWithAverageRatingFreeRandom();

    @Query("SELECT c, AVG(r.rating) AS averageRating " +
            "FROM Courses c LEFT JOIN Ratings r ON c.courseId = r.id.courseId " +
            "GROUP BY c.courseId " +
            "ORDER BY c.courseId DESC " +
            "LIMIT 8"
    )
    List<Object> findFinalCourseWithAverageRating();

//    @Query("SELECT c, AVG(r.rating) AS averageRating " +
//            "FROM Courses c LEFT JOIN Ratings r ON c.courseId = r.id.courseId " +
//            "WHERE (:languages IS NULL OR c.languagesProgramming IN :languages) " +
//            "AND (:categories IS NULL OR c.category IN :categories) " +
//            "AND (:levels IS NULL OR c.level IN :levels) " +
//            "AND ((:minPrice IS NULL AND :maxPrice IS NULL AND :freePrice IS NULL) OR c.price = :freePrice OR (CAST(c.price AS double) >= :minPrice AND CAST(c.price AS double) <= :maxPrice)) " +
//            "GROUP BY c.courseId " +
//            "HAVING (:minRating IS NULL OR AVG(r.rating) >= :minRating)")
//    Page<Object> findAllCoursesByFilter(
//            Double minRating,
//            List<String> languages,
//            List<String> categories,
//            List<String> levels,
//            Double minPrice,
//            Double maxPrice,
//            String freePrice,
//            Pageable pageable);

    @Query("SELECT c, AVG(r.rating) AS averageRating " +
            "FROM Courses c LEFT JOIN Ratings r ON c.courseId = r.id.courseId " +
            "WHERE (:languages IS NULL OR c.languagesProgramming IN :languages) " +
            "AND (:categories IS NULL OR c.category IN :categories) " +
            "AND (:levels IS NULL OR c.level IN :levels) " +
            "AND ((:minPrice1 IS NULL AND :maxPrice1 IS NULL AND :minPrice2 IS NULL AND :maxPrice2 IS NULL AND" +
            ":minPrice3 IS NULL AND :maxPrice3 IS NULL AND" +
            ":minPrice4 IS NULL AND :maxPrice4 IS NULL AND" +
            ":minPrice5 IS NULL AND :maxPrice5 IS NULL AND :freePrice IS NULL) OR (:freePrice IS NOT NULL AND c.price = :freePrice) " +
            "OR (:minPrice1 IS NOT NULL AND :maxPrice1 IS NOT NULL AND CAST(c.price AS double) >= :minPrice1 AND CAST(c.price AS double) <= :maxPrice1) " +
            "OR (:minPrice2 IS NOT NULL AND :maxPrice2 IS NOT NULL AND CAST(c.price AS double) >= :minPrice2 AND CAST(c.price AS double) <= :maxPrice2)" +
            "OR (:minPrice3 IS NOT NULL AND :maxPrice3 IS NOT NULL AND CAST(c.price AS double) >= :minPrice3 AND CAST(c.price AS double) <= :maxPrice3)" +
            "OR (:minPrice4 IS NOT NULL AND :maxPrice4 IS NOT NULL AND CAST(c.price AS double) >= :minPrice4 AND CAST(c.price AS double) <= :maxPrice4)" +
            "OR (:minPrice5 IS NOT NULL AND :maxPrice5 IS NOT NULL AND CAST(c.price AS double) >= :minPrice5 AND CAST(c.price AS double) <= :maxPrice5)) " +
            "GROUP BY c.courseId " +
            "HAVING (:minRating IS NULL OR AVG(r.rating) >= :minRating)")
    Page<Object> findAllCoursesByFilter(
            Double minRating,
            List<String> languages,
            List<String> categories,
            List<String> levels,
            Double minPrice1,
            Double maxPrice1,
            Double minPrice2,
            Double maxPrice2,
            Double minPrice3,
            Double maxPrice3,
            Double minPrice4,
            Double maxPrice4,
            Double minPrice5,
            Double maxPrice5,
            String freePrice,
            Pageable pageable);

}
