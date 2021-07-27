package com.softserve.controller;

import com.softserve.dto.TeacherDTO;
import com.softserve.dto.TeacherWishDTO;
import com.softserve.entity.Teacher;
import com.softserve.entity.User;
import com.softserve.entity.enums.Role;
import com.softserve.exception.MessageNotSendException;
import com.softserve.mapper.TeacherMapper;
import com.softserve.service.MailService;
import com.softserve.service.ScheduleService;
import com.softserve.service.TeacherService;
import com.softserve.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;


@RestController
@Api(tags = "Teacher API")
@Slf4j
public class TeacherController {

    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;
    private final UserService userService;
    private final ScheduleService scheduleService;

    @Autowired
    public TeacherController(TeacherService teacherService, TeacherMapper teacherMapper, UserService userService, ScheduleService scheduleService) {
        this.teacherService = teacherService;
        this.teacherMapper = teacherMapper;
        this.userService = userService;
        this.scheduleService = scheduleService;
    }

    @GetMapping(path = {"/teachers", "/public/teachers"})
    @ApiOperation(value = "Get the list of all teachers")
    public ResponseEntity<List<TeacherDTO>> getAll() {
        log.info("Enter into list method");
        return ResponseEntity.ok(teacherMapper.teachersToTeacherDTOs(teacherService.getAll()));
    }

    @GetMapping("/teachers/{id}")
    @ApiOperation(value = "Get teacher by id")
    public ResponseEntity<TeacherDTO> get(@PathVariable("id") Long id) {
        log.info("Enter into get method with id {} ", id);
        Teacher teacher = teacherService.getById(id);
        return ResponseEntity.ok().body(teacherMapper.teacherToTeacherDTO(teacher));
    }

    @GetMapping("/teachers/with-wishes")
    @ApiOperation(value = "Get the list of all teachers with wishes")
    public ResponseEntity<List<TeacherWishDTO>> getAllWithWishes() {
        log.info("Enter into getAllWithWishes method");
        return ResponseEntity.ok(teacherMapper.toTeacherWithWishesDTOs(teacherService.getAllTeachersWithWishes()));
    }

    @GetMapping("/teachers/{id}/with-wishes")
    @ApiOperation(value = "Get teacher with wish by id")
    public ResponseEntity<TeacherWishDTO> getTeacherWithWishes(@PathVariable("id") Long id) {
        log.info("Enter into getTeacherWithWishes method with id {} ", id);
        Teacher teacher = teacherService.getTeacherWithWishes(id);
        return ResponseEntity.ok().body(teacherMapper.toTeacherWithWishesDTOs(teacher));
    }

    @PostMapping("/teachers")
    @ApiOperation(value = "Create new teacher")
    public ResponseEntity<TeacherDTO> save(@RequestBody TeacherDTO teacherDTO) {
        log.info("Enter into save method with teacherDTO: {}", teacherDTO);
        Teacher teacher = teacherService.save(teacherMapper.teacherDTOToTeacher(teacherDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherMapper.teacherToTeacherDTO(teacher));
    }

    @PutMapping("/teachers")
    @ApiOperation(value = "Update existing teacher by id")
    public ResponseEntity<TeacherDTO> update(@RequestBody TeacherDTO updateTeacherDTO) {
        log.info("Enter into update method with updateTeacherDTO: {}",updateTeacherDTO);
        Teacher teacherToUpdate = teacherMapper.teacherDTOToTeacher(updateTeacherDTO);
        teacherToUpdate.setUserId(teacherService.getById(teacherToUpdate.getId()).getUserId());
        Teacher teacher = teacherService.update(teacherToUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(teacherMapper.teacherToTeacherDTO(teacher));
    }

    @DeleteMapping("/teachers/{id}")
    @ApiOperation(value = "Delete teacher by id")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        log.info("Enter into delete method with  teacher id: {}", id);
        Teacher teacher = teacherService.getById(id);
        if (teacher.getUserId() != null) {
            User user = userService.getById(teacher.getUserId().longValue());
            user.setRole(Role.ROLE_USER);
            userService.update(user);
        }
        teacherService.delete(teacher);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/teachers/disabled")
    @ApiOperation(value = "Get the list of disabled teachers")
    public ResponseEntity<List<TeacherDTO>> getDisabled() {
        log.info("Enter into getDisabled");
        return ResponseEntity.ok(teacherMapper.teachersToTeacherDTOs(teacherService.getDisabled()));
    }


    @GetMapping("/not-registered-teachers")
    @ApiOperation(value = "Get the list of all teachers, that don't registered in system")
    public ResponseEntity<List<TeacherDTO>> getAllNotRegisteredTeachers() {
        log.info("Enter into getAllNotRegisteredTeachers method");
        return ResponseEntity.ok(teacherMapper.teachersToTeacherDTOs(teacherService.getAllTeacherWithoutUser()));
    }

    @GetMapping("/send-pdf-to-email/semester/{id}")
    @ApiOperation(value = "Send pdf with schedule to teachers emails")
    public ResponseEntity sendSchedulesToEmail(@PathVariable("id") Long semesterId, @RequestParam Long[] teachersId) {
        log.info("Enter into sendPDFToEmail method with teachers id: {} and semester id: {}", teachersId, semesterId);
        try {
            scheduleService.sendScheduleToTeachers(semesterId, teachersId);
        } catch (MessagingException e) {
            log.error("Message was not sent");
            throw new MessageNotSendException(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}