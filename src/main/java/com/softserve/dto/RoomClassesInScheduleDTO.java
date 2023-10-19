package com.softserve.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class RoomClassesInScheduleDTO {
    private List<LessonsInRoomScheduleDTO> even;
    private List<LessonsInRoomScheduleDTO> odd;
}
