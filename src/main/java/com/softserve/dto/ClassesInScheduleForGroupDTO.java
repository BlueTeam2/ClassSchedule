package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ClassesInScheduleForGroupDTO implements Serializable {
    @JsonProperty("class")
    private PeriodDTO period;
    private LessonInScheduleByWeekDTO weeks;
}
