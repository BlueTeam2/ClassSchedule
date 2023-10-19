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
public class ScheduleTemporaryTeacherDateRangeDTO {
    private Long id;
    private RoomForScheduleDTO room;
    @JsonProperty(value = "class")
    private PeriodDTO period;
    private LessonInfoDTO lesson;
    private boolean vacation;

}
