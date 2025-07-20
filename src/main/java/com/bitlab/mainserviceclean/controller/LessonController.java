package com.bitlab.mainserviceclean.controller;

import com.bitlab.mainserviceclean.dto.course.LessonDTO;
import com.bitlab.mainserviceclean.service.LessonService;
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
@RequestMapping("/api/lesson")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Уроки", description = "Операции для управления уроками")
@SecurityRequirement(name = "BearerAuth")
public class LessonController {

    private final LessonService lessonService;

    @Operation(summary = "Получить список всех уроков")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TEACHER')")
    @GetMapping
    public ResponseEntity<List<LessonDTO>> getAllLessons() {
        log.debug("Получение всех уроков");
        return ResponseEntity.ok(lessonService.getAllLessons());
    }

    @Operation(summary = "Получить урок по ID")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<LessonDTO> getLessonById(@PathVariable Long id) {
        log.debug("Получение урока по id: {}", id);
        return ResponseEntity.ok(lessonService.getLessonById(id));
    }

    @Operation(summary = "Создать новый урок")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<LessonDTO> createLesson(@RequestBody LessonDTO lessonDTO) {
        log.info("Создание нового урока: {}", lessonDTO.getName());
        return new ResponseEntity<>(lessonService.createLesson(lessonDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить урок по ID")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<LessonDTO> updateLesson(@PathVariable Long id, @RequestBody LessonDTO lessonDTO) {
        log.info("Обновление урока с id: {}", id);
        return ResponseEntity.ok(lessonService.updateLesson(id, lessonDTO));
    }

    @Operation(summary = "Удалить урок по ID")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        log.info("Удаление урока с id: {}", id);
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}


