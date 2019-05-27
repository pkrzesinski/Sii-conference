package com.project.siiproject.feature.lecture.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.siiproject.feature.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "LECTURES",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"lecture_date", "path"})})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Lecture {

    @Id
    @Column(name = "lecture_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Column(unique = true)
    private String title;
    @FutureOrPresent
    @Column(name = "lecture_date")
    private LocalDateTime lectureDate;
    @Positive
    @Column(name = "path")
    private int path;
    @ManyToMany(mappedBy = "lectures")
    private List<User> users;

    public Lecture() {
    }

    public Lecture(@NotEmpty String title, LocalDateTime lectureDate, @Min(1) int path, List<User> users) {
        this.title = title;
        this.lectureDate = lectureDate;
        this.path = path;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getLectureDate() {
        return lectureDate;
    }

    public void setLectureDate(LocalDateTime lectureDate) {
        this.lectureDate = lectureDate;
    }

    public int getPath() {
        return path;
    }

    public void setPath(int path) {
        this.path = path;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", lectureDate=" + lectureDate +
                ", path=" + path +
                ", users=" + users +
                '}';
    }
}
