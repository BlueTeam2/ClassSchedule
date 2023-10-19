package com.softserve.mapper;

import com.softserve.dto.RoomForScheduleDTO;
import com.softserve.entity.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomForScheduleMapper {

    RoomForScheduleDTO roomToRoomForScheduleDTO(Room room);
}
