package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softserve.entity.enums.LessonType;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class LessonForTeacherScheduleDTO implements Serializable {
    //@EqualsAndHashCode.Exclude
    private Long id;
    private String linkToMeeting;
    private String subjectForSite;
    private LessonType lessonType;
    // @EqualsAndHashCode.Exclude
    private GroupDTO group;
    private String room;
    @JsonProperty("temporary_schedule")
    private TemporaryScheduleDTOForDashboard temporaryScheduleDTO;
}
