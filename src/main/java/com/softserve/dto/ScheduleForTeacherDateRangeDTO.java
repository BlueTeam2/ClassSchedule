package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ScheduleForTeacherDateRangeDTO {
    @JsonProperty(value = "class")
    private PeriodDTO period;
    private List<LessonTeacherDTO> lessons;
}
