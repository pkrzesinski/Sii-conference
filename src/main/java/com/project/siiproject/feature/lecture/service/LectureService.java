package com.project.siiproject.feature.lecture.service;

import com.project.siiproject.feature.lecture.dao.LectureRepository;
import com.project.siiproject.feature.lecture.model.Lecture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LectureService {

    private static final Logger LOG = LogManager.getLogger(LectureService.class);
    private final LectureRepository lectureRepository;

    @Autowired
    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public List<Lecture> getAllLectures() {
        LOG.info("All lectures have been displayed.");
        return lectureRepository.findAll();
    }

    public Lecture getLectureById(final Long id) {
        LOG.info("Lecture with id: {} has been looked in database.", id);
        return lectureRepository.getOne(id);
    }

    public Lecture getLectureByTitle(String title) {
        LOG.info("Lecture: {} has been looked in database", title);
        return lectureRepository.findByTitle(title);
    }

    public Lecture save(final Lecture lecture) {
        LOG.info("New lecture has been saved in database: {}", lecture.getTitle());
        return lectureRepository.save(lecture);
    }

    public void delete(final Lecture lecture) {
        LOG.warn("Lecture: {} has been deleted.", lecture.getTitle());
        lectureRepository.delete(lecture);
    }
}
