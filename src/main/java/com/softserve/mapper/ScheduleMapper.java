package com.softserve.mapper;

import com.softserve.dto.ScheduleDTO;
import com.softserve.dto.ScheduleForCopyDTO;
import com.softserve.entity.Schedule;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {GroupMapper.class, RoomMapper.class, LessonInfoMapper.class})
public interface ScheduleMapper {
    ScheduleDTO scheduleToScheduleDTO(Schedule schedule);

    Schedule scheduleDTOToSchedule(ScheduleDTO scheduleDTO);

    ScheduleForCopyDTO scheduleToScheduleForCopyDTO(Schedule schedule);

    List<ScheduleDTO> scheduleToScheduleDTOs(List<Schedule> schedules);

    List<ScheduleForCopyDTO> scheduleToScheduleForCopyDTOs(List<Schedule> schedules);
}
