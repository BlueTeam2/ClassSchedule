package com.softserve.mapper;

import com.softserve.dto.LessonDTO;
import com.softserve.dto.LessonForGroupsDTO;
import com.softserve.dto.LessonInfoDTO;
import com.softserve.dto.LessonWithLinkDTO;
import com.softserve.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonInfoMapper {
    @Mapping(source = "semester.id", target = "semesterId")
    LessonInfoDTO lessonToLessonInfoDTO(Lesson lesson);

    @Mapping(target = "semester.id", source = "semesterId")
    @Mapping(target = "teacher.position", ignore = true)
    @Mapping(target = "teacher.department", ignore = true)
    @Mapping(target = "teacher.disable", ignore = true)
    @Mapping(target = "teacher.userId", ignore = true)
    @Mapping(target = "group.students", ignore = true)
    @Mapping(target = "group.sortOrder", ignore = true)
    Lesson lessonInfoDTOToLesson(LessonInfoDTO lessonInfoDTO);

    @Mapping(source = "semester.id", target = "semesterId")
    LessonDTO lessonToLessonDTO(Lesson lesson);

    @Mapping(target = "semester.groups", ignore = true)
    @Mapping(target = "grouped", ignore = true)
    @Mapping(target = "group.students", ignore = true)
    @Mapping(target = "group.sortOrder", ignore = true)
    @Mapping(target = "teacher.position", ignore = true)
    @Mapping(target = "teacher.department", ignore = true)
    @Mapping(target = "teacher.disable", ignore = true)
    @Mapping(target = "teacher.userId", ignore = true)
    Lesson lessonDTOToLesson(LessonDTO lessonDTO);

    default List<Lesson> lessonForGroupsDTOToLessons(LessonForGroupsDTO lessonForGroupsDTO) {
        if (lessonForGroupsDTO == null || lessonForGroupsDTO.getGroups() == null) {
            return new ArrayList<>();
        }
        List<Lesson> lessons = new ArrayList<>();
        lessonForGroupsDTO.getGroups().forEach(group -> {
            LessonInfoDTO lessonInfoDTO = lessonForGroupsDTOToLessonInfoDTO(lessonForGroupsDTO);
            lessonInfoDTO.setGroup(group);
            lessons.add(lessonInfoDTOToLesson(lessonInfoDTO));
        });
        return lessons;
    }

    List<LessonInfoDTO> lessonsToLessonInfoDTOs(List<Lesson> lessons);

    List<LessonDTO> lessonsToLessonDTOs(List<Lesson> lessons);

    @Mapping(target = "group", ignore = true)
    LessonInfoDTO lessonForGroupsDTOToLessonInfoDTO(LessonForGroupsDTO lessonForGroupsDTO);

    @Mapping(source = "semesterId", target = "semester.id")
    @Mapping(source = "teacherId", target = "teacher.id")
    @Mapping(source = "linkToMeeting", target = "linkToMeeting")
    @Mapping(source = "subjectId", target = "subject.id")
    @Mapping(source = "lessonType", target = "lessonType")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hours", ignore = true)
    @Mapping(target = "subjectForSite", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "grouped", ignore = true)
    Lesson lessonWithLinkDTOToLesson(LessonWithLinkDTO lessonWithLinkDTO);
}
