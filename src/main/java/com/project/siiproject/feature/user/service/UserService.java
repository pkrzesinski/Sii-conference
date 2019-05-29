package com.project.siiproject.feature.user.service;

import com.project.siiproject.feature.lecture.model.Lecture;
import com.project.siiproject.feature.user.dao.UserRepository;
import com.project.siiproject.feature.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public User getUserById(final Long id) {
        return userRepository.getOne(id);
    }

    public User getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByLogin(final String login) {
        return userRepository.findByLogin(login);
    }

    public User getUserByLoginAndEmail(final String login, final String email) {
        User user = userRepository.findByLoginAndEmail(login, email);
        if (user != null) {
            return user;
        } else
            throw new IllegalStateException();
    }

    public User addNewLecture(User user, Lecture lecture) {
        Optional<Lecture> lectureOptional = user.getLectures().stream()
                .filter(lecture::equals)
                .findFirst();
        Optional<Lecture> lectureAtTheSameTime = user.getLectures().stream()
                .filter(l -> l.getLectureDate().isEqual(lecture.getLectureDate()))
                .findFirst();

        if (lectureOptional.isPresent() || lectureAtTheSameTime.isPresent()) {
            throw new IllegalStateException();
        } else {
            user.getLectures().add(lecture);
            return userRepository.save(user);
        }
    }

    public User save(final User user) {
        if (checkIfUserAlreadyInDatabase(user)) {
            throw new IllegalStateException();
        }
        return userRepository.save(user);
    }

    public User update(User user) {
        if (checkIfUserLoginWithoutChange(user)) {
            return userRepository.save(user);
        }
        throw new IllegalStateException();
    }

    public void delete(final User user) {
        userRepository.delete(user);
    }

    private boolean checkIfUserAlreadyInDatabase(User user) {
        return getUserByEmail(user.getEmail()) != null || getUserByLogin(user.getLogin()) != null;
    }

    private boolean checkIfUserLoginWithoutChange(User user) {
        return getUserById(user.getId()).getLogin().equals(user.getLogin());
    }
}

