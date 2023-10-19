package com.softserve.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ScheduleForTeacherDTO {
    private SemesterDTO semester;
    private TeacherDTO teacher;
    private List<DaysOfWeekWithClassesForTeacherDTO> days;
}
