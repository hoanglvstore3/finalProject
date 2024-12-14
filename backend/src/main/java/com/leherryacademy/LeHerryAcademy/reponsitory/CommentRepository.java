package com.leherryacademy.LeHerryAcademy.reponsitory;

import com.leherryacademy.LeHerryAcademy.model.Comments;
import com.leherryacademy.LeHerryAcademy.model.Courses;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.xml.stream.events.Comment;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Integer> {
    List<Comments> findByCoursesCourseIdAndParentIsNull(int courseId);

    List<Comments> findByParentCommentIdIn(List<Integer> parentIds);
}
