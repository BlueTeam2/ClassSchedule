package com.softserve.controller;

import com.softserve.dto.GroupDTO;
import com.softserve.dto.SemesterDTO;
import com.softserve.dto.SemesterWithGroupsDTO;
import com.softserve.entity.Group;
import com.softserve.entity.Semester;
import com.softserve.mapper.GroupMapper;
import com.softserve.mapper.SemesterMapper;
import com.softserve.service.GroupService;
import com.softserve.service.SemesterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Api(tags = "Semester API")
@Slf4j
public class SemesterController {

    private final SemesterService semesterService;
    private final SemesterMapper semesterMapper;
    private final GroupMapper groupMapper;
    private final GroupService groupService;

    @Autowired
    public SemesterController(SemesterService semesterService, SemesterMapper semesterMapper, GroupMapper groupMapper, GroupService groupService) {
        this.semesterService = semesterService;
        this.semesterMapper = semesterMapper;
        this.groupMapper = groupMapper;
        this.groupService = groupService;
    }

    @GetMapping(path = {"/semesters", "/public/semesters"})
    @ApiOperation(value = "Get the list of all semesters")
    public ResponseEntity<List<SemesterWithGroupsDTO>> list() {
        log.info("In list ()");
        List<Semester> semesters = semesterService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(semesterMapper.semestersToSemesterWithGroupsDTOs(semesters));
    }

    @GetMapping("/semesters/{id}")
    @ApiOperation(value = "Get semester info by id")
    public ResponseEntity<SemesterWithGroupsDTO> get(@PathVariable("id") long id) {
        log.info("In get(id = [{}])", id);
        Semester semester = semesterService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(semesterMapper.semesterToSemesterWithGroupsDTO(semester));
    }

    @GetMapping("/semesters/current")
    @ApiOperation(value = "Get current semester a manager is working on")
    public ResponseEntity<SemesterWithGroupsDTO> getCurrent() {
        log.info("In getCurrent()");
        Semester semester = semesterService.getCurrentSemester();
        return ResponseEntity.status(HttpStatus.OK).body(semesterMapper.semesterToSemesterWithGroupsDTO(semester));
    }

    @GetMapping("/semesters/default")
    @ApiOperation(value = "Get default semester")
    public ResponseEntity<SemesterWithGroupsDTO> getDefault() {
        log.info("In getDefault()");
        Semester semester = semesterService.getDefaultSemester();
        return ResponseEntity.status(HttpStatus.OK).body(semesterMapper.semesterToSemesterWithGroupsDTO(semester));
    }

    @PutMapping("/semesters/current")
    @ApiOperation(value = "Change current semester a manager is working on")
    public ResponseEntity<SemesterDTO> setCurrent(@RequestParam Long semesterId) {
        log.info("In setCurrent(semesterId = [{}])", semesterId);
        Semester semester = semesterService.changeCurrentSemester(semesterId);
        return ResponseEntity.status(HttpStatus.OK).body(semesterMapper.semesterToSemesterDTO(semester));
    }

    @PutMapping("/semesters/default")
    @ApiOperation(value = "Change default semester a manager is working on")
    public ResponseEntity<SemesterDTO> setDefault(@RequestParam Long semesterId) {
        log.info("In setDefault(semesterId = [{}])", semesterId);
        Semester semester = semesterService.changeDefaultSemester(semesterId);
        return ResponseEntity.status(HttpStatus.OK).body(semesterMapper.semesterToSemesterDTO(semester));
    }

    @PostMapping("/semesters")
    @ApiOperation(value = "Create new semester")
    public ResponseEntity<SemesterWithGroupsDTO> save(@RequestBody SemesterWithGroupsDTO semesterDTO) {
        log.info("In save (semesterDTO = [{}])", semesterDTO);
        Semester semester = semesterService.save(semesterMapper.semesterWithGroupsDTOToSemester(semesterDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(semesterMapper.semesterToSemesterWithGroupsDTO(semester));
    }

    @PutMapping("/semesters")
    @ApiOperation(value = "Update existing semester by id")
    public ResponseEntity<SemesterWithGroupsDTO> update(@RequestBody SemesterWithGroupsDTO semesterDTO) {
        log.info("In update (semesterDTO = [{}])", semesterDTO);
        semesterService.getById(semesterDTO.getId());
        Semester semester = semesterService.update(semesterMapper.semesterWithGroupsDTOToSemester(semesterDTO));
        return ResponseEntity.status(HttpStatus.OK).body(semesterMapper.semesterToSemesterWithGroupsDTO(semester));
    }

    @DeleteMapping("/semesters/{id}")
    @ApiOperation(value = "Delete semester by id")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        log.info("In delete (id =[{}]", id);
        Semester semester = semesterService.getById(id);
        semesterService.delete(semester);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/semesters/disabled")
    @ApiOperation(value = "Get the list of disabled semester")
    public ResponseEntity<List<SemesterDTO>> getDisabled() {
        log.info("Enter into getDisabled");
        return ResponseEntity.ok().body(semesterMapper.semestersToSemesterDTOs(semesterService.getDisabled()));
    }

    @PutMapping("/semesters/{semesterId}/groups")
    @ApiOperation(value = "Replace groups in semester by id")
    public ResponseEntity<SemesterWithGroupsDTO> addGroupsToSemester(@PathVariable Long semesterId, @RequestBody List<Long> groupId) {
        log.info("In addGroupsToSemester (semesterId = [{}], groupId = [{}])", semesterId, groupId);
        Semester semester = semesterService.addGroupsToSemester(semesterService.getById(semesterId), groupId);
        return ResponseEntity.status(HttpStatus.OK).body(semesterMapper.semesterToSemesterWithGroupsDTO(semester));
    }

    @GetMapping("/semesters/current/groups")
    @ApiOperation(value = "Get the list of all groups for current semester")
    public ResponseEntity<List<GroupDTO>> getGroupsForCurrentSemester() {
        log.info("In getGroupsForCurrentSemester");
        Set<Group> groups = groupService.getGroupsForCurrentSemester();
        return ResponseEntity.status(HttpStatus.OK).body(groupMapper.groupsToGroupDTOs(groups));
    }

    @GetMapping("/semesters/default/groups")
    @ApiOperation(value = "Get the list of all groups for default semester")
    public ResponseEntity<List<GroupDTO>> getGroupsForDefaultSemester() {
        log.info("In getGroupsForDefaultSemester");
        Set<Group> groups = groupService.getGroupsForDefaultSemester();
        return ResponseEntity.status(HttpStatus.OK).body(groupMapper.groupsToGroupDTOs(groups));
    }

    @GetMapping("/semesters/{semesterId}/groups")
    @ApiOperation(value = "Get the list of all groups for semester by id")
    public ResponseEntity<List<GroupDTO>> getGroupsBySemesterId(@PathVariable Long semesterId) {
        log.info("In getGroupsBySemesterId (semesterId =[{}]", semesterId);
        Set<Group> groups = groupService.getGroupsBySemesterId(semesterId);
        return ResponseEntity.status(HttpStatus.OK).body(groupMapper.groupsToGroupDTOs(groups));
    }

    @PostMapping("/semesters/copy-semester")
    @ApiOperation(value = "Copy full semester from one to another semester")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<SemesterWithGroupsDTO> copySemester(@RequestParam Long fromSemesterId,
                                                              @RequestParam Long toSemesterId) {
        log.debug("In copySemester with fromSemesterId = {} and toSemesterId = {}", fromSemesterId, toSemesterId);
        Semester semester = semesterService.copySemester(fromSemesterId, toSemesterId);
        return ResponseEntity.status(HttpStatus.OK).body(semesterMapper.semesterToSemesterWithGroupsDTO(semester));
    }
}
