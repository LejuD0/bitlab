package com.bitlab.mainserviceclean.controller;

import com.bitlab.mainserviceclean.dto.course.ChapterDTO;
import com.bitlab.mainserviceclean.service.ChapterService;
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
@RequestMapping("/api/chapter")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Главы", description = "Операции для управления главами курсов")
@SecurityRequirement(name = "BearerAuth")
public class ChapterController {

    private final ChapterService chapterService;

    @Operation(summary = "Получить список всех глав")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TEACHER')")
    @GetMapping
    public ResponseEntity<List<ChapterDTO>> getAllChapters() {
        log.debug("Получение всех глав");
        return ResponseEntity.ok(chapterService.getAllChapters());
    }

    @Operation(summary = "Получить главу по ID")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<ChapterDTO> getChapterById(@PathVariable Long id) {
        log.debug("Получение главы по id: {}", id);
        return ResponseEntity.ok(chapterService.getChapterById(id));
    }

    @Operation(summary = "Создать новую главу")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ChapterDTO> createChapter(@RequestBody ChapterDTO chapterDTO) {
        log.info("Создание новой главы: {}", chapterDTO.getName());
        return new ResponseEntity<>(chapterService.createChapter(chapterDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить главу по ID")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ChapterDTO> updateChapter(@PathVariable Long id, @RequestBody ChapterDTO chapterDTO) {
        log.info("Обновление главы с id: {}", id);
        return ResponseEntity.ok(chapterService.updateChapter(id, chapterDTO));
    }

    @Operation(summary = "Удалить главу по ID")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        log.info("Удаление главы с id: {}", id);
        chapterService.deleteChapter(id);
        return ResponseEntity.noContent().build();
    }
}


