package com.bitlab.mainserviceclean.service;

import com.bitlab.mainserviceclean.dto.course.CourseDTO;

import java.util.List;

public interface CourseService {
    List<CourseDTO> getAllCourses();
    CourseDTO getCourseById(Long id);
    CourseDTO createCourse(CourseDTO dto);
    CourseDTO updateCourse(Long id, CourseDTO dto);
    void deleteCourse(Long id);
}
