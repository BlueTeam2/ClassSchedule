package com.softserve.mapper;

import com.softserve.dto.RoomForScheduleInfoDTO;
import com.softserve.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomForScheduleInfoMapper {
    @Mapping(target = "available", ignore = true)
    RoomForScheduleInfoDTO roomToRoomForScheduleInfoDTO(Room room);

    List<RoomForScheduleInfoDTO> toRoomForScheduleDTOList(List<Room> room);
}
