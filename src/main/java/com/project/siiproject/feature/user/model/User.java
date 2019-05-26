package com.project.siiproject.feature.user.model;

import com.project.siiproject.feature.lecture.model.Lecture;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class User {

    @Id
    @Column(name = "user_id")
    private Long id;
    @NotEmpty
    @Column(length = 64)
    private String name;
    @NotEmpty
    @Column(length = 64)
    private String surname;
    @Email
    private String email;
    @ManyToMany
    @JoinTable(name = "USERS_TO_LECTURES",
    joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "lecture_id"))
    private List<Lecture> lectures;


}
