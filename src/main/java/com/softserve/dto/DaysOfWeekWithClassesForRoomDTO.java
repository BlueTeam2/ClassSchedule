package com.softserve.dto;

import lombok.*;

import java.time.DayOfWeek;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class DaysOfWeekWithClassesForRoomDTO {
    private DayOfWeek day;
    private List<RoomClassesInScheduleDTO> classes;
}
