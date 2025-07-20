package com.bitlab.mainserviceclean.service;

import com.bitlab.mainserviceclean.dto.course.AttachmentDTO;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    AttachmentDTO uploadAttachment(MultipartFile file, Long lessonId);
    byte[] downloadAttachment(Long id);
    void deleteAttachment(Long id);
    AttachmentDTO getAttachmentInfo(Long id);
}


