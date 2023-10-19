package com.softserve.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class LessonWithLinkDTO implements Serializable {
    private Long semesterId;
    private Long teacherId;
    private String linkToMeeting;
    private Long subjectId;
    private String lessonType;

}
