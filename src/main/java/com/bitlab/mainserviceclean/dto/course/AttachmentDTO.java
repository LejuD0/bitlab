package com.bitlab.mainserviceclean.dto.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDTO {
    private Long id;
    private String name;
    private String url;
    private Long lessonId;
}


