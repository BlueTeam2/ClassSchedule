package com.softserve.mapper;

import com.softserve.dto.*;
import com.softserve.entity.*;
import com.softserve.entity.enums.EvenOdd;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ConverterToSchedulesInRoom {

    private final GroupMapper groupMapper;

    public ConverterToSchedulesInRoom(GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }

    private List<LessonsListInRoomScheduleDTO> getLessonsListInRoomScheduleDTOS(Map<Lesson, List<Schedule>> schedules) {
        List<LessonsListInRoomScheduleDTO> lessonsListInRoomScheduleDTOS = new ArrayList<>();
        for (var lessonSchedule : schedules.entrySet()) {
            LessonsListInRoomScheduleDTO lessonsListInRoomScheduleDTO = new LessonsListInRoomScheduleDTO();
            lessonsListInRoomScheduleDTO.setSubjectName(lessonSchedule.getKey().getSubjectForSite());
            lessonsListInRoomScheduleDTO.setLessonType(lessonSchedule.getKey().getLessonType());
            lessonsListInRoomScheduleDTO.setSurname(lessonSchedule.getKey().getTeacher().getSurname());
            lessonsListInRoomScheduleDTO.setGroups(groupMapper.toGroupDTOInRoomSchedule(
                    lessonSchedule.getValue().stream().map(s -> s.getLesson().getGroup())
                            .collect(Collectors.toList())
            ));
            lessonsListInRoomScheduleDTOS.add(lessonsListInRoomScheduleDTO);
        }
        return lessonsListInRoomScheduleDTOS;
    }

    public List<ScheduleForRoomDTO> getBySemester(List<Room> rooms, Semester semester,
                                                  Map<Room, List<Schedule>> roomSchedules) {
        List<ScheduleForRoomDTO> schedulesInRoomDTOS = new ArrayList<>();
        Set<Room> roomsScheduled = roomSchedules.keySet();
        for (var roomSchedule : rooms) {
            ScheduleForRoomDTO schedule = new ScheduleForRoomDTO();
            schedule.setRoomId(roomSchedule.getId());
            schedule.setRoomName(roomSchedule.getName());
            schedule.setRoomType(roomSchedule.getType().getDescription());
            if (roomsScheduled.contains(roomSchedule)) {
                schedule.setSchedules(
                        concatDaySchedules(semester, roomSchedules.get(roomSchedule).stream()
                                .collect(Collectors.groupingBy(Schedule::getDayOfWeek, LinkedHashMap::new, Collectors.toList()))
                        ));
            } else {
                schedule.setSchedules(getEmptyDays(semester));
            }
            schedulesInRoomDTOS.add(schedule);
        }
        return schedulesInRoomDTOS;
    }

    public List<DaysOfWeekWithClassesForRoomDTO> concatDaySchedules(Semester semester,
                                                                    Map<DayOfWeek, List<Schedule>> daySchedules) {
        List<DaysOfWeekWithClassesForRoomDTO> days = new ArrayList<>();
        Set<DayOfWeek> daysWithSchedules = daySchedules.keySet();
        Set<DayOfWeek> semesterDays = new TreeSet<>(semester.getDaysOfWeek());
        for (var day : semesterDays) {
            DaysOfWeekWithClassesForRoomDTO daysOfWeekWithClassesForRoomDTO = new DaysOfWeekWithClassesForRoomDTO();
            daysOfWeekWithClassesForRoomDTO.setDay(day);
            if (daysWithSchedules.contains(day)) {
                daysOfWeekWithClassesForRoomDTO.setClasses(concatRoomClassesInScheduleDTOS(semester,
                        daySchedules.get(day)));
            } else {
                daysOfWeekWithClassesForRoomDTO.setClasses(getEmptyRoomClassesInScheduleDTOS(semester));
            }
            days.add(daysOfWeekWithClassesForRoomDTO);
        }
        return days;
    }

    private List<RoomClassesInScheduleDTO> concatRoomClassesInScheduleDTOS(Semester semester,
                                                                           List<Schedule> schedules) {
        List<RoomClassesInScheduleDTO> roomClassesInScheduleDTOS = new ArrayList<>();
        RoomClassesInScheduleDTO roomClassesInScheduleDTO = new RoomClassesInScheduleDTO();
        Map<Boolean, List<Schedule>> evenOdd = schedules.stream()
                .collect(Collectors.partitioningBy(s -> s.getEvenOdd().equals(EvenOdd.EVEN)));
        roomClassesInScheduleDTO.setEven(concatLessonsInRoomScheduleDTOS(semester,
                evenOdd.get(Boolean.TRUE).stream().collect(Collectors.groupingBy(Schedule::getPeriod))));
        roomClassesInScheduleDTO.setOdd(concatLessonsInRoomScheduleDTOS(semester,
                evenOdd.get(Boolean.FALSE).stream().collect(Collectors.groupingBy(Schedule::getPeriod))));
        roomClassesInScheduleDTOS.add(roomClassesInScheduleDTO);
        return roomClassesInScheduleDTOS;
    }

    private List<LessonsInRoomScheduleDTO> concatLessonsInRoomScheduleDTOS(Semester semester,
                                                                           Map<Period, List<Schedule>> periodSchedules) {
        List<LessonsInRoomScheduleDTO> lessons = new ArrayList<>();
        Set<Period> periods = periodSchedules.keySet();
        for (var periodSchedule : semester.getPeriods()) {
            LessonsInRoomScheduleDTO lessonsInRoomScheduleDTO = new LessonsInRoomScheduleDTO();
            lessonsInRoomScheduleDTO.setClassId(periodSchedule.getId());
            lessonsInRoomScheduleDTO.setClassName(periodSchedule.getName());
            if (periods.contains(periodSchedule)) {
                lessonsInRoomScheduleDTO.setLessons(
                        getLessonsListInRoomScheduleDTOS(
                                periodSchedules.get(periodSchedule).stream()
                                        .collect(Collectors.groupingBy(Schedule::getLesson))
                        )
                );
            } else {
                lessonsInRoomScheduleDTO.setLessons(new ArrayList<>());
            }
            lessons.add(lessonsInRoomScheduleDTO);
        }
        return lessons;
    }

    private List<DaysOfWeekWithClassesForRoomDTO> getEmptyDays(Semester semester) {
        List<DaysOfWeekWithClassesForRoomDTO> days = new ArrayList<>();
        for (var daySchedule : semester.getDaysOfWeek()) {
            DaysOfWeekWithClassesForRoomDTO daysOfWeekWithClassesForRoomDTO = new DaysOfWeekWithClassesForRoomDTO();
            daysOfWeekWithClassesForRoomDTO.setDay(daySchedule);
            daysOfWeekWithClassesForRoomDTO.setClasses(getEmptyRoomClassesInScheduleDTOS(semester));
            days.add(daysOfWeekWithClassesForRoomDTO);
        }
        return days;
    }

    private List<RoomClassesInScheduleDTO> getEmptyRoomClassesInScheduleDTOS(Semester semester) {
        List<RoomClassesInScheduleDTO> roomClassesInScheduleDTOS = new ArrayList<>();
        RoomClassesInScheduleDTO roomClassesInScheduleDTO = new RoomClassesInScheduleDTO();
        roomClassesInScheduleDTO.setEven(getEmptyLessonsInRoomScheduleDTOS(semester));
        roomClassesInScheduleDTO.setOdd(getEmptyLessonsInRoomScheduleDTOS(semester));
        roomClassesInScheduleDTOS.add(roomClassesInScheduleDTO);
        return roomClassesInScheduleDTOS;
    }

    private List<LessonsInRoomScheduleDTO> getEmptyLessonsInRoomScheduleDTOS(Semester semester) {
        List<LessonsInRoomScheduleDTO> lessons = new ArrayList<>();
        for (var periodSchedule : semester.getPeriods()) {
            LessonsInRoomScheduleDTO lessonsInRoomScheduleDTO = new LessonsInRoomScheduleDTO();
            lessonsInRoomScheduleDTO.setClassId(periodSchedule.getId());
            lessonsInRoomScheduleDTO.setClassName(periodSchedule.getName());
            lessonsInRoomScheduleDTO.setLessons(new ArrayList<>());
            lessons.add(lessonsInRoomScheduleDTO);
        }
        return lessons;
    }
}
