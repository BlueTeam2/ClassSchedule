package com.softserve.controller;

import com.softserve.dto.AddPeriodDTO;
import com.softserve.dto.MessageDTO;
import com.softserve.dto.PeriodDTO;
import com.softserve.entity.Period;
import com.softserve.mapper.PeriodMapper;
import com.softserve.service.PeriodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "Class API")
@Slf4j
public class PeriodController {

    private final PeriodService periodService;

    private final PeriodMapper periodMapper;

    @Autowired
    public PeriodController(PeriodService periodService, PeriodMapper periodMapper) {
        this.periodService = periodService;
        this.periodMapper = periodMapper;
    }

    @GetMapping(path = {"/classes", "/public/classes"})
    @ApiOperation(value = "Get the list of all classes")
    public ResponseEntity<List<PeriodDTO>> list() {
        log.info("Enter into list of PeriodController");
        return ResponseEntity.ok().body(periodMapper.convertToDtoList(periodService.getAll()));
    }

    @GetMapping("/classes/{id}")
    @ApiOperation(value = "Get class info by id")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PeriodDTO> get(@PathVariable("id") long id) {
        log.info("Enter into get of PeriodController with id {} ", id);
        Period period = periodService.getById(id);
        return ResponseEntity.ok().body(periodMapper.convertToDto(period));
    }

    @PostMapping("/classes")
    @ApiOperation(value = "Create new class")
    public ResponseEntity<PeriodDTO> save(@RequestBody AddPeriodDTO addPeriodDTO) {
        log.info("Enter into save of PeriodController with addPeriodDTO: {}", addPeriodDTO);
        Period newPeriod = periodService.save(periodMapper.convertToEntity(addPeriodDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(periodMapper.convertToDto(newPeriod));
    }

    @PostMapping("/classes/all")
    @ApiOperation(value = "Create a list of classes")
    public ResponseEntity<MessageDTO> save(@RequestBody List<AddPeriodDTO> periods) {
        log.info("Enter into save of PeriodController with List of addPeriodDTO: {}", periods);
        periodService.save(periodMapper.convertToEntityList(periods));
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDTO("New periods have been saved"));
    }

    @PutMapping("/classes")
    @ApiOperation(value = "Update existing class")
    public ResponseEntity<PeriodDTO> update(@RequestBody PeriodDTO periodDTO) {
        log.info("Enter into update of PeriodController with periodDTO: {}", periodDTO);
        Period newPeriod = periodService.update(periodMapper.convertToEntity(periodDTO));
        return ResponseEntity.ok().body(periodMapper.convertToDto(newPeriod));
    }

    @DeleteMapping("/classes/{id}")
    @ApiOperation(value = "Delete class by id")
    public ResponseEntity<MessageDTO> delete(@PathVariable("id") long id) {
        log.info("Enter into delete of PeriodController with id: {}", id);
        periodService.delete(periodService.getById(id));
        return ResponseEntity.ok().body(new MessageDTO("Period has been deleted successfully."));
    }
}
