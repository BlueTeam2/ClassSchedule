package com.softserve.dto;

import com.softserve.entity.enums.EvenOdd;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.DayOfWeek;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ScheduleDTO {
    private Long id;
    private DayOfWeek dayOfWeek;
    private EvenOdd evenOdd;
    private LessonInfoDTO lesson;
    private PeriodDTO period;
    private RoomDTO room;

}
