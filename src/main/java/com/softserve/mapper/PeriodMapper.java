package com.softserve.mapper;

import com.softserve.dto.PeriodDTO;
import com.softserve.dto.AddPeriodDTO;
import com.softserve.entity.Period;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PeriodMapper {

    Period convertToEntity(AddPeriodDTO addPeriodDTO);

    PeriodDTO convertToDto(Period entity);

    Period convertToEntity(PeriodDTO dto);

    List<PeriodDTO> convertToDtoList(List<Period> periods);

    List<Period> convertToEntityList(List<AddPeriodDTO> periods);
}
