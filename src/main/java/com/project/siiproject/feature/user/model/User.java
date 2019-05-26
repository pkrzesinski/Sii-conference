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
    @Column(length = 64)
    private String name;
    @NotEmpty
    @Column(length = 64)
    private String surname;
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

    public User(@NotEmpty String name, @NotEmpty String surname, @Email String email, List<Lecture> lectures) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.lectures = lectures;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", lectures=" + lectures +
                '}';
    }
}
