package com.leherryacademy.LeHerryAcademy.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Ratings {
    @EmbeddedId
    private RatingId id;

    private int rating;

    private LocalDateTime timestamp;
}
