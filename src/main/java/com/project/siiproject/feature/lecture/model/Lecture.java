package com.project.siiproject.feature.lecture.model;

import com.project.siiproject.feature.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "LECTURES")
public class Lecture {

    @Id
    @Column(name = "lecture_id")
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String topic;
    @NotEmpty
    private String lecturer;
    private LocalDateTime lectureDate;
    @Min(1)
    private int path;
    @ManyToMany(mappedBy = "lectures")
    private List<User> users;
}
