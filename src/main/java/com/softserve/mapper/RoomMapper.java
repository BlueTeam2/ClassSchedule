package com.softserve.mapper;

import com.softserve.dto.RoomDTO;
import com.softserve.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    @Mapping(target = "sortOrder", ignore = true)
    Room convertToEntity(RoomDTO dto);

    RoomDTO convertToDto(Room entity);

    List<RoomDTO> convertToDtoList(List<Room> rooms);
}
