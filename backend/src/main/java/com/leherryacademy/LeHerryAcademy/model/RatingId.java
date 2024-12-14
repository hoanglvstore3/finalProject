package com.leherryacademy.LeHerryAcademy.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class RatingId implements Serializable {
    private int personId;
    private int courseId;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingId that = (RatingId) o;
        return Objects.equals(personId, that.personId) &&
                Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, courseId);
    }
}
