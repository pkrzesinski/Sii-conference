package com.project.siiproject.feature.enrollment;

import com.project.siiproject.feature.lecture.dao.LectureRepository;
import com.project.siiproject.feature.lecture.model.Lecture;
import com.project.siiproject.feature.user.dao.UserRepository;
import com.project.siiproject.feature.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EnrollmentService {

    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;

    @Autowired
    public EnrollmentService(UserRepository userRepository, LectureRepository lectureRepository) {
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
    }

    public Lecture enrollForLecture(User user, Lecture lecture) {
        user.setLectures((List<Lecture>) lecture);
        return lecture;
    }

}
