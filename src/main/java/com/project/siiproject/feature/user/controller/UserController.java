package com.project.siiproject.feature.user.controller;

import com.project.siiproject.feature.user.model.User;
import com.project.siiproject.feature.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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

    @PutMapping
    public User update(@RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping
    public void delete(@RequestBody User user) {
        userService.delete(user);
    }
}
