package com.bitlab.mainserviceclean.service.impl;

import com.bitlab.mainserviceclean.dto.course.CourseDTO;
import com.bitlab.mainserviceclean.entity.Course;
import com.bitlab.mainserviceclean.mapper.CourseMapper;
import com.bitlab.mainserviceclean.repository.CourseRepository;
import com.bitlab.mainserviceclean.service.CourseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository repository;
    private final CourseMapper mapper;

    @Override
    public List<CourseDTO> getAllCourses() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public CourseDTO getCourseById(Long id) {
        Course course = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Курс не найден: " + id));
        return mapper.toDTO(course);
    }

    @Override
    public CourseDTO createCourse(CourseDTO dto) {
        Course course = mapper.toEntity(dto);
        course.setCreatedTime(LocalDateTime.now());
        course.setUpdatedTime(LocalDateTime.now());
        return mapper.toDTO(repository.save(course));
    }

    @Override
    public CourseDTO updateCourse(Long id, CourseDTO dto) {
        Course course = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Курс не найден: " + id));
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setUpdatedTime(LocalDateTime.now());
        return mapper.toDTO(repository.save(course));
    }

    @Override
    public void deleteCourse(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Курс не найден: " + id);
        }
        repository.deleteById(id);
    }
}
