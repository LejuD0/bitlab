package com.bitlab.mainserviceclean.service.impl;

import com.bitlab.mainserviceclean.dto.course.ChapterDTO;
import com.bitlab.mainserviceclean.entity.Chapter;
import com.bitlab.mainserviceclean.entity.Course;
import com.bitlab.mainserviceclean.mapper.ChapterMapper;
import com.bitlab.mainserviceclean.repository.ChapterRepository;
import com.bitlab.mainserviceclean.repository.CourseRepository;
import com.bitlab.mainserviceclean.service.ChapterService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;
    private final ChapterMapper chapterMapper;

    @Override
    public List<ChapterDTO> getAllChapters() {
        return chapterRepository.findAll().stream()
                .map(chapterMapper::toDTO)
                .toList();
    }

    @Override
    public ChapterDTO getChapterById(Long id) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Глава не найдена: " + id));
        return chapterMapper.toDTO(chapter);
    }

    @Override
    public ChapterDTO createChapter(ChapterDTO dto) {
        Chapter chapter = chapterMapper.toEntity(dto);
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Курс не найден: " + dto.getCourseId()));
        chapter.setCourse(course);
        chapter.setCreatedTime(LocalDateTime.now());
        chapter.setUpdatedTime(LocalDateTime.now());
        return chapterMapper.toDTO(chapterRepository.save(chapter));
    }

    @Override
    public ChapterDTO updateChapter(Long id, ChapterDTO dto) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Глава не найдена: " + id));
        chapter.setName(dto.getName());
        chapter.setDescription(dto.getDescription());
        chapter.setOrder(dto.getOrder());
        chapter.setUpdatedTime(LocalDateTime.now());
        return chapterMapper.toDTO(chapterRepository.save(chapter));
    }

    @Override
    public void deleteChapter(Long id) {
        if (!chapterRepository.existsById(id)) {
            throw new EntityNotFoundException("Глава с ID " + id + " не найдена");
        }
        chapterRepository.deleteById(id);
    }

}
