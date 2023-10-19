package com.softserve.mapper;

import com.softserve.dto.ScheduleSaveDTO;
import com.softserve.entity.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScheduleSaveMapper {

    @Mapping(source = "room.id", target = "roomId")
    @Mapping(source = "period.id", target = "periodId")
    @Mapping(source = "dayOfWeek", target = "dayOfWeek")
    @Mapping(source = "evenOdd", target = "evenOdd")
    @Mapping(source = "lesson.id", target = "lessonId")
    ScheduleSaveDTO scheduleToScheduleSaveDTO(Schedule schedule);

    @Mapping(source = "roomId", target = "room.id")
    @Mapping(source = "periodId", target = "period.id")
    @Mapping(source = "dayOfWeek", target = "dayOfWeek")
    @Mapping(source = "evenOdd", target = "evenOdd")
    @Mapping(source = "lessonId", target = "lesson.id")
    Schedule scheduleSaveDTOToSchedule(ScheduleSaveDTO scheduleSaveDTO);

    List<Schedule> scheduleSaveDTOsListToSchedulesList(List<ScheduleSaveDTO> scheduleSaveDTOs);

    List<ScheduleSaveDTO> schedulesListToScheduleSaveDTOsList(List<Schedule> schedules);


}
