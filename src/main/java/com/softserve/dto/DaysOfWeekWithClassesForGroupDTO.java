package com.softserve.dto;

import lombok.*;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class DaysOfWeekWithClassesForGroupDTO implements Serializable {
    private DayOfWeek day;
    private List<ClassesInScheduleForGroupDTO> classes;
}
