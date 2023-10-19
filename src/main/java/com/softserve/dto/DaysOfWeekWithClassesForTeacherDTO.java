package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.DayOfWeek;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class DaysOfWeekWithClassesForTeacherDTO {
    private DayOfWeek day;
    @JsonProperty("even")
    private ClassesInScheduleForTeacherDTO evenWeek;
    @JsonProperty("odd")
    private ClassesInScheduleForTeacherDTO oddWeek;
}
