package com.project.siiproject.feature.user.service;

import com.project.siiproject.feature.lecture.model.Lecture;
import com.project.siiproject.feature.user.dao.UserRepository;
import com.project.siiproject.feature.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        List<Lecture> usersLecture = user.getLectures();
        LocalDateTime lectureTime = lecture.getLectureDate();
        Optional<Lecture> lectureOptional = usersLecture.stream()
                .filter(lecture::equals)
                .findFirst();
        Optional<Lecture> lectureAtTheSameTime = usersLecture.stream()
                .filter(l -> l.getLectureDate().isEqual(lectureTime))
                .findFirst();

        if ( lectureOptional.isPresent() || lectureAtTheSameTime.isPresent() || isLectureFull(lecture)){
            throw new IllegalStateException();
        } else{
            user.getLectures().add(lecture);
            return userRepository.save(user);
        }
    }



    public User save(final User user) {
        if (isUserAlreadyInDatabase(user)) {
            throw new IllegalStateException();
        }
        User newUser = new User(user.getLogin(), user.getEmail());
        return userRepository.save(newUser);
    }

    public User update(User user) {
        if (isUserLoginWithoutChange(user)) {
            return userRepository.save(user);
        }
        throw new IllegalStateException();
    }

    public User emailUpdate(User user) {
        if (isUserLoginWithoutChange(user) && !isEmailAlreadyInDataBase(user)) {
            User updateUser = getUserByLogin(user.getLogin());
            updateUser.setEmail(user.getEmail());
            return userRepository.save(updateUser);
        }
        throw new IllegalStateException();
    }

    public void delete(final User user) {
        userRepository.delete(user);
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
