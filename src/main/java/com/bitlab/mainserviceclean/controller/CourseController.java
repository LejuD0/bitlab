package com.bitlab.mainserviceclean.controller;

import com.bitlab.mainserviceclean.dto.course.CourseDTO;
import com.bitlab.mainserviceclean.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Курсы", description = "Управление курсами (создание, получение, редактирование, удаление)")
@SecurityRequirement(name = "BearerAuth")
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Получить список всех курсов")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TEACHER')")
    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        log.debug("Получение списка всех курсов");
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @Operation(summary = "Получить курс по ID")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        log.debug("Получение курса по id: {}", id);
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @Operation(summary = "Создать новый курс")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        log.info("Создание нового курса: {}", courseDTO.getName());
        return new ResponseEntity<>(courseService.createCourse(courseDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить существующий курс")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id, @RequestBody CourseDTO courseDTO) {
        log.info("Обновление курса с id: {}", id);
        return ResponseEntity.ok(courseService.updateCourse(id, courseDTO));
    }

    @Operation(summary = "Удалить курс по ID")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        log.info("Удаление курса с id: {}", id);
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}

