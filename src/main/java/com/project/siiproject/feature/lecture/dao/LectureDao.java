package com.project.siiproject.feature.lecture.dao;

import com.project.siiproject.feature.lecture.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureDao extends JpaRepository<Lecture, Long> {
}
