package com.bitlab.mainserviceclean.service.impl;

import com.bitlab.mainserviceclean.dto.course.LessonDTO;
import com.bitlab.mainserviceclean.entity.Chapter;
import com.bitlab.mainserviceclean.entity.Lesson;
import com.bitlab.mainserviceclean.mapper.LessonMapper;
import com.bitlab.mainserviceclean.repository.ChapterRepository;
import com.bitlab.mainserviceclean.repository.LessonRepository;
import com.bitlab.mainserviceclean.service.LessonService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final ChapterRepository chapterRepository;
    private final LessonMapper lessonMapper;

    @Override
    public List<LessonDTO> getAllLessons() {
        return lessonRepository.findAll().stream()
                .map(lessonMapper::toDTO)
                .toList();
    }

    @Override
    public LessonDTO getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Урок не найден: " + id));
        return lessonMapper.toDTO(lesson);
    }

    @Override
    public LessonDTO createLesson(LessonDTO dto) {
        Lesson lesson = lessonMapper.toEntity(dto);
        Chapter chapter = chapterRepository.findById(dto.getChapterId())
                .orElseThrow(() -> new EntityNotFoundException("Глава не найдена: " + dto.getChapterId()));
        lesson.setChapter(chapter);
        lesson.setCreatedTime(LocalDateTime.now());
        lesson.setUpdatedTime(LocalDateTime.now());
        return lessonMapper.toDTO(lessonRepository.save(lesson));
    }

    @Override
    public LessonDTO updateLesson(Long id, LessonDTO dto) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Урок не найден: " + id));
        lesson.setName(dto.getName());
        lesson.setDescription(dto.getDescription());
        lesson.setContent(dto.getContent());
        lesson.setOrder(dto.getOrder());
        lesson.setUpdatedTime(LocalDateTime.now());
        return lessonMapper.toDTO(lessonRepository.save(lesson));
    }

    @Override
    public void deleteLesson(Long id) {
        if (!lessonRepository.existsById(id)) {
            throw new EntityNotFoundException("Урок с ID " + id + " не найден");
        }
        lessonRepository.deleteById(id);
    }

}

