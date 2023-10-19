package com.softserve.service.impl;

import com.softserve.entity.*;
import com.softserve.exception.*;
import com.softserve.repository.GroupRepository;
import com.softserve.repository.LessonRepository;
import com.softserve.repository.ScheduleRepository;
import com.softserve.repository.SemesterRepository;
import com.softserve.service.PeriodService;
import com.softserve.service.SemesterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.CachePut;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class SemesterServiceImpl implements SemesterService {

    private final SemesterRepository semesterRepository;
    private final ScheduleRepository scheduleRepository;
    private final LessonRepository lessonRepository;
    private final GroupRepository groupRepository;

    private final PeriodService periodService;

    private final List<DayOfWeek> workDaysList = Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);

    @Autowired
    public SemesterServiceImpl(SemesterRepository semesterRepository,
                               PeriodService periodService,
                               GroupRepository groupRepository,
                               ScheduleRepository scheduleService,
                               LessonRepository lessonRepository) {
        this.semesterRepository = semesterRepository;
        this.periodService = periodService;
        this.groupRepository = groupRepository;
        this.scheduleRepository = scheduleService;
        this.lessonRepository = lessonRepository;
    }

    /**
     * {@inheritDoc}
     */
    //@Cacheable(value = "map", key = "#id")
    @Override
    public Semester getById(Long id) {
        log.info("In getById(id = [{}])", id);
        Semester semester = semesterRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(Semester.class, "id", id.toString()));
        Hibernate.initialize(semester.getDaysOfWeek());
        Hibernate.initialize(semester.getPeriods());
        Hibernate.initialize(semester.getGroups());
        return semester;
    }

    /**
     * {@inheritDoc}
     */
    //@Cacheable(value = "semesterList")
    @Override
    public List<Semester> getAll() {
        log.debug("In getAll()");
        return semesterRepository.getAll();
    }

    /**
     * {@inheritDoc}
     */
    //@CacheEvict(value = "semesterList", allEntries = true)
    @Override
    public Semester save(Semester semester) {
        log.info("In save(entity = [{}]", semester);
        checkConstraints(semester);
        fillDefaultValues(semester);
        setCurrentToFalse(semester);
        setDefaultToFalse(semester);
        return semesterRepository.save(semester);
    }

    /**
     * Checks constraints for the given semester.
     *
     * @param semester the semester to be checked
     * @throws IncorrectTimeException       if the start time of the period was after its end or the start time was equal to the end time
     * @throws EntityAlreadyExistsException if semester already exists with given description and year
     */
    private void checkConstraints(Semester semester) {
        if (isTimeInvalid(semester)) {
            throw new IncorrectTimeException("The end day cannot be before the start day");
        }
        if (isSemesterExists(semester.getId(), semester.getDescription(), semester.getYear())) {
            throw new EntityAlreadyExistsException("Semester already exists with current description and year.");
        }
    }

    /**
     * Checks update constraints for the given semester.
     *
     * @param semester the semester to be checked
     * @throws UsedEntityException if semester have schedule and can not be removed,
     *                             if one or more days in a semester have lessons and can not be removed,
     *                             if one or more classes in a semester have lessons and can not be removed
     */
    private void checkUpdateConstraints(Semester semester) {
        checkConstraints(semester);
        if (isScheduleWithLessonsCanNotBeRemoved(semester)) {
            throw new UsedEntityException("Semester have Schedule and can not be removed");
        }
        if (isDaysWithLessonsCanNotBeRemoved(semester)) {
            throw new UsedEntityException("One or more days in a semester have lessons and can not be removed");
        }
        if (isPeriodsWithLessonsCanNotBeRemoved(semester)) {
            throw new UsedEntityException("One or more classes in a semester have lessons and can not be removed");
        }
    }

    /**
     * Fills the semester by default values.
     *
     * @param semester the semester to be filled
     */
    private void fillDefaultValues(Semester semester) {
        if (CollectionUtils.isEmpty(semester.getDaysOfWeek())) {
            semester.setDaysOfWeek(new HashSet<>(workDaysList));
        }
        if (CollectionUtils.isEmpty(semester.getPeriods())) {
            semester.setPeriods(new HashSet<>(periodService.getFirstFourPeriods()));
        }
    }

    /**
     * Sets current semester in the repository to {@code false} while saving new current semester or updating current semester.
     *
     * @param semester the semester to be saved or updated
     */
    private void setCurrentToFalse(Semester semester) {
        if (semester.isCurrentSemester()) {
            semesterRepository.updateAllSemesterCurrentToFalse();
            semesterRepository.setCurrentSemester(semester.getId());
        }
    }

    /**
     * Sets default semester in the repository to {@code false} while saving new default semester or updating default semester.
     *
     * @param semester the semester to be saved or updated
     */
    private void setDefaultToFalse(Semester semester) {
        if (semester.isDefaultSemester()) {
            semesterRepository.updateAllSemesterDefaultToFalse();
            semesterRepository.setDefaultSemester(semester.getId());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws UsedEntityException if the given semester has not passed checkUpdateConstraints
     */
    //@Caching(put = {@CachePut(value = "map", key = "#semester.id")},
     //       evict = {@CacheEvict(value = "semesterList", allEntries = true)})
    @Override
    public Semester update(Semester semester) {
        log.debug("In update(entity = [{}]", semester);
        checkUpdateConstraints(semester);
        setCurrentToFalse(semester);
        setDefaultToFalse(semester);
        return semesterRepository.update(semester);
    }

    /**
     * {@inheritDoc}
     */
    //@CacheEvict(value = "map", key = "#object.id")
    @Override
    public Semester delete(Semester object) {
        log.debug("In delete(object = [{}])", object);
        return semesterRepository.delete(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Semester getCurrentSemester() {
        log.debug("In getCurrentSemester");
        Semester semester = semesterRepository.getCurrentSemester().orElseThrow(
                () -> new ScheduleConflictException("Current semester for managers work isn't specified"));
        Hibernate.initialize(semester.getDaysOfWeek());
        Hibernate.initialize(semester.getPeriods());
        Hibernate.initialize(semester.getGroups());
        return semester;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Semester getDefaultSemester() {
        log.debug("In getDefaultSemester");
        Semester semester = semesterRepository.getDefaultSemester().orElseThrow(
                () -> new ScheduleConflictException("Default semester isn't specified"));
        Hibernate.initialize(semester.getDaysOfWeek());
        Hibernate.initialize(semester.getPeriods());
        Hibernate.initialize(semester.getGroups());
        return semester;
    }

    /**
     * Checks the start and end days of the semester.
     *
     * @param object the semester that will be checked
     * @return {@code true} if the end time is before the start time or equals, otherwise return {@code false}
     */
    private boolean isTimeInvalid(Semester object) {
        log.info("Enter into isTimeInvalid  with entity: {}", object);
        return object.getStartDay().isAfter(object.getEndDay()) ||
                object.getStartDay().equals(object.getEndDay());
    }

    /**
     * Check existence of the given semester.
     *
     * @param semesterId  the id of the semester
     * @param description the string represents the description of the semester
     * @param year        the year of the semester
     * @return {@code true} if the given semester exists in the repository
     */
    private boolean isSemesterExists(Long semesterId, String description, int year) {
        log.info("In isSemesterExists (semesterId = [{}],description = [{}], year = [{}])", semesterId, description, year);
        Semester existingSemester = semesterRepository.getSemesterByDescriptionAndYear(description, year).orElse(null);
        if (existingSemester == null) {
            return false;
        }
        return existingSemester.getId().equals(semesterId);
    }

    /**
     * Checks if days with lessons are not removed from the updated semester.
     *
     * @param semester the semester that will be checked before updating
     * @return {@code true} if one or more days with lessons have been removed from the updated semester,
     * {@code false} if days with lessons have not been removed
     */
    private boolean isDaysWithLessonsCanNotBeRemoved(Semester semester) {
        log.debug("Enter into isDaysWithLessonsCanBeRemoved with entity: {}", semester);
        List<DayOfWeek> daysInSchedule = semesterRepository.getDaysWithLessonsBySemesterId(semester.getId());
        return !semester.getDaysOfWeek().containsAll(daysInSchedule);
    }

    /**
     * Checks if semester with schedule are not removed from the updated semester.
     *
     * @param semester the semester that will be checked before updating
     * @return {@code true} if schedule have been removed from the updated semester;
     * {@code false} if schedule have not been removed.
     */
    private boolean isScheduleWithLessonsCanNotBeRemoved(Semester semester) {
        log.debug("Enter into isScheduleWithLessonsCanNotBeRemoved with entity: {}", semester);
        List<Schedule> scheduleInSemester = scheduleRepository.getScheduleBySemester(semester.getId());
        return CollectionUtils.isNotEmpty(scheduleInSemester);
    }

    /**
     * Checks if periods with lessons are not removed from the updated semester.
     *
     * @param semester the semester that will be checked before updating
     * @return {@code true} if one or more periods with lessons have been removed from the updated semester,
     * {@code false} if periods with lessons have not been removed.
     */
    private boolean isPeriodsWithLessonsCanNotBeRemoved(Semester semester) {
        log.debug("Enter into isPeriodsWithLessonsCanNotBeRemoved with entity: {}", semester);
        List<Period> periodsInSchedule = semesterRepository.getPeriodsWithLessonsBySemesterId(semester.getId());
        return !semester.getPeriods().containsAll(periodsInSchedule);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Semester> getDisabled() {
        log.debug("Enter into getAll of getDisabled");
        List<Semester> semesters = semesterRepository.getDisabled();
        for (Semester semester : semesters) {
            Hibernate.initialize(semester.getDaysOfWeek());
            Hibernate.initialize(semester.getPeriods());
            Hibernate.initialize(semester.getGroups());
        }
        return semesters;
    }

    /**
     * {@inheritDoc}
     */
    //@CacheEvict(value = "semesterList", allEntries = true)
    @Override
    public Semester changeCurrentSemester(Long semesterId) {
        log.debug("In changeCurrentSemester(Long semesterId = [{}])", semesterId);
        semesterRepository.updateAllSemesterCurrentToFalse();
        semesterRepository.setCurrentSemester(semesterId);
        return getById(semesterId);
    }

    /**
     * {@inheritDoc}
     */
    //@CacheEvict(value = "semesterList", allEntries = true)
    @Override
    public Semester changeDefaultSemester(Long semesterId) {
        log.debug("In changeDefaultSemester(Long semesterId = [{}])", semesterId);
        semesterRepository.updateAllSemesterDefaultToFalse();
        semesterRepository.setDefaultSemester(semesterId);
        return getById(semesterId);
    }

    /**
     * {@inheritDoc}
     */
    //@CacheEvict(value = "semesterList", allEntries = true)
    @Override
    public Semester addGroupToSemester(Semester semester, Group group) {
        log.debug("In addGroupToSemester (semester = [{}], group = [{}])", semester, group);
        Set<Group> groups = semester.getGroups();
        if (groups == null) {
            groups = new HashSet<>();
        }
        groups.add(group);
        semester.setGroups(groups);
        return semester;
    }

    /**
     * {@inheritDoc}
     */
    //@CacheEvict(value = "semesterList", allEntries = true)
    @Override
    public Semester addGroupsToSemester(Semester semester, List<Long> groupIds) {
        log.info("In addGroupsToSemester (semester = [{}], groupIds = [{}])", semester, groupIds);
        List<Group> groups = groupRepository.getGroupsByGroupIds(groupIds);
        Set<Group> groupSet = new HashSet<>(groups);
        semester.setGroups(groupSet);
        semesterRepository.update(semester);
        log.debug("Semester groups has been updated");
        return semester;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Semester addDaysOfWeekToSemester(Semester semester, Set<DayOfWeek> daysOfWeek) {
        log.debug("In addDaysOfWeekToSemester (semester = [{}], daysOfWeek = [{}])", semester, daysOfWeek);

        Set<DayOfWeek> days = semester.getDaysOfWeek();

        if (days == null) {
            days = new HashSet<>();
        }
        days.addAll(daysOfWeek);
        semester.setDaysOfWeek(days);
        return semester;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Semester addPeriodsToSemester(Semester semester, Set<Period> periods) {
        log.debug("In addPeriodsToSemester (semester = [{}], periods = [{}])", semester, periods);

        Set<Period> periodsSemester = semester.getPeriods();

        if (periodsSemester == null) {
            periodsSemester = new HashSet<>();
        }
        periodsSemester.addAll(periods);
        semester.setPeriods(periodsSemester);
        return semester;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Semester deleteGroupFromSemester(Semester semester, Group group) {
        log.debug("In deleteGroupFromSemester (semester = [{}], group = [{}])", semester, group);
        Set<Group> groups = semester.getGroups();
        groups.remove(group);
        update(semester);
        return semester;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Semester deleteAllContentFromSemester(Semester semester) {
        log.debug("In deleteAllContentFromSemester (semester = [{}] )", semester);
        Set<Group> groups = new HashSet<>();
        Set<Period> periods = new HashSet<>();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        semester.setGroups(groups);
        semester.setPeriods(periods);
        semester.setDaysOfWeek(dayOfWeeks);
        return semester;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Semester deleteGroupsFromSemester(Semester semester, List<Group> groups) {
        log.debug("In deleteGroupsFromSemester (semester = [{}], group = [{}])", semester, groups);
        groups.forEach(group -> deleteGroupFromSemester(semester, group));
        return semester;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Semester copySemester(Long fromSemesterId, Long toSemesterId) {
        log.info("In copySemester (fromSemesterId = [{}], toSemesterId = [{}])", fromSemesterId, toSemesterId);
        Semester toSemester = getById(toSemesterId);
        Semester fromSemester = getById(fromSemesterId);
        List<Schedule> schedules = scheduleRepository.getScheduleBySemester(fromSemesterId);

        if (shouldClearSemesterContent(toSemester)) {
            deleteAllContentFromSemester(toSemester);
        }

        addGroupsToSemester(toSemester, fromSemester.getGroups().stream().map(Group::getId).collect(Collectors.toList()));
        addDaysOfWeekToSemester(toSemester, fromSemester.getDaysOfWeek());
        addPeriodsToSemester(toSemester, fromSemester.getPeriods());

        Set<Lesson> lessonSet = schedules.stream().map(Schedule::getLesson).collect(Collectors.toSet());

        copySchedules(schedules, copyLessons(lessonSet, toSemester));

        return update(toSemester);
    }

    private boolean shouldClearSemesterContent(Semester semester) {
        return CollectionUtils.isNotEmpty(semester.getGroups())
                || CollectionUtils.isNotEmpty(semester.getPeriods())
                || CollectionUtils.isNotEmpty(semester.getDaysOfWeek());
    }

    private Map<Long, Lesson> copyLessons(Set<Lesson> lessonSet, Semester toSemester) {
        log.debug("In copyLessons (lessonSet = [{}], toSemester = [{}])", lessonSet, toSemester);
        Map<Long, Lesson> oldToNewLessonMap = new HashMap<>();

        for (Lesson lesson : lessonSet) {
            Lesson lessonNew = new Lesson();
            lessonNew.setSemester(toSemester);
            lessonNew.setHours(lesson.getHours());
            lessonNew.setLessonType(lesson.getLessonType());
            lessonNew.setSubjectForSite(lesson.getSubjectForSite());
            lessonNew.setGroup(lesson.getGroup());
            lessonNew.setSubject(lesson.getSubject());
            lessonNew.setTeacher(lesson.getTeacher());
            lessonNew.setGrouped(lesson.isGrouped());
            lessonNew.setLinkToMeeting(lesson.getLinkToMeeting());
            Lesson lessonNewSaved = lessonRepository.save(lessonNew);
            oldToNewLessonMap.put(lesson.getId(), lessonNewSaved);
        }
        return oldToNewLessonMap;
    }

    private List<Schedule> copySchedules(List<Schedule> schedules, Map<Long, Lesson> oldToNewLessonMap) {
        log.debug("In copySchedules (schedules = [{}], oldToNewLessonMap = [{}])", schedules, oldToNewLessonMap);
        List<Schedule> scheduleSaved = new ArrayList<>();

        for (Schedule schedule : schedules) {
            Schedule scheduleNew = new Schedule();
            scheduleNew.setDayOfWeek(schedule.getDayOfWeek());
            scheduleNew.setEvenOdd(schedule.getEvenOdd());
            scheduleNew.setLesson(oldToNewLessonMap.get(schedule.getLesson().getId()));
            scheduleNew.setPeriod(schedule.getPeriod());
            scheduleNew.setRoom(schedule.getRoom());
            scheduleSaved.add(scheduleRepository.save(scheduleNew));
        }
        return scheduleSaved;
    }

}
