package com.softserve.mapper;

import com.softserve.dto.TemporaryScheduleForArchiveDTO;
import com.softserve.entity.TemporarySchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;


@Mapper(componentModel = "spring", uses = TeacherMapper.class)
public interface TemporaryScheduleMapperForArchive {
    @Named("temporary_schedule_for_archive")
    List<TemporaryScheduleForArchiveDTO> convertToNewDtoList(List<TemporarySchedule> schedules);
}
