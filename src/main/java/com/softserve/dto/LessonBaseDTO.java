package com.softserve.dto;

import com.softserve.entity.enums.LessonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class LessonBaseDTO implements Serializable {
    private Long id;
    private int hours;
    private String linkToMeeting;
    private String subjectForSite;
    private LessonType lessonType;
    private SubjectDTO subject;
    private TeacherNameDTO teacher;
    private Long semesterId;
}
