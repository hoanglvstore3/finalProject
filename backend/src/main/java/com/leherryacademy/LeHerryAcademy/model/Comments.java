package com.leherryacademy.LeHerryAcademy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Comments {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private int commentId;

    @NotBlank(message="Content must not be blank")
    @Size(min=15, message="Content must be at least 15 characters long")
    private String content;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    @JsonIgnore
    private Courses courses;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Comments parent;



}
