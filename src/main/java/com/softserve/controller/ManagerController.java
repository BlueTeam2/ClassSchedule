package com.softserve.controller;

import com.softserve.dto.JoinTeacherWithUserDTO;
import com.softserve.dto.TeacherWithUserDTO;
import com.softserve.mapper.TeacherMapper;
import com.softserve.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/managers")
@Api(tags = "Manager API")
@Slf4j
public class ManagerController {

    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;

    @Autowired
    public ManagerController(TeacherService teacherService, TeacherMapper teacherMapper) {
        this.teacherService = teacherService;
        this.teacherMapper = teacherMapper;
    }

    @PutMapping("/teacher_credentials")
    @ApiOperation(value = "Update Teacher without credentials and join with User")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TeacherWithUserDTO> joinTeacherWithUser(@RequestBody JoinTeacherWithUserDTO joinTeacherWithUserDTO) {
        log.info("Enter into joinTeacherWithUser method with teacherId {} and userId {}",
                joinTeacherWithUserDTO.getTeacherId(), joinTeacherWithUserDTO.getUserId());
        return ResponseEntity.ok().body(teacherMapper.toTeacherWithUserDTO(
                teacherService.joinTeacherWithUser(joinTeacherWithUserDTO.getTeacherId(), joinTeacherWithUserDTO.getUserId())));
    }
}
