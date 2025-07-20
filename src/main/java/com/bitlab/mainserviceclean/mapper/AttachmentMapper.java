package com.bitlab.mainserviceclean.mapper;

import com.bitlab.mainserviceclean.dto.course.AttachmentDTO;
import com.bitlab.mainserviceclean.entity.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {

    @Mapping(source = "fileName", target = "name")
    @Mapping(source = "lesson.id", target = "lessonId")  // <-- вот это добавляем
    AttachmentDTO toDTO(Attachment attachment);
}


