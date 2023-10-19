package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class ScheduleForTemporaryTeacherDateRangeDTO {
    private ScheduleTemporaryTeacherDateRangeDTO schedule;

    @JsonProperty(value = "temporary_schedule")
    private ScheduleTemporaryTeacherDateRangeDTO temporarySchedule;
}
