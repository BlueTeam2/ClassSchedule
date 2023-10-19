package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softserve.entity.enums.LessonType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TemporaryScheduleForArchiveDTO {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;
    private String linkToMeeting;
    private String subjectForSite;
    private TeacherDTO teacher;
    private LessonType lessonType;
    private SubjectDTO subject;
    private GroupDTO group;
    private RoomDTO room;
    private SemesterDTO semester;
    @JsonProperty("class")
    private PeriodDTO period;
    private boolean grouped;
    private boolean vacation;
}
