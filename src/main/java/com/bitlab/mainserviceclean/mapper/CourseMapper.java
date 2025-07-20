package com.bitlab.mainserviceclean.mapper;

import com.bitlab.mainserviceclean.dto.course.CourseDTO;
import com.bitlab.mainserviceclean.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ChapterMapper.class})
public interface CourseMapper {

    @Mapping(source = "chapters", target = "chapterList")
    CourseDTO toDTO(Course course);

    @Mapping(source = "chapterList", target = "chapters")
    Course toEntity(CourseDTO dto);
}


