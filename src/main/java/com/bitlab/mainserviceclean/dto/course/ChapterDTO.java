package com.bitlab.mainserviceclean.dto.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDTO {
    private Long id;
    private String name;
    private String description;
    private int order;
    private Long courseId;
}
