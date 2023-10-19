package com.softserve.controller;

import com.softserve.dto.SubjectDTO;
import com.softserve.dto.SubjectNameWithTypesDTO;
import com.softserve.entity.Subject;
import com.softserve.mapper.SubjectMapper;
import com.softserve.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "Subject API")
@RequestMapping("/subjects")
@Slf4j
public class SubjectController {

    private final SubjectService subjectService;
    private final SubjectMapper subjectMapper;

    @Autowired
    public SubjectController(SubjectService subjectService, SubjectMapper subjectMapper) {
        this.subjectService = subjectService;
        this.subjectMapper = subjectMapper;
    }

    @GetMapping
    @ApiOperation(value = "Get the list of all subjects")
    public ResponseEntity<List<SubjectDTO>> list() {
        log.info("In list ()");
        List<Subject> subjects = subjectService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(subjectMapper.subjectsToSubjectDTOs(subjects));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get subject info by id")
    public ResponseEntity<SubjectDTO> get(@PathVariable("id") long id) {
        log.info("In get(id = [{}])", id);
        Subject subject = subjectService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(subjectMapper.subjectToSubjectDTO(subject));
    }

    @PostMapping
    @ApiOperation(value = "Create new subject")
    public ResponseEntity<SubjectDTO> save(@RequestBody SubjectDTO subjectDTO) {
        log.info("In save (subjectDTO = [{}])", subjectDTO);
        Subject subject = subjectService.save(subjectMapper.subjectDTOToSubject(subjectDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectMapper.subjectToSubjectDTO(subject));
    }

    @PutMapping
    @ApiOperation(value = "Update existing subject by id")
    public ResponseEntity<SubjectDTO> update(@RequestBody SubjectDTO subjectDTO) {
        log.info("In update (subjectDTO = [{}])", subjectDTO);
        Subject subject = subjectService.update(subjectMapper.subjectDTOToSubject(subjectDTO));
        return ResponseEntity.status(HttpStatus.OK).body(subjectMapper.subjectToSubjectDTO(subject));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete subject by id")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        log.info("In delete (id =[{}]", id);
        Subject subject = subjectService.getById(id);
        subjectService.delete(subject);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/disabled")
    @ApiOperation(value = "Get the list of disabled subjects")
    public ResponseEntity<List<SubjectDTO>> getDisabled() {
        log.info("In list getDisabled");
        List<Subject> subjects = subjectService.getDisabled();
        return ResponseEntity.status(HttpStatus.OK).body(subjectMapper.subjectsToSubjectDTOs(subjects));
    }

    @GetMapping("/semester/{semesterId}/teacher/{teacherId}")
    @ApiOperation(value = "Get the list of subjects by teacher id and semester id")
    public ResponseEntity<List<SubjectNameWithTypesDTO>> getSubjectsWithTypes(@PathVariable("semesterId") Long semesterId,
                                                                              @PathVariable("teacherId") Long teacherId) {
        log.info("Enter into getSubjects method with semester id: {} and teacher id: {}", semesterId, teacherId);
        return ResponseEntity.ok().body(subjectMapper
                .subjectWithTypeDTOsToSubjectNameWithTypesDTOs(subjectService.getSubjectsWithTypes(semesterId, teacherId)));
    }
}
