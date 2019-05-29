package com.project.siiproject.feature.lecture.service;

import com.project.siiproject.feature.lecture.dao.LectureRepository;
import com.project.siiproject.feature.lecture.model.Lecture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LectureServiceTest {

    @Mock
    private LectureRepository lectureRepository;
    @InjectMocks
    private LectureService classUnderTest;

    @Test
    public void shouldReturnLectureList() {
        //given
        List<Lecture> lectures = createdMockedList();
        when(lectureRepository.findAll()).thenReturn(lectures);
        //when
        List<Lecture> result = classUnderTest.getAllLectures();
        //then
        assertEquals(3, result.size());
    }

    @Test
    public void shouldSaveLecture() {
        //given
        Lecture lecture = new Lecture();
        //when
        classUnderTest.save(lecture);
        //then
        verify(lectureRepository).save(Mockito.any(Lecture.class));
    }

    @Test
    public void shouldSaveLectureOnlyOneTime() {
        //given
        Lecture lecture = new Lecture();
        //when
        classUnderTest.save(lecture);
        //then
        verify(lectureRepository, Mockito.times(1)).save(Mockito.any(Lecture.class));
    }

    private List<Lecture> createdMockedList() {
        List<Lecture> mockedList = new ArrayList<>();
        mockedList.add(new Lecture());
        mockedList.add(new Lecture());
        mockedList.add(new Lecture());
        return mockedList;
    }
}