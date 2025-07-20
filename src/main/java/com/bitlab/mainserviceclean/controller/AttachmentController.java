package com.bitlab.mainserviceclean.controller;

import com.bitlab.mainserviceclean.dto.course.AttachmentDTO;
import com.bitlab.mainserviceclean.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping
    public AttachmentDTO upload(@RequestParam("file") MultipartFile file,
                                @RequestParam("lessonId") Long lessonId) {
        return attachmentService.uploadAttachment(file, lessonId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {
        AttachmentDTO attachment = attachmentService.getAttachmentInfo(id);
        byte[] file = attachmentService.downloadAttachment(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + attachment.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        attachmentService.deleteAttachment(id);
    }
}


