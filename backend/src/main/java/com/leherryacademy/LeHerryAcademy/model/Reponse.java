package com.leherryacademy.LeHerryAcademy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reponse {

    private String statusCode;
    private boolean statusMsg;

}