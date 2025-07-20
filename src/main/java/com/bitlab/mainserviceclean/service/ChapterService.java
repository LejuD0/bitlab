package com.bitlab.mainserviceclean.service;

import com.bitlab.mainserviceclean.dto.course.ChapterDTO;

import java.util.List;

public interface ChapterService {
    List<ChapterDTO> getAllChapters();
    ChapterDTO getChapterById(Long id);
    ChapterDTO createChapter(ChapterDTO dto);
    ChapterDTO updateChapter(Long id, ChapterDTO dto);
    void deleteChapter(Long id);
}
