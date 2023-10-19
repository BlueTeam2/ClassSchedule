package com.softserve.controller;

import com.softserve.dto.*;
import com.softserve.entity.CurrentUser;
import com.softserve.entity.Teacher;
import com.softserve.entity.TemporarySchedule;
import com.softserve.mapper.TemporaryScheduleMapper;
import com.softserve.security.jwt.JwtUser;
import com.softserve.service.TeacherService;
import com.softserve.service.TemporaryScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Api(tags = "Temporary Schedule API")
@RequestMapping("/temporary-schedules")
@Slf4j
public class TemporaryScheduleController {

    private final TemporaryScheduleService temporaryScheduleService;
    private final TemporaryScheduleMapper temporaryScheduleMapper;
    private final TeacherService teacherService;

    @Autowired
    public TemporaryScheduleController(TemporaryScheduleService temporaryScheduleService,
                                       TemporaryScheduleMapper temporaryScheduleMapper, TeacherService teacherService) {
        this.temporaryScheduleService = temporaryScheduleService;
        this.temporaryScheduleMapper = temporaryScheduleMapper;
        this.teacherService = teacherService;
    }

    @PostMapping
    @ApiOperation(value = "Create new temporary schedule")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<TemporaryScheduleDTO> save(@RequestBody TemporaryScheduleSaveDTO temporaryScheduleDTO) {
        log.info("Enter into save of TemporaryScheduleController with temporaryScheduleDTO: {}", temporaryScheduleDTO);
        TemporarySchedule temporarySchedule = temporaryScheduleService.save(temporaryScheduleMapper.convertToEntity(temporaryScheduleDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(temporaryScheduleMapper.convertToDto(temporarySchedule));
    }

    @PostMapping("/add-range")
    @ApiOperation(value = "Create new temporary schedules by date range")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<TemporaryAddedMessagesDTO>> addByRange(@RequestBody TemporaryScheduleSaveRangeDTO temporaryScheduleSaveRangeDTO) {
        log.info("Enter into addByRange of TemporaryScheduleController with temporaryScheduleDTO: {}", temporaryScheduleSaveRangeDTO);
        LocalDate to = temporaryScheduleSaveRangeDTO.getTo();
        LocalDate from = temporaryScheduleSaveRangeDTO.getFrom();
        List<String> temporaryAddedMessagesDTOS = temporaryScheduleService
                .addRange(from, to, temporaryScheduleMapper.convertToEntity(temporaryScheduleSaveRangeDTO.getTemporarySchedule()));

        List<TemporaryAddedMessagesDTO> temporaryAddedMessagesDTOList = new ArrayList<>();
        for (String message : temporaryAddedMessagesDTOS) {
            TemporaryAddedMessagesDTO temporaryAddedMessagesDTO = new TemporaryAddedMessagesDTO(message);
            temporaryAddedMessagesDTOList.add(temporaryAddedMessagesDTO);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(temporaryAddedMessagesDTOList);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get temporary schedule by id")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<TemporaryScheduleDTO> getById(@PathVariable("id") long id) {
        log.info("Enter into getById of TemporaryScheduleController");
        return ResponseEntity.ok().body(temporaryScheduleMapper.convertToDto(temporaryScheduleService.getById(id)));
    }

    @PutMapping
    @ApiOperation(value = "Update existing temporary schedule by id")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<TemporaryScheduleDTO> update(@RequestBody TemporaryScheduleSaveDTO temporaryScheduleDTO) {
        log.info("Enter into update method with temporaryScheduleDTO: {}", temporaryScheduleDTO);
        TemporarySchedule temporarySchedule = temporaryScheduleService.update(temporaryScheduleMapper.convertToEntity(temporaryScheduleDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(temporaryScheduleMapper.convertToDto(temporarySchedule));
    }


    @GetMapping
    @ApiOperation(value = "Get all temporary schedules")
    //@PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<TemporaryScheduleDTO>> getAll(@RequestParam("teacherId") Optional<Long> teacherId,
                                                             @RequestParam Optional<String> from,
                                                             @RequestParam Optional<String> to) {
        log.info("Enter into getAll of TemporaryScheduleController");
        List<TemporarySchedule> temporaryScheduleList;
        if (from.isPresent() && to.isPresent()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter currentFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fromDate = LocalDate.parse(LocalDate.parse(from.get(), formatter).toString(), currentFormatter);
            LocalDate toDate = LocalDate.parse(LocalDate.parse(to.get(), formatter).toString(), currentFormatter);
            if (teacherId.isPresent()) {
                teacherService.getById(teacherId.get());
                temporaryScheduleList = temporaryScheduleService.getAllByTeacherAndRange(fromDate, toDate, teacherId.get());
            } else {
                temporaryScheduleList = temporaryScheduleService.getAllByRange(fromDate, toDate);
            }
        } else {
            temporaryScheduleList = temporaryScheduleService.getAllByCurrentSemester();
        }
        return ResponseEntity.ok().body(temporaryScheduleMapper.convertToDtoList(temporaryScheduleList));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete temporary schedule by id")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<MessageDTO> delete(@PathVariable("id") long id) {
        log.info("Enter into delete of TemporaryScheduleController with id: {}", id);
        temporaryScheduleService.delete(temporaryScheduleService.getById(id));
        return ResponseEntity.ok().body(new MessageDTO("Temporary schedule has been deleted successfully."));
    }

    @GetMapping("/teacher")
    @ApiOperation(value = "Get temporary schedules for current teacher")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<TemporaryScheduleDTO>> getTemporarySchedulesForCurrentTeacher(@CurrentUser JwtUser jwtUser,
                                                                                             @RequestParam String from,
                                                                                             @RequestParam String to) {
        log.info("In getTemporarySchedulesForCurrentTeacher");
        Teacher teacher = teacherService.findByUserId(jwtUser.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter currentFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fromDate = LocalDate.parse(LocalDate.parse(from, formatter).toString(), currentFormatter);
        LocalDate toDate = LocalDate.parse(LocalDate.parse(to, formatter).toString(), currentFormatter);
        return ResponseEntity.ok().body(temporaryScheduleMapper.convertToDtoList(temporaryScheduleService
                .getTemporaryScheduleByTeacherAndRange(fromDate, toDate, teacher.getId())));
    }


    /*private List<TemporaryScheduleDTO> convertToGroupedTemporarySchedule(List<TemporarySchedule> temporarySchedules){
    List<TemporaryScheduleDTO> temporaryScheduleDTOS = new ArrayList<>();
        Map<LocalDate, Map<Period, Map<Room, Map<String, Map<String, List<TemporarySchedule>>>>>> groupedTemporarySchedules =
        temporarySchedules.stream()
                .collect(Collectors.groupingBy(TemporarySchedule::getDate,
                        Collectors.groupingBy(TemporarySchedule::getPeriod,
                                Collectors.groupingBy(TemporarySchedule::getRoom,
                                Collectors.groupingBy(TemporarySchedule::getTeacherForSite,
                                        Collectors.groupingBy(TemporarySchedule::getSubjectForSite))))));


        for (Map.Entry<LocalDate, Map<Period, Map<Room, Map<String, Map<String, List<TemporarySchedule>>>>>> dateMapEntry :
        groupedTemporarySchedules.entrySet()) {
            for (Map.Entry<Period,Map<Room, Map<String, Map<String, List<TemporarySchedule>>>>> periodMapEntry : dateMapEntry.getValue().entrySet()) {
            for (Map.Entry<Room, Map<String, Map<String, List<TemporarySchedule>>>> roomMapEntry : periodMapEntry.getValue().entrySet()) {
                for (Map.Entry<String, Map<String, List<TemporarySchedule>>> teacherForSiteMap : roomMapEntry.getValue().entrySet()) {
                    for (Map.Entry<String, List<TemporarySchedule>> subjectForSiteMap : teacherForSiteMap.getValue().entrySet()) {
                        TemporaryScheduleDTO temporaryScheduleDTO = new TemporaryScheduleDTO();
                        temporaryScheduleDTO.setDate(dateMapEntry.getKey());
                        temporaryScheduleDTO.setRoom(roomMapEntry.getKey());
                        temporaryScheduleDTO.setGroups(temporarySchedules.stream().map(TemporarySchedule::getGroup).collect(Collectors.toList()));
                        temporaryScheduleDTOS.add(temporaryScheduleDTO);
                    }
                }
                    }
                }
            }
    return temporaryScheduleDTOS;
    }*/
}
