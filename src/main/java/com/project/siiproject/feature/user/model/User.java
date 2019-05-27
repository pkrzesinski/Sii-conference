package com.project.siiproject.feature.user.model;

import com.project.siiproject.feature.lecture.model.Lecture;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Column(unique = true)
    private String login;
    @Email
    @NotEmpty
    @Column(unique = true)
    private String email;
    @ManyToMany
    @JoinTable(name = "USERS_TO_LECTURES",
            joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "lecture_id"))
    private List<Lecture> lectures;

    public User() {
    }

    public User(@NotEmpty String login, @Email @NotEmpty String email, List<Lecture> lectures) {
        this.login = login;
        this.email = email;
        this.lectures = lectures;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", lectures=" + lectures +
                '}';
    }
}
