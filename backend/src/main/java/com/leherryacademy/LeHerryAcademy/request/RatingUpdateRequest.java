package com.leherryacademy.LeHerryAcademy.request;

import lombok.Data;

@Data
public class RatingUpdateRequest {
    private int personId;
    private int courseId;
    private int newRating;
}
