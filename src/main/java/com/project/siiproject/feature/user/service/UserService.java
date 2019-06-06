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
        LOG.info("User: {} was looked in database", login);
        return userRepository.findByLogin(login);
    }

    public User getUserByLoginAndEmail(final String login, final String email) {
        User user = userRepository.findByLoginAndEmail(login, email);
        if (user != null) {
            LOG.info("User: {}, email: {} was found in database", login, email);
            return user;
        } else
            LOG.info("User: {}, email: {} was not found in database", login, email);
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
            LOG.warn("User: {} has tried to add lecture, but failed.", user.getLogin());
            throw new IllegalStateException();
        } else {
            user.getLectures().add(lecture);
            LOG.info("User: {} has enrolled for lecture: {}", user.getLogin(), lecture.getTitle());
            return userRepository.save(user);
        }
    }

    public User save(final User user) {
        if (isUserAlreadyInDatabase(user)) {
            LOG.warn("User: {} is already in database, failed to save.", user.getLogin());
            throw new IllegalStateException();
        }
        LOG.info("New user added to database: {}", user.getLogin());
        return userRepository.save(user);
    }

    public User update(User user) {
        if (isUserLoginWithoutChange(user)) {
            LOG.info("User: {} has updated profile", user.getLogin());
            return userRepository.save(user);
        }
        LOG.warn("User: {} has tried updated profile, but failed.", user.getLogin());
        throw new IllegalStateException();
    }

    public User emailUpdate(User user) {
        if (isUserLoginWithoutChange(user) && !isEmailAlreadyInDataBase(user)) {
            User updateUser = getUserByLogin(user.getLogin());
            updateUser.setEmail(user.getEmail());
            LOG.info("User {} has changed email address.", user.getLogin());
            return userRepository.save(updateUser);
        }
        LOG.warn("User: {} has tired to changed email address, but failed.", user.getLogin());
        throw new IllegalStateException();
    }

    public void delete(final User user) {
        userRepository.delete(user);
        LOG.warn("User: {} has been deleted.", user.getLogin());
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
