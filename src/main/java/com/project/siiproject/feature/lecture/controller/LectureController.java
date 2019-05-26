package com.project.siiproject.feature.lecture.controller;

import com.project.siiproject.feature.lecture.model.Lecture;
import com.project.siiproject.feature.lecture.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/lectures")
public class LectureController {

    private final LectureService lectureService;

    @Autowired
    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping
    public List<Lecture> getAllLectures() {
        return lectureService.getAllLectures();
    }

    @GetMapping("{id}")
    public Lecture getLectureById(@PathVariable final Long id) {
        return lectureService.getLectureById(id);
    }

    @PostMapping
    public Lecture save(@RequestBody final Lecture lecture) {
        return lectureService.save(lecture);
    }

    @PutMapping
    public Lecture update(@RequestBody final Lecture lecture) {
        return lectureService.save(lecture);
    }

    @DeleteMapping
    public void delete(@RequestBody final Lecture lecture) {
        lectureService.delete(lecture);
    }
}
