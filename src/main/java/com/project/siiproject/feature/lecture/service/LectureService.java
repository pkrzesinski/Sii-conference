package com.project.siiproject.feature.lecture.service;

import com.project.siiproject.feature.lecture.dao.LectureRepository;
import com.project.siiproject.feature.lecture.model.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LectureService {

    private final LectureRepository lectureRepository;

    @Autowired
    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public List<Lecture> getAllLectures() {
        return lectureRepository.findAll();
    }

    public Lecture getLectureById(final Long id) {
        return lectureRepository.getOne(id);
    }

    public Lecture save(final Lecture lecture) {
        return lectureRepository.save(lecture);
    }

    public void delete(final Lecture lecture) {
        lectureRepository.delete(lecture);
    }
}
