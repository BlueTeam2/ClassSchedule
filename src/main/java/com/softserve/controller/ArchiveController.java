package com.softserve.controller;

import com.softserve.dto.*;
import com.softserve.entity.Schedule;
import com.softserve.entity.Semester;
import com.softserve.mapper.TemporaryScheduleMapperForArchive;
import com.softserve.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/archive")
@Api(tags = "Archive API")
@Slf4j
public class ArchiveController {

    private final ArchiveService archiveService;
    private final ScheduleService scheduleService;
    private final SemesterService semesterService;
    private final LessonService lessonService;
    private final TemporaryScheduleService temporaryScheduleService;
    private final TemporaryScheduleMapperForArchive temporaryScheduleMapper;

    @Autowired
    public ArchiveController(ArchiveService archiveService, ScheduleService scheduleService, SemesterService semesterService,
                             LessonService lessonService, TemporaryScheduleService temporaryScheduleService,
                             TemporaryScheduleMapperForArchive temporaryScheduleMapper) {
        this.archiveService = archiveService;
        this.scheduleService = scheduleService;
        this.semesterService = semesterService;
        this.lessonService = lessonService;
        this.temporaryScheduleService = temporaryScheduleService;
        this.temporaryScheduleMapper = temporaryScheduleMapper;
    }

    @PostMapping("/{semesterId}")
    @ApiOperation(value = "Save archive schedule by semesterId in mongo db")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Object> archiveScheduleBySemester(@PathVariable("semesterId") Long semesterId) {
        log.info("In archiveScheduleBySemester with semesterId = {}", semesterId);
        Semester semester = semesterService.getById(semesterId);
        List<Schedule> schedules = scheduleService.getSchedulesBySemester(semesterId);
        if (schedules.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageDTO(String.format("Schedules with semesterId = %d not found.", semesterId)));
        }
        ScheduleFullDTO scheduleFullDTO = scheduleService.getFullScheduleForSemester(semesterId);
        List<TemporaryScheduleForArchiveDTO> temporaryScheduleDTOs = temporaryScheduleMapper.convertToNewDtoList(
                temporaryScheduleService.getAllBySemesterId(semesterId));
        ScheduleFullForArchiveDTO scheduleForArchiveDTO = new ScheduleFullForArchiveDTO(true, scheduleFullDTO.getSemester(),
                scheduleFullDTO.getSchedule(), temporaryScheduleDTOs);
        temporaryScheduleService.deleteTemporarySchedulesBySemesterId(semesterId);
        scheduleService.deleteSchedulesBySemesterId(semesterId);
        lessonService.deleteLessonBySemesterId(semesterId);
        semesterService.delete(semester);
        return ResponseEntity.status(HttpStatus.OK).body(archiveService.saveScheduleForArchive(scheduleForArchiveDTO));
    }

    @GetMapping("/{semesterId}")
    @ApiOperation(value = "Get archive schedule by semesterId from mongo db")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ScheduleFullForArchiveDTO> getScheduleBySemesterId(@PathVariable("semesterId") Long semesterId) {
        log.info("In getScheduleBySemesterId with semesterId = {}", semesterId);
        return ResponseEntity.ok().body(archiveService.getArchiveScheduleBySemesterId(semesterId));
    }

    @GetMapping(value = "all-semesters")
    @ApiOperation(value = "Get all semesters from archived schedules, that contains it")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<SemesterDTO>> getAllSemestersInArchiveSchedule() {
        log.info("In getAllSemestersInArchiveSchedule ");
        return ResponseEntity.ok().body(archiveService.getAllSemestersInArchiveSchedule());
    }

    @DeleteMapping("/{semesterId}")
    @ApiOperation(value = "Delete archive schedule by semesterId from mongo db")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<MessageDTO> removeArchiveScheduleBySemesterId(@PathVariable("semesterId") Long semesterId) {
        log.info("In removeArchiveScheduleBySemesterId with semesterId = {}", semesterId);
        archiveService.deleteArchiveScheduleBySemesterId(semesterId);
        return ResponseEntity.ok().body(new MessageDTO("Object deleted successfully"));
    }
}
