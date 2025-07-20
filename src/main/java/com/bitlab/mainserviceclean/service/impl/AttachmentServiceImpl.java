package com.bitlab.mainserviceclean.service.impl;

import com.bitlab.mainserviceclean.dto.course.AttachmentDTO;
import com.bitlab.mainserviceclean.entity.Attachment;
import com.bitlab.mainserviceclean.entity.Lesson;
import com.bitlab.mainserviceclean.mapper.AttachmentMapper;
import com.bitlab.mainserviceclean.repository.AttachmentRepository;
import com.bitlab.mainserviceclean.repository.LessonRepository;
import com.bitlab.mainserviceclean.service.AttachmentService;
import com.bitlab.mainserviceclean.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final LessonRepository lessonRepository;
    private final FileStorageService fileStorageService;
    private final AttachmentMapper attachmentMapper;

    @Override
    public AttachmentDTO uploadAttachment(MultipartFile file, Long lessonId) {
        String url = fileStorageService.uploadFile(file);

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));

        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setUrl(url);
        attachment.setLesson(lesson);
        attachment.setCreatedTime(LocalDateTime.now());

        return attachmentMapper.toDTO(attachmentRepository.save(attachment));
    }

    @Override
    public byte[] downloadAttachment(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found"));

        try (InputStream inputStream = fileStorageService.downloadFile(attachment.getUrl())) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при скачивании файла", e);
        }

    }

    @Override
    public void deleteAttachment(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found"));

        fileStorageService.deleteFile(attachment.getUrl());
        attachmentRepository.delete(attachment);
    }

    @Override
    public AttachmentDTO getAttachmentInfo(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found"));
        return attachmentMapper.toDTO(attachment);
    }
}

