package com.leherryacademy.LeHerryAcademy.request;

import lombok.Data;

@Data
public class CommentRequest {
    String content;
    String datetime;
    int courseId;
    int personId;
    int parentId;
}
