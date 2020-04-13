package com.softserve.service.mapper;


import com.softserve.dto.RoomTypeDTO;
import com.softserve.entity.RoomType;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface    RoomTypeMapper {
    RoomTypeDTO roomTypeToRoomTypeDTO(RoomType roomType);
    RoomType RoomTypeDTOTRoomType(RoomTypeDTO roomTypeDTO);

    List<RoomTypeDTO> roomTypesToRoomTypeDTOs(List<RoomType> roomTypes);
}
