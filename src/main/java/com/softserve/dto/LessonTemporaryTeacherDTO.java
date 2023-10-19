package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LessonTemporaryTeacherDTO {
    private ScheduleTemporaryTeacherDateRangeDTO lesson;

    @JsonProperty(value = "temporary_schedule")
    private ScheduleTemporaryTeacherDateRangeDTO temporaryLesson;
}
