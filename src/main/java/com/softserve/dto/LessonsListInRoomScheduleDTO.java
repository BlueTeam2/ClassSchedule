package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softserve.entity.enums.LessonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LessonsListInRoomScheduleDTO implements Serializable {

    @JsonProperty("subject_for_site")
    private String subjectName;

    @JsonProperty("lesson_type")
    private LessonType lessonType;
    @JsonProperty("teacher_for_site")
    private String surname;
    private List<GroupDTOInRoomSchedule> groups;
}
