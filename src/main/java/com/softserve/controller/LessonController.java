package com.softserve.controller;


import com.softserve.dto.*;
import com.softserve.entity.Lesson;
import com.softserve.entity.Semester;
import com.softserve.entity.enums.LessonType;
import com.softserve.mapper.GroupMapper;
import com.softserve.mapper.LessonInfoMapper;
import com.softserve.service.GroupService;
import com.softserve.service.LessonService;
import com.softserve.service.ScheduleService;
import com.softserve.service.SemesterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "Lesson API")
@Slf4j
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final LessonInfoMapper lessonInfoMapper;
    private final SemesterService semesterService;
    private final GroupService groupService;
    private final ScheduleService scheduleService;
    private final GroupMapper groupMapper;

    @Autowired
    public LessonController(LessonService lessonService, LessonInfoMapper lessonInfoMapper, SemesterService semesterService,
                            GroupService groupService, ScheduleService scheduleService, GroupMapper groupMapper) {
        this.lessonService = lessonService;
        this.lessonInfoMapper = lessonInfoMapper;
        this.semesterService = semesterService;
        this.groupService = groupService;
        this.scheduleService = scheduleService;
        this.groupMapper = groupMapper;
    }


    @ApiOperation(value = "Get list of all lessons")
    @GetMapping
    public ResponseEntity<List<LessonInfoDTO>> list(
            @RequestParam(required = false) @ApiParam(value = "Get all lessons for particular group") Long groupId
    ) {
        log.info("In list ()");
        List<Lesson> lessons = groupId == null ? lessonService.getAll() : lessonService.getAllForGroup(groupId);
        return ResponseEntity.status(HttpStatus.OK).body(lessonInfoMapper.lessonsToLessonInfoDTOs(lessons));

    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get lesson info by id")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LessonInfoDTO> get(@PathVariable("id") long id) {
        log.info("In get(id = [{}])", id);
        Lesson lesson = lessonService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(lessonInfoMapper.lessonToLessonInfoDTO(lesson));
    }

    @GetMapping("/teacher")
    @ApiOperation(value = "Get lessons info by teacher")
    public ResponseEntity<List<LessonInfoDTO>> getLessonByTeacher(@RequestParam long teacherId) {
        log.info("In getLessonByTeacher(teacherId = [{}])", teacherId);
        List<Lesson> lessons = lessonService.getLessonByTeacher(teacherId);
        return ResponseEntity.status(HttpStatus.OK).body(lessonInfoMapper.lessonsToLessonInfoDTOs(lessons));
    }

    @PostMapping
    @ApiOperation(value = "Create new lessons")
    //TODO Check test
    public ResponseEntity<List<LessonInfoDTO>> save(@RequestBody LessonForGroupsDTO lessonForGroupsDTO) {
        log.info("In save (lessonForGroupsDTO = [{}])", lessonForGroupsDTO);
        List<Lesson> lessons = lessonService.save(lessonInfoMapper.lessonForGroupsDTOToLessons(lessonForGroupsDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonInfoMapper.lessonsToLessonInfoDTOs(lessons));
    }

    @PutMapping
    @ApiOperation(value = "Update existing lesson")
    public ResponseEntity<LessonInfoDTO> update(@RequestBody LessonInfoDTO lessonInfoDTO) {
        log.info("In update (lessonInfoDTO = [{}])", lessonInfoDTO);
        Lesson updatedLesson = lessonService.update(lessonInfoMapper.lessonInfoDTOToLesson(lessonInfoDTO));
        return ResponseEntity.status(HttpStatus.OK).body(lessonInfoMapper.lessonToLessonInfoDTO(updatedLesson));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete lesson by id")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        log.info("In delete (id =[{}]", id);
        Lesson lesson = lessonService.getById(id);
        lessonService.delete(lesson);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/types")
    @ApiOperation(value = "Get all lesson types")
    public ResponseEntity<List<LessonType>> getLessonTypes() {
        log.info("In getLessonTypes()");
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.getAllLessonTypes());
    }

    @PostMapping("/copy-lessons")
    @ApiOperation(value = "Copy all lessons from one semester to another")
    public ResponseEntity<List<LessonDTO>> copyLessonsFromOneSemesterToAnother(@RequestParam Long fromSemesterId,
                                                                               @RequestParam Long toSemesterId) {
        log.info("In copyLessonsFromOneSemesterToAnother with fromSemesterId = {} and toSemesterId = {}", fromSemesterId, toSemesterId);
        List<Lesson> lessons = lessonService.getLessonsBySemester(fromSemesterId);
        Semester toSemester = semesterService.getById(toSemesterId);
        return ResponseEntity.ok().body(lessonInfoMapper.lessonsToLessonDTOs(
                lessonService.copyLessonsFromOneToAnotherSemester(lessons, toSemester)));
    }

    @PostMapping("/copy-lesson-for-groups")
    @ApiOperation(value = "Copy lesson for several groups")
    public ResponseEntity<List<LessonInfoDTO>> copyLessonInSameSemesterForGroups(@RequestParam Long lessonId,
                                                                                 @RequestBody List<Long> groupsId) {
        log.info("In copyLessonInSameSemesterForGroups with lessonId = {} and groupsId = {}", lessonId, groupsId);
        List<Lesson> lessonsToSave = new ArrayList<>();
        Lesson lesson = lessonService.getById(lessonId);
        for (long groupId : groupsId) {
            if (groupService.isExistsById(groupId)) {
                lesson.setGroup(groupService.getById(groupId));
                lessonService.saveLessonDuringCopy(lesson);
                lessonsToSave.add(lesson);
            }
        }
        return ResponseEntity.ok().body(lessonInfoMapper.lessonsToLessonInfoDTOs(lessonsToSave));
    }

    @GetMapping("/all-groups-with-their-lessons-for-grouped-lesson/{lessonId}")
    @ApiOperation(value = "Get all groups with their lessons for adding a group lesson to schedule")
    public ResponseEntity<List<GroupWithLessonIdDTO>> getAllGroupsWithLessonsForSetGroupedClass(@PathVariable("lessonId") Long lessonId) {
        log.info("In getAllGroupsWithLessonsForSetGroupedClass with lessonId = {}", lessonId);
        Lesson lesson = lessonService.getById(lessonId);
        List<Lesson> lessons = lessonService.getLessonsBySubjectIdTeacherIdSemesterIdLessonTypeAndExcludeCurrentLessonId(lesson);

        List<Lesson> groupedLessons = new ArrayList<>();
        for (Lesson les : lessons) {
            if (scheduleService.countInputLessonsInScheduleByLessonId(les.getId()) < Long.parseLong(String.valueOf(les.getHours()))) {
                groupedLessons.add(les);
            }
        }

        return ResponseEntity.ok().body(convertToGroupWithLessonIdDTO(groupedLessons));
    }

    private List<GroupWithLessonIdDTO> convertToGroupWithLessonIdDTO(List<Lesson> lessons) {
        List<GroupWithLessonIdDTO> groupWithLessonIdDTOs = new ArrayList<>();
        for (Lesson lesson : lessons) {
            GroupWithLessonIdDTO groupWithLessonIdDTO = new GroupWithLessonIdDTO();
            groupWithLessonIdDTO.setGroupDTO(groupMapper.groupToGroupDTO(lesson.getGroup()));
            groupWithLessonIdDTO.setLessonId(lesson.getId());
            groupWithLessonIdDTOs.add(groupWithLessonIdDTO);
        }
        return groupWithLessonIdDTOs;
    }

    @PutMapping("/link")
    @ApiOperation(value = "Update link to meeting")
    public ResponseEntity<Integer> updateLinkToMeeting(@RequestBody LessonWithLinkDTO lessonWithLinkDTO) {
        log.info("In updateLinkToMeeting (lessonWithLinkDTO = [{}])", lessonWithLinkDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(lessonService.updateLinkToMeeting(lessonInfoMapper.lessonWithLinkDTOToLesson(lessonWithLinkDTO)));
    }
}
