package com.softserve.dto;

import com.softserve.entity.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SchedulesInRoomDTO {
    private Room room;
    private List<SchedulesAtDayOfWeek> schedules;
}
