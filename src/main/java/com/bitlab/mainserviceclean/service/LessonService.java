package com.bitlab.mainserviceclean.service;

import com.bitlab.mainserviceclean.dto.course.LessonDTO;

import java.util.List;

public interface LessonService {
    List<LessonDTO> getAllLessons();
    LessonDTO getLessonById(Long id);
    LessonDTO createLesson(LessonDTO dto);
    LessonDTO updateLesson(Long id, LessonDTO dto);
    void deleteLesson(Long id);
}

