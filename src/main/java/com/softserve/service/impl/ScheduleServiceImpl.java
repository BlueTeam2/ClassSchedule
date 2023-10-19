package com.softserve.service.impl;


import com.softserve.dto.*;
import com.softserve.entity.*;
import com.softserve.entity.enums.EvenOdd;
import com.softserve.exception.EntityAlreadyExistsException;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.exception.MessageNotSendException;
import com.softserve.exception.ScheduleConflictException;
import com.softserve.mapper.*;
import com.softserve.repository.ScheduleRepository;
import com.softserve.service.*;
import com.softserve.util.PdfReportGenerator;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final LessonService lessonService;
    private final RoomService roomService;
    private final GroupService groupService;
    private final TeacherService teacherService;
    private final SemesterService semesterService;
    private final UserService userService;
    private final MailService mailService;

    private final GroupMapper groupMapper;
    private final PeriodMapper periodMapper;
    private final LessonsInScheduleMapper lessonsInScheduleMapper;
    private final RoomForScheduleMapper roomForScheduleMapper;
    private final TeacherMapper teacherMapper;
    private final LessonForTeacherScheduleMapper lessonForTeacherScheduleMapper;


    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, LessonService lessonService, RoomService roomService,
                               GroupService groupService, TeacherService teacherService, SemesterService semesterService,
                               UserService userService, MailService mailService, GroupMapper groupMapper, PeriodMapper periodMapper,
                               LessonsInScheduleMapper lessonsInScheduleMapper, RoomForScheduleMapper roomForScheduleMapper,
                               TeacherMapper teacherMapper, LessonForTeacherScheduleMapper lessonForTeacherScheduleMapper) {
        this.scheduleRepository = scheduleRepository;
        this.lessonService = lessonService;
        this.roomService = roomService;
        this.groupService = groupService;
        this.teacherService = teacherService;
        this.semesterService = semesterService;
        this.userService = userService;
        this.mailService = mailService;
        this.groupMapper = groupMapper;
        this.periodMapper = periodMapper;
        this.lessonsInScheduleMapper = lessonsInScheduleMapper;
        this.roomForScheduleMapper = roomForScheduleMapper;
        this.teacherMapper = teacherMapper;
        this.lessonForTeacherScheduleMapper = lessonForTeacherScheduleMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schedule getById(Long id) {
        log.info("In getById(id = [{}])", id);
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(Schedule.class, "id", id.toString()));
        Hibernate.initialize(schedule.getLesson().getSemester().getDaysOfWeek());
        Hibernate.initialize(schedule.getLesson().getSemester().getPeriods());
        Hibernate.initialize(schedule.getLesson().getSemester().getGroups());
        return schedule;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    //@Cacheable("scheduleList")
    public List<Schedule> getAll() {
        log.info("In getAll()");
        List<Schedule> schedules = scheduleRepository.getAll();
        for (Schedule schedule : schedules) {
            Hibernate.initialize(schedule.getLesson().getSemester().getDaysOfWeek());
            Hibernate.initialize(schedule.getLesson().getSemester().getPeriods());
            Hibernate.initialize(schedule.getLesson().getSemester().getGroups());
        }
        return schedules;
    }

    /**
     * {@inheritDoc}
     *
     * @throws ScheduleConflictException if schedule for group already exists
     */
    @Override
    @CacheEvict(value = "scheduleList", allEntries = true)
    public Schedule save(Schedule schedule) {
        log.info("In save(entity = [{}]", schedule);
        if (isConflictForGroupInSchedule(schedule.getLesson().getSemester().getId(), schedule.getDayOfWeek(), schedule.getEvenOdd(),
                schedule.getPeriod().getId(), schedule.getLesson().getId())) {
            log.error("Schedule for group with id [{}] has conflict with already existing", schedule.getLesson().getGroup().getId());
            throw new ScheduleConflictException("You can't create schedule item for this group, because one already exists");
        } else {
            return scheduleRepository.save(schedule);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Schedule> schedulesForGroupedLessons(Schedule schedule) {
        log.info("In schedulesForGroupedLessons(schedule = [{}]", schedule);
        List<Schedule> schedules = new ArrayList<>();
        List<Lesson> lessons = lessonService.getAllGroupedLessonsByLesson(schedule.getLesson());
        lessons.forEach(lesson -> {
            Schedule newSchedule = new Schedule();
            newSchedule.setRoom(schedule.getRoom());
            newSchedule.setDayOfWeek(schedule.getDayOfWeek());
            newSchedule.setPeriod(schedule.getPeriod());
            newSchedule.setEvenOdd(schedule.getEvenOdd());
            newSchedule.setLesson(lesson);
            schedules.add(newSchedule);
        });
        return schedules;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Schedule> getSchedulesForGroupedLessons(Schedule schedule) {
        log.info("In getSchedulesForGroupedLessons(schedule = [{}]", schedule);
        List<Schedule> schedules = new ArrayList<>();
        schedulesForGroupedLessons(schedule).forEach(schedule1 ->
                schedules.add(scheduleRepository.getScheduleByObject(schedule1))
        );
        return schedules;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkReferences(Schedule schedule) {
        if (isLessonInScheduleByLessonIdPeriodIdEvenOddDayOfWeek(schedule.getLesson().getId(), schedule.getPeriod().getId(),
                schedule.getEvenOdd(), schedule.getDayOfWeek())) {
            log.error("Lessons with group title [{}] already exists in schedule", schedule.getLesson().getGroup().getTitle());
            throw new EntityAlreadyExistsException("Lessons with this group title already exists");
        }
        if (isConflictForGroupInSchedule(schedule.getLesson().getSemester().getId(), schedule.getDayOfWeek(), schedule.getEvenOdd(),
                schedule.getPeriod().getId(), schedule.getLesson().getId())) {
            log.error("Schedule for group with id [{}] has conflict with already existing", schedule.getLesson().getGroup().getId());
            throw new ScheduleConflictException("You can't create schedule item for this group, because one already exists");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ScheduleConflictException if schedule it violates already existing schedule
     */
    @Override
    @CacheEvict(value = "scheduleList", allEntries = true)
    public Schedule update(Schedule object) {
        log.info("In update(entity = [{}]", object);
        if (isConflictForGroupInSchedule(object.getLesson().getSemester().getId(), object.getDayOfWeek(), object.getEvenOdd(),
                object.getPeriod().getId(), object.getLesson().getId())) {
            throw new ScheduleConflictException("You can't update schedule item for this group, because it violates already existing");
        } else {
            return scheduleRepository.update(object);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(value = "scheduleList", allEntries = true)
    public Schedule delete(Schedule object) {
        return scheduleRepository.delete(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CreateScheduleInfoDTO getInfoForCreatingSchedule(Long semesterId, DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId, Long lessonId) {
        log.info("In getInfoForCreatingSchedule (semesterId = [{}], dayOfWeek = [{}], evenOdd = [{}], classId = [{}], lessonId = [{}])",
                semesterId, dayOfWeek, evenOdd, classId, lessonId);
        //checking for missing parameters and wrong types is skipped, because it handles automatically by GlobalExceptionHandler
        if (isConflictForGroupInSchedule(semesterId, dayOfWeek, evenOdd, classId, lessonId)) {
            log.error("Schedule for group already exists");
            throw new ScheduleConflictException("You can't create schedule for this group, because one already exists");
        } else {
            CreateScheduleInfoDTO createScheduleInfoDTO = new CreateScheduleInfoDTO();
            createScheduleInfoDTO.setTeacherAvailable(isTeacherAvailableForSchedule(semesterId, dayOfWeek, evenOdd, classId, lessonId));
            createScheduleInfoDTO.setRooms(roomService.getAllRoomsForCreatingSchedule(semesterId, dayOfWeek, evenOdd, classId));
            return createScheduleInfoDTO;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConflictForGroupInSchedule(Long semesterId, DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId, Long lessonId) {
        log.info("In isConflictForGroupInSchedule(semesterId = [{}], dayOfWeek = [{}], evenOdd = [{}], classId = [{}], lessonId = [{}])",
                semesterId, dayOfWeek, evenOdd, classId, lessonId);
        //Get group ID from Lesson by lesson ID to search further by group ID
        Long groupId = lessonService.getById(lessonId).getGroup().getId();
        //If Repository doesn't count any records that means there are no conflicts for this group at that point of time
        return scheduleRepository.conflictForGroupInSchedule(semesterId, dayOfWeek, evenOdd, classId, groupId) != 0;
    }

    /**
     * Checks if teacher already has another schedule at some semester (by semester id) at some day for some period(by classId).
     *
     * @param semesterId the id of the semester
     * @param dayOfWeek  the day of the week
     * @param evenOdd    the type of the week
     * @param classId    the id of the class
     * @param lessonId   the id of the lesson
     * @return {@code true} if teacher has schedule at some semester at some day(by even odd week) for some period
     */
    private boolean isTeacherAvailableForSchedule(Long semesterId, DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId, Long lessonId) {
        log.info("In isTeacherAvailable (semesterId = [{}], dayOfWeek = [{}], evenOdd = [{}], classId = [{}], lessonId = [{}]",
                semesterId, dayOfWeek, evenOdd, classId, lessonId);
        //Get teacher ID from Lesson by lesson ID to search further by teacher ID
        Long teacherId = lessonService.getById(lessonId).getTeacher().getId();
        return scheduleRepository.conflictForTeacherInSchedule(semesterId, dayOfWeek, evenOdd, classId, teacherId) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScheduleForGroupDTO> getFullScheduleForGroup(Long semesterId, Long groupId) {
        log.info("In getFullSchedule(semesterId = [{}], groupId[{}])", semesterId, groupId);
        List<ScheduleForGroupDTO> scheduleForGroupDTOList = new ArrayList<>();
        List<Group> groupsForSchedule = new ArrayList<>();
        if (semesterId != null && groupId != null) {
            if (groupHasScheduleInSemester(semesterId, groupId)) {
                groupsForSchedule.add(groupService.getById(groupId));
                ScheduleForGroupDTO scheduleForGroupDTO = new ScheduleForGroupDTO();
                scheduleForGroupDTO.setGroup(groupMapper.groupToGroupDTO(groupsForSchedule.get(0)));
                scheduleForGroupDTO.setDays(getDaysWhenGroupHasClassesBySemester(semesterId, groupId));
                scheduleForGroupDTOList.add(scheduleForGroupDTO);
            }
            return scheduleForGroupDTOList;
        } else {
            groupsForSchedule.addAll(scheduleRepository.uniqueGroupsInScheduleBySemester(semesterId));
            for (Group group : groupsForSchedule) {
                ScheduleForGroupDTO scheduleForGroupDTO = new ScheduleForGroupDTO();
                scheduleForGroupDTO.setGroup(groupMapper.groupToGroupDTO(group));
                scheduleForGroupDTO.setDays(getDaysWhenGroupHasClassesBySemester(semesterId, group.getId()));
                scheduleForGroupDTOList.add(scheduleForGroupDTO);
            }
            return scheduleForGroupDTOList;
        }
    }

    /**
     * Returns all days when given group has schedule and fill days by classes.
     *
     * @param semesterId the id of the semester
     * @param groupId    the id of the group
     * @return the list of the days when given group has schedule and fill days by classes
     */
    private List<DaysOfWeekWithClassesForGroupDTO> getDaysWhenGroupHasClassesBySemester(Long semesterId, Long groupId) {
        log.info("In getDaysWhenGroupHasClassesBySemester(semesterId = [{}], groupId = [{}])", semesterId, groupId);
        List<DaysOfWeekWithClassesForGroupDTO> daysOfWeekWithClassesForGroupDTOList = new ArrayList<>();
        List<DayOfWeek> weekList = scheduleRepository.getDaysWhenGroupHasClassesBySemester(semesterId, groupId);
        weekList.sort(Comparator.comparingInt(DayOfWeek::getValue));
        for (DayOfWeek day : weekList) {
            DaysOfWeekWithClassesForGroupDTO daysOfWeekWithClassesForGroupDTO = new DaysOfWeekWithClassesForGroupDTO();
            daysOfWeekWithClassesForGroupDTO.setDay(day);
            daysOfWeekWithClassesForGroupDTO.setClasses(getClassesForGroupBySemesterByDayOfWeek(semesterId, groupId, day));
            daysOfWeekWithClassesForGroupDTOList.add(daysOfWeekWithClassesForGroupDTO);

        }

        return daysOfWeekWithClassesForGroupDTOList;
    }

    /**
     * Returns all classes in the given day when group has schedule and fill classes by even/odd lessons.
     *
     * @param semesterId the id of the semester
     * @param groupId    the id of the group
     * @param day        the day of the week
     * @return the list of classes in the given day when group has schedule and fill classes by even/odd lessons
     */
    private List<ClassesInScheduleForGroupDTO> getClassesForGroupBySemesterByDayOfWeek(Long semesterId, Long groupId, DayOfWeek day) {
        log.info("In getClassesForGroupBySemesterByDayOfWeek(semesterId = [{}], groupId = [{}], day = [{}])", semesterId, groupId, day);
        //get Classes in that Day for group
        List<Period> uniquePeriods = scheduleRepository.periodsForGroupByDayBySemester(semesterId, groupId, day);
        List<ClassesInScheduleForGroupDTO> classesInScheduleForGroupDTOList = new ArrayList<>();

        for (Period period : uniquePeriods) {
            ClassesInScheduleForGroupDTO classesInScheduleForGroupDTO = new ClassesInScheduleForGroupDTO();
            classesInScheduleForGroupDTO.setPeriod(periodMapper.convertToDto(period));
            classesInScheduleForGroupDTO.setWeeks(getLessonsForGroupForPeriodBySemesterAndDay(semesterId, groupId, period.getId(), day));
            classesInScheduleForGroupDTOList.add(classesInScheduleForGroupDTO);
        }

        return classesInScheduleForGroupDTOList;
    }

    //get and fill even and odd lessons for group at some semester (by semester id) at some day for some period(by periodId)
    private LessonInScheduleByWeekDTO getLessonsForGroupForPeriodBySemesterAndDay(Long semesterId, Long groupId, Long periodId, DayOfWeek day) {
        log.info("In getLessonsForGroupForPeriodBySemesterAndDay(semesterId = [{}], groupId = [{}], periodId = [{}], day = [{}])",
                semesterId, groupId, periodId, day);
        LessonInScheduleByWeekDTO lessonInScheduleByWeekDTO = new LessonInScheduleByWeekDTO();
        Lesson lesson = scheduleRepository
                .lessonForGroupByDayBySemesterByPeriodByWeek(semesterId, groupId, periodId, day, EvenOdd.EVEN).orElse(null);
        LessonsInScheduleDTO even = lessonsInScheduleMapper.lessonToLessonsInScheduleDTO(lesson);

        if (lesson != null) {
            even.setRoom(roomForScheduleMapper.roomToRoomForScheduleDTO(
                    scheduleRepository.getRoomForLesson(semesterId, periodId, lesson.getId(), day, EvenOdd.EVEN)));
            lessonInScheduleByWeekDTO.setEven(even);
        }

        Lesson lesson2 = scheduleRepository.lessonForGroupByDayBySemesterByPeriodByWeek(semesterId, groupId, periodId, day, EvenOdd.ODD).orElse(null);
        LessonsInScheduleDTO odd = lessonsInScheduleMapper.lessonToLessonsInScheduleDTO(lesson2);
        if (lesson2 != null) {
            odd.setRoom(roomForScheduleMapper.roomToRoomForScheduleDTO(
                    scheduleRepository.getRoomForLesson(semesterId, periodId, lesson2.getId(), day, EvenOdd.ODD)));
            lessonInScheduleByWeekDTO.setOdd(odd);
        }

        return lessonInScheduleByWeekDTO;
    }

    //verifies if group with groupId has Schedule in semester with semesterId
    private boolean groupHasScheduleInSemester(Long semesterId, Long groupId) {
        log.info("In groupHasScheduleInSemester(semesterId = [{}], groupId = [{}])", semesterId, groupId);
        return scheduleRepository.countSchedulesForGroupInSemester(semesterId, groupId) != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Cacheable("scheduleList")
    @Override
    public ScheduleFullDTO getFullScheduleForSemester(Long semesterId) {
        ScheduleFullDTO scheduleFullDTO = new ScheduleFullDTO();
        SemesterMapper semesterMapper = new SemesterMapperImpl();
        scheduleFullDTO.setSemester(semesterMapper.semesterToSemesterDTO(semesterService.getById(semesterId)));

        List<ScheduleForGroupDTO> scheduleForGroupDTOList = new ArrayList<>();
        List<Group> groupsForSchedule = new ArrayList<>();

        groupsForSchedule.addAll(scheduleRepository.uniqueGroupsInScheduleBySemester(semesterId));
        for (Group group : groupsForSchedule) {
            ScheduleForGroupDTO scheduleForGroupDTO = new ScheduleForGroupDTO();
            scheduleForGroupDTO.setGroup(groupMapper.groupToGroupDTO(group));
            scheduleForGroupDTO.setDays(getDaysForSemester(semesterId, group.getId()));
            scheduleForGroupDTOList.add(scheduleForGroupDTO);

        }
        scheduleFullDTO.setSchedule(scheduleForGroupDTOList);
        return scheduleFullDTO;

    }

    private List<DaysOfWeekWithClassesForGroupDTO> getDaysForSemester(Long semesterId, Long groupId) {
        log.info("In getDaysForSemester(semesterId = [{}])", semesterId);
        List<DaysOfWeekWithClassesForGroupDTO> daysOfWeekWithClassesForGroupDTOList = new ArrayList<>();
        Set<DayOfWeek> weekList = semesterService.getById(semesterId).getDaysOfWeek();
        TreeSet<DayOfWeek> dayOfWeeks = new TreeSet<>(weekList);
        for (DayOfWeek day : dayOfWeeks) {
            DaysOfWeekWithClassesForGroupDTO daysOfWeekWithClassesForGroupDTO = new DaysOfWeekWithClassesForGroupDTO();
            daysOfWeekWithClassesForGroupDTO.setDay(day);
            daysOfWeekWithClassesForGroupDTO.setClasses(getClassesForSemesterByDay(semesterId, day, groupId));
            daysOfWeekWithClassesForGroupDTOList.add(daysOfWeekWithClassesForGroupDTO);

        }

        return daysOfWeekWithClassesForGroupDTOList;
    }

    private List<ClassesInScheduleForGroupDTO> getClassesForSemesterByDay(Long semesterId, DayOfWeek day, Long groupId) {
        log.info("In getClassesForSemester(semesterId = [{}])", semesterId);
        //get Classes in that Day for group
        Set<Period> semesterPeriods = semesterService.getById(semesterId).getPeriods();
        List<ClassesInScheduleForGroupDTO> classesInScheduleForGroupDTOList = new ArrayList<>();
        for (Period period : semesterPeriods) {
            ClassesInScheduleForGroupDTO classesInScheduleForGroupDTO = new ClassesInScheduleForGroupDTO();
            classesInScheduleForGroupDTO.setPeriod(periodMapper.convertToDto(period));
            classesInScheduleForGroupDTO.setWeeks(getLessonsForGroupForPeriodBySemesterAndDay(semesterId, groupId, period.getId(), day));
            classesInScheduleForGroupDTOList.add(classesInScheduleForGroupDTO);
        }

        return classesInScheduleForGroupDTOList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduleForTeacherDTO getScheduleForTeacher(Long semesterId, Long teacherId) {
        log.info("In getScheduleForTeacher(semesterId = [{}], teacherId[{}])", semesterId, teacherId);
        ScheduleForTeacherDTO scheduleForTeacherDTO = new ScheduleForTeacherDTO();
        SemesterMapper semesterMapper = new SemesterMapperImpl();
        scheduleForTeacherDTO.setSemester(semesterMapper.semesterToSemesterDTO(semesterService.getById(semesterId)));
        //get Teacher Info
        scheduleForTeacherDTO.setTeacher(teacherMapper.teacherToTeacherDTO(teacherService.getById(teacherId)));

        List<DayOfWeek> weekList = scheduleRepository.getDaysWhenTeacherHasClassesBySemester(semesterId, teacherId);
        weekList.sort(Comparator.comparingInt(DayOfWeek::getValue));

        List<DaysOfWeekWithClassesForTeacherDTO> daysOfWeekWithClassesForTeacherDTOList = new ArrayList<>();
        for (DayOfWeek day : weekList) {
            DaysOfWeekWithClassesForTeacherDTO daysOfWeekWithClassesForTeacherDTO = new DaysOfWeekWithClassesForTeacherDTO();
            daysOfWeekWithClassesForTeacherDTO.setDay(day);
            daysOfWeekWithClassesForTeacherDTO.setEvenWeek(getInfoForTeacherScheduleByWeek(semesterId, teacherId, day, EvenOdd.EVEN));
            daysOfWeekWithClassesForTeacherDTO.setOddWeek(getInfoForTeacherScheduleByWeek(semesterId, teacherId, day, EvenOdd.ODD));
            daysOfWeekWithClassesForTeacherDTOList.add(daysOfWeekWithClassesForTeacherDTO);
        }
        scheduleForTeacherDTO.setDays(daysOfWeekWithClassesForTeacherDTOList);
        return scheduleForTeacherDTO;
    }

    private ClassesInScheduleForTeacherDTO getInfoForTeacherScheduleByWeek(Long semesterId, Long teacherId, DayOfWeek day, EvenOdd evenOdd) {
        List<ClassForTeacherScheduleDTO> classForTeacherScheduleDTOList = new ArrayList<>();

        ClassesInScheduleForTeacherDTO classesInScheduleForTeacherDTO = new ClassesInScheduleForTeacherDTO();

        List<Period> periodList = scheduleRepository.periodsForTeacherBySemesterByDayByWeek(semesterId, teacherId, day, evenOdd);

        if (!periodList.isEmpty()) {
            for (Period period : periodList) {
                ClassForTeacherScheduleDTO classForTeacherScheduleDTO = new ClassForTeacherScheduleDTO();
                classForTeacherScheduleDTO.setPeriod(periodMapper.convertToDto(period));
                classForTeacherScheduleDTO.setLessons(
                        getLessonsForTeacherBySemesterByDayByWeekByPeriod(semesterId, teacherId, day, evenOdd, period.getId()));
                classForTeacherScheduleDTOList.add(classForTeacherScheduleDTO);

            }
        }
        classesInScheduleForTeacherDTO.setPeriods(classForTeacherScheduleDTOList);
        return classesInScheduleForTeacherDTO;
    }

    private List<LessonForTeacherScheduleDTO> getLessonsForTeacherBySemesterByDayByWeekByPeriod(Long semesterId, Long teacherId,
                                                                                                DayOfWeek day, EvenOdd evenOdd, Long periodId) {
        List<LessonForTeacherScheduleDTO> lessonForTeacherScheduleDTOList = new ArrayList<>();
        List<Lesson> lessons = scheduleRepository.lessonsForTeacherBySemesterByDayByPeriodByWeek(semesterId, teacherId, periodId, day, evenOdd);
        for (Lesson lesson : lessons) {
            LessonForTeacherScheduleDTO lessonForTeacherScheduleDTO = lessonForTeacherScheduleMapper.lessonToLessonForTeacherScheduleDTO(lesson);
            lessonForTeacherScheduleDTO.setRoom(
                    scheduleRepository.getRoomForLesson(semesterId, periodId, lessonForTeacherScheduleDTO.getId(), day, evenOdd).getName());
            lessonForTeacherScheduleDTOList.add(lessonForTeacherScheduleDTO);
        }
        return lessonForTeacherScheduleDTOList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Schedule> getAllSchedulesByTeacherIdAndSemesterId(Long teacherId, Long semesterId) {
        log.info("Enter into getAllSchedulesByTeacherIdAndSemesterId with teacherId = {}, semesterId = {}", teacherId, semesterId);
        return scheduleRepository.getAllSchedulesByTeacherIdAndSemesterId(teacherId, semesterId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Schedule> getSchedulesBySemester(Long semesterId) {
        log.info("In getScheduleBySemester(Long semesterId = [{}])", semesterId);

        return scheduleRepository.getScheduleBySemester(semesterId);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteSchedulesBySemesterId(Long semesterId) {
        log.info("In deleteSchedulesBySemesterId with semesterId = {}", semesterId);
        scheduleRepository.deleteSchedulesBySemesterId(semesterId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schedule saveScheduleDuringCopy(Schedule schedule) {
        log.info("In saveScheduleDuringCopy with schedule = {}", schedule);
        return scheduleRepository.save(schedule);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schedule updateWithoutChecks(Schedule schedule) {
        log.info("In updateWithoutChecks with schedule = {}", schedule);
        return scheduleRepository.update(schedule);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countInputLessonsInScheduleByLessonId(Long lessonId) {
        log.info("In countInputLessonsInScheduleByLessonId(lessonId = [{}])", lessonId);
        return scheduleRepository.countInputLessonsInScheduleByLessonId(lessonId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLessonInScheduleByLessonIdPeriodIdEvenOddDayOfWeek(Long lessonId, Long periodId, EvenOdd evenOdd, DayOfWeek day) {
        log.info("In countByLessonIdPeriodIdEvenOddDayOfWeek(lessonId = [{}], periodId = [{}], evenOdd = [{}], day = [{}])",
                lessonId, periodId, evenOdd, day);
        return scheduleRepository.countByLessonIdPeriodIdEvenOddDayOfWeek(lessonId, periodId, evenOdd, day) != 0;
    }

    //check date in semester date range, if yes return - true, else - false
    private boolean isDateInSemesterDateRange(Schedule schedule, LocalDate toDate) {
        DayOfWeek startSemester = schedule.getLesson().getSemester().getStartDay().getDayOfWeek();

        if (schedule.getEvenOdd() == EvenOdd.ODD) {
            if (startSemester.getValue() > schedule.getDayOfWeek().getValue()) {
                int i = startSemester.getValue() - schedule.getDayOfWeek().getValue();
                LocalDate firstCaseDate = schedule.getLesson().getSemester().getStartDay().plusDays(14L - i);

                return checkDateRangeForReturn(firstCaseDate, schedule.getLesson().getSemester().getEndDay(), toDate);
            }
            int k = schedule.getDayOfWeek().getValue() - startSemester.getValue();
            LocalDate secondCaseDate = schedule.getLesson().getSemester().getStartDay().plusDays(k);

            return checkDateRangeForReturn(secondCaseDate, schedule.getLesson().getSemester().getEndDay(), toDate);
        }

        if (schedule.getEvenOdd() == EvenOdd.EVEN || schedule.getEvenOdd() == EvenOdd.WEEKLY) {
            if (startSemester.getValue() > schedule.getDayOfWeek().getValue()) {
                int i = startSemester.getValue() - schedule.getDayOfWeek().getValue();
                LocalDate firstCaseDate = schedule.getLesson().getSemester().getStartDay().plusDays(7L - i);

                return checkDateRangeForReturn(firstCaseDate, schedule.getLesson().getSemester().getEndDay(), toDate);
            }
            int k = schedule.getDayOfWeek().getValue() - startSemester.getValue();
            if (schedule.getEvenOdd() == EvenOdd.WEEKLY) {
                LocalDate secondCaseDate = schedule.getLesson().getSemester().getStartDay().plusDays(k);
                return checkDateRangeForReturn(secondCaseDate, schedule.getLesson().getSemester().getEndDay(), toDate);
            }
            LocalDate thirdCaseDate = schedule.getLesson().getSemester().getStartDay().plusDays(7L + k);
            return checkDateRangeForReturn(thirdCaseDate, schedule.getLesson().getSemester().getEndDay(), toDate);
        }
        return false;
    }

    //this method use for don't duplicate code
    private boolean checkDateRangeForReturn(LocalDate dateForCheck, LocalDate semesterEndDate, LocalDate toDate) {
        return (dateForCheck.isBefore(semesterEndDate) || dateForCheck.isEqual(semesterEndDate)) &&
                (dateForCheck.isBefore(toDate) || dateForCheck.isEqual(toDate));
    }

    //check dates(even/odd/weekly) for distribution in baskets and create Map<LocalDate, Map<Period, List<Schedule>>>
    private Map<LocalDate, Map<Period, List<Schedule>>> fullScheduleForTeacherByDateRange(List<Schedule> schedules,
                                                                                          LocalDate fromDate, LocalDate toDate) {
        Map<LocalDate, List<Schedule>> scheduleByDateRange = new LinkedHashMap<>();

        for (LocalDate date = fromDate; date.isBefore(toDate.plusDays(1)); date = date.plusDays(1)) {
            List<Schedule> scheduleList = new ArrayList<>();
            for (Schedule schedule : schedules) {
                if (date.getDayOfWeek() == schedule.getDayOfWeek() && (date.isBefore(schedule.getLesson().getSemester().getEndDay()) ||
                        date.isEqual(schedule.getLesson().getSemester().getEndDay())) &&
                        (date.isAfter(schedule.getLesson().getSemester().getStartDay()) ||
                                date.isEqual(schedule.getLesson().getSemester().getStartDay()))) {
                    int countStartDate = schedule.getLesson().getSemester().getStartDay().getDayOfWeek().getValue();
                    int countEndDate = date.getDayOfWeek().getValue();
                    int countDays = Integer.parseInt(String.valueOf(ChronoUnit.DAYS.between(
                            schedule.getLesson().getSemester().getStartDay().minusDays(countStartDate), date.plusDays(7L - countEndDate))));

                    switch (schedule.getEvenOdd()) {
                        case ODD:
                            if ((countDays / 7) % 2 != 0) {
                                scheduleList.add(schedule);
                            }
                            break;
                        case EVEN:
                            if ((countDays / 7) % 2 == 0) {
                                scheduleList.add(schedule);
                            }
                            break;
                        case WEEKLY:
                            scheduleList.add(schedule);
                            break;
                        default:
                            break;
                    }
                }
            }
            if (!scheduleList.isEmpty()) {
                scheduleByDateRange.put(date, scheduleList);
            }
        }
        return convertToMapScheduleDateRange(scheduleByDateRange);
    }

    //convert from Map<LocalDate, List<Schedule>> to Map<LocalDate, Map<Period, List<Schedule>>> for easy way to convert dto in future
    private Map<LocalDate, Map<Period, List<Schedule>>> convertToMapScheduleDateRange(Map<LocalDate, List<Schedule>> scheduleByDateRange) {
        Map<LocalDate, Map<Period, List<Schedule>>> map = new LinkedHashMap<>();

        for (Map.Entry<LocalDate, List<Schedule>> itr : scheduleByDateRange.entrySet()) {
            Map<Period, List<Schedule>> collect = itr.getValue().stream()
                    .collect(Collectors.groupingBy(Schedule::getPeriod));

            /*Map<Period, List<Schedule>> sorted = collect.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey(Comparator.comparing(Period::getName)))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));*/
            Map<Period, List<Schedule>> sorted = new LinkedHashMap<>();
            collect.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.comparing(Period::getName)))
                    .forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));
            map.put(itr.getKey(), sorted);
        }
        return map;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void sendScheduleToTeachers(Long semesterId, Long[] teachersId, Locale language) {
        log.info("Enter into sendScheduleToTeachers of TeacherServiceImpl");
        Arrays.stream(teachersId).forEach(teacherId -> {
            try {
                sendScheduleToTeacher(semesterId, teacherId, language);
            } catch (MessagingException e) {
                throw new MessageNotSendException(e.getMessage());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendScheduleToTeacher(Long semesterId, Long teacherId, Locale language) throws MessagingException {
        log.info("Enter into sendScheduleToTeacher of TeacherServiceImpl");
        Teacher teacher = teacherService.getById(teacherId);
        ScheduleForTeacherDTO schedule = getScheduleForTeacher(semesterId, teacher.getId());
        PdfReportGenerator generatePdfReport = new PdfReportGenerator();
        ByteArrayOutputStream bos = generatePdfReport.teacherScheduleReport(schedule, language);
        String teacherEmail = userService.getById(teacher.getUserId()).getEmail();
        mailService.send(String.format("%s_%s_%s_%s.pdf", semesterService.getById(semesterId).getDescription(), teacher.getSurname(),
                        teacher.getName(), teacher.getPatronymic()),
                teacherEmail,
                "Schedule",
                String.format("Schedule for %s %s %s", teacher.getSurname(), teacher.getName(), teacher.getPatronymic()),
                bos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Map<Room, List<Schedule>> getAllOrdered(Long semesterId) {
        log.info("Entered getAllOrdered({})", semesterId);
        return scheduleRepository
                .getAllOrdered(semesterId)
                .stream()
                .collect(Collectors.groupingBy(e -> e.getRoom(), LinkedHashMap::new, Collectors.toList()));
    }
}



