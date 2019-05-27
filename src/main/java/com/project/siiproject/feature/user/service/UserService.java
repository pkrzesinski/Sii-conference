package com.project.siiproject.feature.user.service;

import com.project.siiproject.feature.user.dao.UserRepository;
import com.project.siiproject.feature.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User save(final User user) {
        if (checkIfUserAlreadyInDatabase(user)) {
            return userRepository.save(user);
        }
        throw new IllegalStateException();
    }

    public void delete(final User user) {
        userRepository.delete(user);
    }

    public User getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByLogin(final String login) {
        return userRepository.findByLogin(login);
    }

    private boolean checkIfUserAlreadyInDatabase(User user) {
        if (getUserByEmail(user.getEmail()) != null || getUserByLogin(user.getLogin()) != null) {
            return true;
        }
        return false;
    }
}
