package com.softserve.controller;

import com.softserve.dto.*;
import com.softserve.entity.*;
import com.softserve.entity.enums.EvenOdd;
import com.softserve.mapper.*;
import com.softserve.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "Schedule API")
@Slf4j
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final SemesterService semesterService;
    private final SemesterMapper semesterMapper;
    private final ScheduleMapper scheduleMapper;
    private final ScheduleSaveMapper scheduleSaveMapper;
    private final ScheduleWithoutSemesterMapper scheduleWithoutSemesterMapper;
    private final TeacherService teacherService;
    private final PeriodMapper periodMapper;
    private final RoomForScheduleMapper roomForScheduleMapper;
    private final LessonsInScheduleMapper lessonsInScheduleMapper;
    private final LessonService lessonService;
    private final RoomService roomService;
    private final ConverterToSchedulesInRoom converterToSchedulesInRoom;

    @Autowired
    public ScheduleController(ScheduleService scheduleService,
                              SemesterService semesterService,
                              SemesterMapper semesterMapper,
                              ScheduleMapper scheduleMapper,
                              ScheduleSaveMapper scheduleSaveMapper,
                              ScheduleWithoutSemesterMapper scheduleWithoutSemesterMapper,
                              TeacherService teacherService, PeriodMapper periodMapper,
                              RoomForScheduleMapper roomForScheduleMapper,
                              LessonService lessonService,
                              LessonsInScheduleMapper lessonsInScheduleMapper,
                              RoomService roomService,
                              ConverterToSchedulesInRoom converterToSchedulesInRoom) {
        this.scheduleService = scheduleService;
        this.semesterService = semesterService;
        this.semesterMapper = semesterMapper;
        this.scheduleMapper = scheduleMapper;
        this.scheduleSaveMapper = scheduleSaveMapper;
        this.scheduleWithoutSemesterMapper = scheduleWithoutSemesterMapper;
        this.teacherService = teacherService;
        this.periodMapper = periodMapper;
        this.roomForScheduleMapper = roomForScheduleMapper;
        this.lessonService = lessonService;
        this.lessonsInScheduleMapper = lessonsInScheduleMapper;
        this.roomService = roomService;
        this.converterToSchedulesInRoom = converterToSchedulesInRoom;
    }

    @GetMapping
    @ApiOperation(value = "Get the list of all schedules")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<ScheduleDTO>> list() {
        log.info("In list()");
        List<Schedule> schedules = scheduleService.getAll();

        return ResponseEntity.status(HttpStatus.OK).body(scheduleMapper.scheduleToScheduleDTOs(schedules));
    }

    @GetMapping("/semester")
    @ApiOperation(value = "Get the list of all schedules")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<ScheduleWithoutSemesterDTO>> listForSemester(@RequestParam Long semesterId) {
        log.info("In listForSemester()");
        List<Schedule> schedules = scheduleService.getSchedulesBySemester(semesterId);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleWithoutSemesterMapper.scheduleToScheduleWithoutSemesterDTOs(schedules));
    }

    @GetMapping("/data-before")
    @ApiOperation(value = "Get the info for finishing creating the schedule")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<CreateScheduleInfoDTO> getInfoForCreatingSchedule(@RequestParam Long semesterId,
                                                                            @RequestParam DayOfWeek dayOfWeek,
                                                                            @RequestParam EvenOdd evenOdd,
                                                                            @RequestParam Long classId,
                                                                            @RequestParam Long lessonId) {
        log.info("In getInfoForCreatingSchedule(semesterId = [{}], dayOfWeek = [{}], evenOdd = [{}], classId = [{}], lessonId = [{}])",
                semesterId, dayOfWeek, evenOdd, classId, lessonId);
        return ResponseEntity.status(HttpStatus.OK).body(
                scheduleService.getInfoForCreatingSchedule(semesterId, dayOfWeek, evenOdd, classId, lessonId));
    }

    @GetMapping("/full/groups")
    @ApiOperation(value = "Get full schedule for groupId in some semester")
    public ResponseEntity<ScheduleFullDTO> getFullScheduleForGroup(@RequestParam Long semesterId,
                                                                   @RequestParam Long groupId) {
        log.info("In, getFullScheduleForGroup (semesterId = [{}], groupId = [{}]) ", semesterId, groupId);
        ScheduleFullDTO scheduleFullDTO = new ScheduleFullDTO();
        scheduleFullDTO.setSemester(semesterMapper.semesterToSemesterDTO(semesterService.getById(semesterId)));
        scheduleFullDTO.setSchedule(scheduleService.getFullScheduleForGroup(semesterId, groupId));
        return ResponseEntity.status(HttpStatus.OK).body(scheduleFullDTO);
    }

    @GetMapping("/full/semester")
    @ApiOperation(value = "Get full schedule for semester")
    public ResponseEntity<ScheduleFullDTO> getFullScheduleForSemester(@RequestParam Long semesterId) {
        log.info("In, getFullScheduleForSemester (semesterId = [{}]) ", semesterId);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getFullScheduleForSemester(semesterId));
    }

    @GetMapping("/full/teachers")
    @ApiOperation(value = "Get full schedule for teacher by semester")
    public ResponseEntity<ScheduleForTeacherDTO> getFullScheduleForTeacher(@RequestParam Long semesterId,
                                                                           @RequestParam Long teacherId) {
        log.info("In, getFullScheduleForTeacher (semesterId = [{}], teacherId = [{}]) ", semesterId, teacherId);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getScheduleForTeacher(semesterId, teacherId));
    }

    @GetMapping("/full/rooms")
    @ApiOperation(value = "Get full schedule for semester. Returns schedule for  rooms")
    public ResponseEntity<List<ScheduleForRoomDTO>> getFullScheduleForRoom(@RequestParam Long semesterId) {
        log.info("In, getFullScheduleForRoom (semesterId = [{}]) ", semesterId);
        Semester semester = semesterService.getById(semesterId);
        List<Room> rooms = roomService.getAllOrdered();
        List<ScheduleForRoomDTO> scheduleForRoomDTOS =
                converterToSchedulesInRoom.getBySemester(rooms, semester,
                        scheduleService.getAllOrdered(semesterId));
        return ResponseEntity.status(HttpStatus.OK).body(scheduleForRoomDTOS);
    }


    @PostMapping
    @ApiOperation(value = "Create new schedules")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<ScheduleSaveDTO>> save(@RequestBody ScheduleSaveDTO scheduleSaveDTO) {
        log.info("In save(scheduleSaveDTO = [{}])", scheduleSaveDTO);
        Schedule schedule = scheduleSaveMapper.scheduleSaveDTOToSchedule(scheduleSaveDTO);
        schedule.setLesson(lessonService.getById(scheduleSaveDTO.getLessonId()));
        List<Schedule> schedules = new ArrayList<>();
        if (schedule.getLesson().isGrouped()) {
            schedules = scheduleService.schedulesForGroupedLessons(schedule);
            schedules.forEach(scheduleService::checkReferences);
            schedules.forEach(scheduleService::save);
        } else {
            schedules.add(scheduleService.save(schedule));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleSaveMapper.schedulesListToScheduleSaveDTOsList(schedules));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete schedule by id")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        log.info("In delete(id =[{}]", id);
        Schedule schedule = scheduleService.getById(id);
        if (schedule.getLesson().isGrouped()) {
            List<Schedule> schedules = scheduleService.getSchedulesForGroupedLessons(schedule);
            schedules.forEach(scheduleService::delete);
        } else {
            scheduleService.delete(schedule);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/delete-schedules")
    @ApiOperation(value = "Delete all schedules by semester id")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteSchedulesBySemesterId(@RequestParam Long semesterId) {
        log.info("In deleteSchedulesBySemesterId with semesterId = {}", semesterId);
        scheduleService.deleteSchedulesBySemesterId(semesterId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/by-room")
    @ApiOperation(value = "Change schedule by room Id")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ScheduleDTO> changeScheduleByRoom(@RequestParam Long scheduleId,
                                                            @RequestParam Long roomId) {
        log.info("In changeScheduleByRoom with scheduleId = {} and roomId = {}", scheduleId, roomId);
        Schedule schedule = scheduleService.getById(scheduleId);
        Room room = roomService.getById(roomId);
        if (schedule.getRoom().getId().equals(room.getId())) {
            return ResponseEntity.ok().body(scheduleMapper.scheduleToScheduleDTO(schedule));
        }
        schedule.setRoom(room);
        Schedule updateSchedule = scheduleService.updateWithoutChecks(schedule);
        return ResponseEntity.ok().body(scheduleMapper.scheduleToScheduleDTO(updateSchedule));
    }

}
