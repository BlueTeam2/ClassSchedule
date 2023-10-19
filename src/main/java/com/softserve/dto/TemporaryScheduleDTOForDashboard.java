package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softserve.entity.*;
import com.softserve.entity.enums.LessonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class TemporaryScheduleDTOForDashboard {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;

    private String linkToMeeting;
    private String subjectForSite;
    private Teacher teacher;
    private LessonType lessonType;
    private Subject subject;
    private Group group;
    private Room room;
    @JsonProperty("class")
    private Period period;
    private boolean grouped;
    private boolean vacation;
}
