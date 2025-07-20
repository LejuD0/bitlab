package com.bitlab.mainserviceclean.mapper;

import com.bitlab.mainserviceclean.dto.course.LessonDTO;
import com.bitlab.mainserviceclean.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttachmentMapper.class})
public interface LessonMapper {

    @Mapping(source = "chapter.id", target = "chapterId")
    @Mapping(target = "attachments", ignore = true)
    LessonDTO toDTO(Lesson lesson);

    @Mapping(target = "chapter.id", source = "chapterId")
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    Lesson toEntity(LessonDTO dto);
}








