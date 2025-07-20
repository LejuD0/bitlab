package com.bitlab.mainserviceclean.dto.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonDTO {
    private Long id;
    private String name;
    private String description;
    private String content;
    private int order;
    private Long chapterId;
    private List<AttachmentDTO> attachments;
}

