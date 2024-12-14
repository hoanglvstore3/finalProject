package com.leherryacademy.LeHerryAcademy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Courses extends BaseEntity{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private int courseId;

    private String courseTitle;

    private String price;

    private String originalPrice;

    private String level;

    private String category;

    private String languagesProgramming;

    private String urlImg;

    private String des;






}
