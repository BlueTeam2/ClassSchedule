package com.softserve.controller;

import com.softserve.dto.DepartmentDTO;
import com.softserve.entity.Department;
import com.softserve.mapper.DepartmentMapper;
import com.softserve.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/departments")
@Api(tags = "Department API")
@Slf4j
public class DepartmentController {
    private final DepartmentService service;
    private final DepartmentMapper mapper;

    @GetMapping
    @ApiOperation(value = "Get the list of all departments")
    public ResponseEntity<List<DepartmentDTO>> getAll() {
        log.info("In getAll ()");
        List<Department> departments = service.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(mapper.departmentsToDepartmentDTOs(departments));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get department info by id")
    public ResponseEntity<DepartmentDTO> getById(@PathVariable("id") long id) {
        log.info("In getById (id = [{}])", id);
        Department department = service.getById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(mapper.departmentToDepartmentDTO(department));
    }

    @PostMapping
    @ApiOperation(value = "Create new department")
    public ResponseEntity<DepartmentDTO> save(@RequestBody DepartmentDTO departmentDTO) {
        log.info("In save (departmentDTO = [{}])", departmentDTO);
        Department department = service.save(mapper.departmentDTOToDepartment(departmentDTO));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.departmentToDepartmentDTO(department));
    }

    @PutMapping
    @ApiOperation(value = "Update existing department")
    public ResponseEntity<DepartmentDTO> update(@RequestBody DepartmentDTO departmentDTO) {
        log.info("In update (departmentDTO = [{}])", departmentDTO);
        Department department = service.update(mapper.departmentDTOToDepartment(departmentDTO));
        return ResponseEntity.status(HttpStatus.OK)
                .body(mapper.departmentToDepartmentDTO(department));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete department by id")
    public ResponseEntity<DepartmentDTO> deleteById(@PathVariable("id") long id) {
        log.info("In deleteById (id =[{}]", id);
        Department department = service.delete(service.getById(id));
        return ResponseEntity.status(HttpStatus.OK)
                .body(mapper.departmentToDepartmentDTO(department));
    }

    @GetMapping("/disabled")
    @ApiOperation(value = "Get the list of disabled departments")
    public ResponseEntity<List<DepartmentDTO>> getDisabled() {
        log.info("In getDisabled ()");
        List<Department> departments = service.getDisabled();
        return ResponseEntity.status(HttpStatus.OK)
                .body(mapper.departmentsToDepartmentDTOs(departments));
    }
}