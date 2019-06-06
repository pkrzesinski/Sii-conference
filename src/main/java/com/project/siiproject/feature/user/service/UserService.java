package com.project.siiproject.feature.user.service;

import com.project.siiproject.feature.lecture.model.Lecture;
import com.project.siiproject.feature.user.dao.UserRepository;
import com.project.siiproject.feature.user.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private static final Logger LOG = LogManager.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(final Long id) {
        LOG.info("User with ID: {} was looked in database", id);
        return userRepository.getOne(id);
    }

    public User getUserByEmail(final String email) {
        LOG.info("User with email: {} was looked in database", email);
        return userRepository.findByEmail(email);
    }

    public User getUserByLogin(final String login) {
        LOG.info("User with login: {} was looked in database", login);
        return userRepository.findByLogin(login);
    }

    public User getUserByLoginAndEmail(final String login, final String email) {
        User user = userRepository.findByLoginAndEmail(login, email);
        if (user != null) {
            LOG.info("User with login " + login + " and email " + email + " was found in database");
            return user;
        } else
            LOG.info("User with login " + login + " and email " + email + " was not found in database");
        throw new IllegalStateException();
    }

    public User addNewLecture(User user, Lecture lecture) {
        List<Lecture> usersLecture = user.getLectures();
        LocalDateTime lectureTime = lecture.getLectureDate();
        Optional<Lecture> lectureOptional = usersLecture.stream()
                .filter(lecture::equals)
                .findFirst();
        Optional<Lecture> lectureAtTheSameTime = usersLecture.stream()
                .filter(l -> l.getLectureDate().isEqual(lectureTime))
                .findFirst();

        if (lectureOptional.isPresent() || lectureAtTheSameTime.isPresent() || isLectureFull(lecture)) {
            LOG.warn("User: " + user.getLogin() + " has tried to add lecture, but failed.");
            throw new IllegalStateException();
        } else {
            user.getLectures().add(lecture);
            LOG.info("User: " + user.getLogin() + " has added lecture: " + lecture.getTitle());
            return userRepository.save(user);
        }
    }

    public User save(final User user) {
        if (isUserAlreadyInDatabase(user)) {
            LOG.warn("User " + user.getLogin() + " is already in database, cannot save.");
            throw new IllegalStateException();
        }
        LOG.info("New user added to database: " + user.getLogin());
        return userRepository.save(user);
    }

    public User update(User user) {
        if (isUserLoginWithoutChange(user)) {
            LOG.info(user.getLogin() + " has updated profile");
            return userRepository.save(user);
        }
        LOG.warn(user.getLogin() + " has tried updated profile, but failed.");
        throw new IllegalStateException();
    }

    public User emailUpdate(User user) {
        if (isUserLoginWithoutChange(user) && !isEmailAlreadyInDataBase(user)) {
            User updateUser = getUserByLogin(user.getLogin());
            updateUser.setEmail(user.getEmail());
            LOG.info("User " + user.getLogin() + " has changed email address.");
            return userRepository.save(updateUser);
        }
        LOG.warn("User " + user.getLogin() + " has tired to changed email address, but failed.");
        throw new IllegalStateException();
    }

    public void delete(final User user) {
        userRepository.delete(user);
        LOG.warn("User " + user.getLogin() + " has been deleted.");
    }

    private boolean isUserAlreadyInDatabase(User user) {
        return getUserByEmail(user.getEmail()) != null || getUserByLogin(user.getLogin()) != null;
    }

    private boolean isUserLoginWithoutChange(User user) {
        return getUserById(user.getId()).getLogin().equals(user.getLogin());
    }

    private boolean isEmailAlreadyInDataBase(User user) {
        if (getUserByEmail(user.getEmail()) != null) {
            return true;
        }
        return false;
    }

    private boolean isLectureFull(Lecture lecture) {
        if (lecture.getUsers().size() >= 5) {
            return true;
        }
        return false;
    }
}
