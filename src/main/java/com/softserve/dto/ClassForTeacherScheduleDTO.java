package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ClassForTeacherScheduleDTO implements Serializable {
    @JsonProperty("class")
    private PeriodDTO period;
    private List<LessonForTeacherScheduleDTO> lessons;
}
