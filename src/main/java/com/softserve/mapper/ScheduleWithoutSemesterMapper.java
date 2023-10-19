package com.softserve.mapper;

import com.softserve.dto.ScheduleWithoutSemesterDTO;
import com.softserve.entity.Schedule;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LessonInfoMapper.class, RoomMapper.class})
public interface ScheduleWithoutSemesterMapper {
    ScheduleWithoutSemesterDTO scheduleToScheduleWithoutSemesterDTO(Schedule schedule);

    Schedule scheduleWithoutSemesterDTOToSchedule(ScheduleWithoutSemesterDTO scheduleWithoutSemesterDTO);

    List<ScheduleWithoutSemesterDTO> scheduleToScheduleWithoutSemesterDTOs(List<Schedule> schedules);
}
