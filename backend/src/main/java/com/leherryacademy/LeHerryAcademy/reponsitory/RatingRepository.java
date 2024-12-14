package com.leherryacademy.LeHerryAcademy.reponsitory;

import com.leherryacademy.LeHerryAcademy.model.Person;
import com.leherryacademy.LeHerryAcademy.model.RatingId;
import com.leherryacademy.LeHerryAcademy.model.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Ratings, RatingId> {
    int countByIdCourseId(int courseId);
    List<Ratings> findByIdCourseId(int courseId);

}
