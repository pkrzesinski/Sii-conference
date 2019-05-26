package com.project.siiproject.lecture.dao;

import com.project.siiproject.lecture.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureDao extends JpaRepository<Lecture, Long> {
}
