package com.project.siiproject.feature.user.controller;

import com.project.siiproject.feature.lecture.model.Lecture;
import com.project.siiproject.feature.lecture.service.LectureService;
import com.project.siiproject.feature.user.model.User;
import com.project.siiproject.feature.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;
    private final LectureService lectureService;

    @Autowired
    public UserController(UserService userService, LectureService lectureService) {
        this.userService = userService;
        this.lectureService = lectureService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{login}")
    public User getUserByLogin(@PathVariable("login") String login) {
        return userService.getUserByLogin(login);
    }

    @PostMapping
    public User save(@RequestBody User user) {
        try {
            return userService.save(user);
        } catch (IllegalStateException e) {
            System.out.println("User already exist: " + e);
        }
        return null;
    }

    @PostMapping("/add-lecture/{title}")
    public void addLecture(@RequestBody User user, @PathVariable String title) {
        Lecture lecture = lectureService.getLectureByTitle(title);
        user.getLectures().add(lecture);
        userService.update(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping
    public void delete(@RequestBody User user) {
        userService.delete(user);
    }
}
