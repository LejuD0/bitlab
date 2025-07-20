package com.bitlab.mainserviceclean.mapper;

import com.bitlab.mainserviceclean.dto.course.ChapterDTO;
import com.bitlab.mainserviceclean.entity.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChapterMapper {

    @Mapping(source = "course.id", target = "courseId")
    ChapterDTO toDTO(Chapter chapter);

    @Mapping(target = "course", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    Chapter toEntity(ChapterDTO dto);
}






