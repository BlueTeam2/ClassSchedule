package com.softserve.mapper;

import com.softserve.dto.LessonForTeacherScheduleDTO;
import com.softserve.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonForTeacherScheduleMapper {
    @Mapping(source = "lessonType", target = "lessonType")
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "temporaryScheduleDTO", ignore = true)
    LessonForTeacherScheduleDTO lessonToLessonForTeacherScheduleDTO(Lesson lesson);
}
