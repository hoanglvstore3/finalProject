package com.leherryacademy.LeHerryAcademy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leherryacademy.LeHerryAcademy.annotation.PasswordValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity

public class Person extends BaseEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private int personId;

    @NotBlank(message="Name must not be blank")
    @Size(min=3, message="Name must be at least 3 characters long")
    private String name;

    @NotBlank(message="Mobile number must not be blank")
    @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @NotBlank(message="Email must not be blank")
    @Email(message = "Please provide a valid email address" )
    private String email;


    @NotBlank(message="Password must not be blank")
    @Size(min=5, message="Password must be at least 5 characters long")
    @PasswordValidator
    @JsonIgnore
    private String pwd;

    private String accountFrom;

    private String urlAvt;


    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST, targetEntity = Roles.class)
    @JoinColumn(name = "role_id", referencedColumnName = "roleId",nullable = false)
    private Roles roles;



}
