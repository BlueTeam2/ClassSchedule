package com.softserve.mapper;

import com.softserve.dto.LessonInfoDTO;
import com.softserve.dto.LessonsInScheduleDTO;
import com.softserve.entity.Lesson;
import com.softserve.entity.TemporarySchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TeacherMapper.class)
public interface LessonsInScheduleMapper {
    @Mapping(source = "lessonType", target = "lessonType")
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "temporaryScheduleDTO", ignore = true)
    LessonsInScheduleDTO lessonToLessonsInScheduleDTO(Lesson lesson);

    @Mapping(source = "semester.id", target = "semesterId")
    LessonInfoDTO lessonToLessonsInTemporaryScheduleDTO(Lesson lesson);

    @Mapping(source = "semester.id", target = "semesterId")
    @Mapping(target = "hours", ignore = true)
    LessonInfoDTO lessonToLessonsInTemporaryScheduleDTO(TemporarySchedule temporarySchedule);
}
