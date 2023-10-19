package com.softserve.repository.impl;

import com.softserve.entity.*;
import com.softserve.entity.enums.EvenOdd;
import com.softserve.repository.ScheduleRepository;
import com.softserve.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class ScheduleRepositoryImpl extends BasicRepositoryImpl<Schedule, Long> implements ScheduleRepository {
    private static final String NOT_DISABLED_SQL = " and s.room.disable=false and s.lesson.semester.disable=false " +
            "and s.lesson.group.disable=false  and s.lesson.teacher.disable=false and s.lesson.subject.disable=false ";

    private static final String SELECT_COUNT = "select count (s.id) " +
            "from Schedule s where s.lesson.semester.id = :semesterId " +
            "and s.dayOfWeek = :dayOfWeek " +
            "and s.period.id = :classId " + NOT_DISABLED_SQL;

    private static final String GET_BY_ALL_PARAMETERS = "FROM Schedule s where s.period.id = :periodId " +
            "and s.lesson.id = :lessonId and s.dayOfWeek = :dayOfWeek and s.evenOdd = :evenOdd and s.room.id = :roomId";

    private static final String GET_ALL_ORDERED_BY_ROOMS_DAYS_PERIODS
            = "SELECT s "
            + "FROM Schedule s "
            + "where s.lesson.semester.id = :semesterId "
            + "ORDER BY s.room.name, " //if sort_order implemented, must be sort_order
            + " CASE "
            + "WHEN day_of_week = 'Monday' THEN 1 "
            + "WHEN day_of_week = 'Tuesday' THEN 2 "
            + "WHEN day_of_week = 'Wednesday' THEN 3 "
            + "WHEN day_of_week = 'Thursday' THEN 4 "
            + "WHEN day_of_week = 'Friday' THEN 5 "
            + "WHEN day_of_week = 'Saturday' THEN 6 "
            + "WHEN day_of_week = 'Sunday' THEN 7 "
            + "END, "
            + "s.evenOdd, s.period.name, "
            + "s.lesson.subjectForSite, s.lesson.teacher.surname, s.lesson.lessonType ";

    private static final String ORDERED_BY_SORTING_ORDER
            = "ORDER BY g1.sortOrder ASC";

    /**
     * {@inheritDoc}
     */
    @Override
    public Long conflictForGroupInSchedule(Long semesterId, DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId, Long groupId) {
        log.info("In isConflictForGroupInSchedule(semesterId = [{}], dayOfWeek = [{}], evenOdd = [{}], classId = [{}], groupId = [{}])",
                semesterId, dayOfWeek, evenOdd, classId, groupId);
        //if schedule pretends to occur weekly need to check that there are no any already saved schedules for that Group
        if (evenOdd == EvenOdd.WEEKLY) {
            log.debug("Search when lesson repeats weekly");
            return (Long) sessionFactory.getCurrentSession().createQuery(
                            SELECT_COUNT +
                                    "and s.lesson.group.id = :groupId")
                    .setParameter(Constants.SEMESTER_ID, semesterId)
                    .setParameter(Constants.DAY_OF_WEEK, dayOfWeek)
                    .setParameter(Constants.CLASS_ID, classId)
                    .setParameter(Constants.GROUP_ID, groupId)
                    .getSingleResult();
        } else {
            //else schedule pretends to occur by even/odd need to check that here are
            // no amy already saved schedules for that Group at the same half or weekly
            log.debug("Search when lesson repeats by even/odd");
            return (Long) sessionFactory.getCurrentSession().createQuery(
                            SELECT_COUNT +
                                    "and s.lesson.group.id = :groupId " +
                                    "and ( s.evenOdd = :evenOdd or s.evenOdd = 'WEEKLY')")
                    .setParameter(Constants.SEMESTER_ID, semesterId)
                    .setParameter(Constants.DAY_OF_WEEK, dayOfWeek)
                    .setParameter(Constants.CLASS_ID, classId)
                    .setParameter(Constants.GROUP_ID, groupId)
                    .setParameter(Constants.EVEN_ODD, evenOdd)
                    .getSingleResult();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long conflictForTeacherInSchedule(Long semesterId, DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId, Long teacherId) {
        log.info("In conflictForTeacherInSchedule(semesterId = [{}], dayOfWeek = [{}], evenOdd = [{}], classId = [{}], teacherId = [{}])",
                semesterId, dayOfWeek, evenOdd, classId, teacherId);
        if (evenOdd == EvenOdd.WEEKLY) {
            return (Long) sessionFactory.getCurrentSession().createQuery("" +
                            SELECT_COUNT +
                            "and s.lesson.teacher.id = :teacherId ")
                    .setParameter(Constants.SEMESTER_ID, semesterId)
                    .setParameter(Constants.DAY_OF_WEEK, dayOfWeek)
                    .setParameter(Constants.CLASS_ID, classId)
                    .setParameter(Constants.TEACHER_ID, teacherId)
                    .getSingleResult();

        } else {
            return (Long) sessionFactory.getCurrentSession().createQuery(
                            SELECT_COUNT +
                                    "and s.lesson.teacher.id = :teacherId " +
                                    "and ( s.evenOdd = :evenOdd or s.evenOdd = 'WEEKLY')")
                    .setParameter(Constants.SEMESTER_ID, semesterId)
                    .setParameter(Constants.DAY_OF_WEEK, dayOfWeek)
                    .setParameter(Constants.CLASS_ID, classId)
                    .setParameter(Constants.TEACHER_ID, teacherId)
                    .setParameter(Constants.EVEN_ODD, evenOdd)
                    .getSingleResult();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> uniqueGroupsInScheduleBySemester(Long semesterId) {
        log.info("In uniqueGroupsInScheduleBySemester", semesterId);
        return sessionFactory.getCurrentSession().createQuery(
                        "select distinct g1 from Group g1 " +
                                "where g1.id in " +
                                "(select g.id from Schedule s join s.lesson.group g " +
                                "" +
                                "where s.lesson.semester.id = :semesterId" + NOT_DISABLED_SQL + ")" +
                                ORDERED_BY_SORTING_ORDER)
                .setParameter(Constants.SEMESTER_ID, semesterId).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Period> periodsForGroupByDayBySemester(Long semesterId, Long groupId, DayOfWeek day) {
        log.info("In periodsForGroupByDayBySemester(semesterId = [{}], groupId = [{}], day = [{}])", semesterId, groupId, day);
        return sessionFactory.getCurrentSession().createQuery(
                        "select distinct p1 from Period p1 " +
                                "where p1.id in " +
                                "(select p.id from Schedule s join s.period p " +
                                "where s.lesson.semester.id = :semesterId " +
                                "and s.lesson.group.id = :groupId " +
                                "and s.dayOfWeek = :dayOfWeek" + NOT_DISABLED_SQL + ") order by p1.startTime")
                .setParameter(Constants.SEMESTER_ID, semesterId)
                .setParameter(Constants.GROUP_ID, groupId)
                .setParameter(Constants.DAY_OF_WEEK, day)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Lesson> lessonForGroupByDayBySemesterByPeriodByWeek(Long semesterId, Long groupId, Long periodId,
                                                                        DayOfWeek day, EvenOdd evenOdd) {
        log.info("In lessonForGroupByDayBySemesterByPeriodByWeek(semesterId = [{}], groupId = [{}], periodId = [{}], day = [{}], evenOdd = [{}])",
                semesterId, groupId, periodId, day, evenOdd);
        return sessionFactory.getCurrentSession().createQuery(
                        "select l1 from Lesson l1 " +
                                "where l1.id in " +
                                "(select l.id from Schedule s join s.lesson l " +
                                "where (s.lesson.semester.id = :semesterId " +
                                "and s.dayOfWeek = :dayOfWeek " +
                                "and s.period.id = :periodId " +
                                "and s.lesson.group.id = :groupId " + NOT_DISABLED_SQL + ") " +
                                "and (s.evenOdd = :evenOdd or s.evenOdd = 'WEEKLY'))")
                .setParameter(Constants.SEMESTER_ID, semesterId)
                .setParameter(Constants.GROUP_ID, groupId)
                .setParameter(Constants.PERIOD_ID, periodId)
                .setParameter(Constants.DAY_OF_WEEK, day)
                .setParameter(Constants.EVEN_ODD, evenOdd)
                .uniqueResultOptional();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Room getRoomForLesson(Long semesterId, Long periodId, Long lessonId, DayOfWeek day, EvenOdd evenOdd) {
        log.info("In getRoomForLesson(semesterId = [{}], periodId = [{}], lessonId = [{}], day = [{}], evenOdd = [{}])",
                semesterId, periodId, lessonId, day, evenOdd);
        return (Room) sessionFactory.getCurrentSession().createQuery(
                        "select r1 from Room r1 " +
                                "where r1.id in" +
                                "(select r.id from Schedule s join s.room r " +
                                "where (s.lesson.semester.id = :semesterId " +
                                "and s.dayOfWeek = :dayOfWeek " +
                                "and s.period.id = :periodId " +
                                "and s.lesson.id = :lessonId " + NOT_DISABLED_SQL + ") " +
                                "and (s.evenOdd = :evenOdd or s.evenOdd = 'WEEKLY'))")
                .setParameter(Constants.SEMESTER_ID, semesterId)
                .setParameter(Constants.LESSON_ID, lessonId)
                .setParameter(Constants.PERIOD_ID, periodId)
                .setParameter(Constants.DAY_OF_WEEK, day)
                .setParameter(Constants.EVEN_ODD, evenOdd)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DayOfWeek> getDaysWhenGroupHasClassesBySemester(Long semesterId, Long groupId) {
        log.info("In getDaysWhenGroupHasClassesBySemester(semesterId = [{}], groupId = [{}])", semesterId, groupId);
        return sessionFactory.getCurrentSession().createQuery(
                        "select distinct s.dayOfWeek from  Schedule s " +
                                "where s.lesson.semester.id = :semesterId " +
                                "and s.lesson.group.id = :groupId")
                .setParameter(Constants.SEMESTER_ID, semesterId)
                .setParameter(Constants.GROUP_ID, groupId)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countSchedulesForGroupInSemester(Long semesterId, Long groupId) {
        log.info("In countSchedulesForGroupInSemester(semesterId = [{}], groupId = [{}])", semesterId, groupId);
        return (Long) sessionFactory.getCurrentSession().createQuery(
                        "select count (s.id) from Schedule s " +
                                "where s.lesson.semester.id = :semesterId " +
                                "and s.lesson.group.id = :groupId" + NOT_DISABLED_SQL)
                .setParameter(Constants.SEMESTER_ID, semesterId)
                .setParameter(Constants.GROUP_ID, groupId)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DayOfWeek> getDaysWhenTeacherHasClassesBySemester(Long semesterId, Long teacherId) {
        log.info("In getDaysWhenTeacherHasClassesBySemester(semesterId = [{}], teacherId = [{}])", semesterId, teacherId);
        return sessionFactory.getCurrentSession().createQuery(
                        "select distinct s.dayOfWeek from  Schedule s " +
                                "where s.lesson.semester.id = :semesterId " +
                                "and s.lesson.teacher.id = :teacherId" + NOT_DISABLED_SQL)
                .setParameter(Constants.SEMESTER_ID, semesterId)
                .setParameter(Constants.TEACHER_ID, teacherId)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Period> periodsForTeacherBySemesterByDayByWeek(Long semesterId, Long teacherId, DayOfWeek day, EvenOdd evenOdd) {
        log.info("In periodsForTeacherBySemesterByDayByWeek(semesterId = [{}], teacherId = [{}], day = [{}], evenOdd = [{}])",
                semesterId, teacherId, day, evenOdd);
        return sessionFactory.getCurrentSession().createQuery(
                        "select distinct p1 from Period p1 " +
                                "where p1.id in " +
                                "(select p.id from Schedule s join s.period p " +
                                "where (s.lesson.semester.id = :semesterId " +
                                "and s.lesson.teacher.id = :teacherId " +
                                "and s.dayOfWeek = :dayOfWeek " + NOT_DISABLED_SQL + ") " +
                                "and (s.evenOdd = :evenOdd or s.evenOdd = 'WEEKLY')) order by p1.startTime")
                .setParameter(Constants.SEMESTER_ID, semesterId)
                .setParameter(Constants.TEACHER_ID, teacherId)
                .setParameter(Constants.DAY_OF_WEEK, day)
                .setParameter(Constants.EVEN_ODD, evenOdd)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lesson> lessonsForTeacherBySemesterByDayByPeriodByWeek(Long semesterId, Long teacherId, Long periodId,
                                                                       DayOfWeek day, EvenOdd evenOdd) {
        log.info("In lessonsForTeacherBySemesterByDayByPeriodByWeek" +
                "(semesterId = [{}], teacherId = [{}], periodId = [{}], day = [{}], evenOdd = [{}])", semesterId, teacherId, periodId, day, evenOdd);
        return sessionFactory.getCurrentSession().createQuery(
                        "select l1 from Lesson l1 " +
                                "where l1.id in " +
                                "(select l.id from Schedule s join s.lesson l " +
                                "where (s.lesson.semester.id = :semesterId " +
                                "and s.dayOfWeek = :dayOfWeek " +
                                "and s.period.id = :periodId " +
                                "and s.lesson.teacher.id = :teacherId " + NOT_DISABLED_SQL + ") " +
                                "and (s.evenOdd = :evenOdd or s.evenOdd = 'WEEKLY'))")
                .setParameter(Constants.SEMESTER_ID, semesterId)
                .setParameter(Constants.TEACHER_ID, teacherId)
                .setParameter(Constants.PERIOD_ID, periodId)
                .setParameter(Constants.DAY_OF_WEEK, day)
                .setParameter(Constants.EVEN_ODD, evenOdd)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Schedule> getAllSchedulesByTeacherIdAndSemesterId(Long teacherId, Long semesterId) {
        log.info("Enter into getAll of TeacherRepositoryImpl");
        return sessionFactory.getCurrentSession().
                createQuery(
                        "from Schedule s where s.lesson.semester.id = :semesterId " +
                                "and s.lesson.teacher.id = :teacherId " + NOT_DISABLED_SQL)
                .setParameter(Constants.SEMESTER_ID, semesterId)
                .setParameter(Constants.TEACHER_ID, teacherId)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schedule getScheduleByObject(Schedule schedule) {
        log.info("Enter into getScheduleByObject");
        return sessionFactory.getCurrentSession().
                createQuery(GET_BY_ALL_PARAMETERS, Schedule.class)
                .setParameter(Constants.PERIOD_ID, schedule.getPeriod().getId())
                .setParameter(Constants.LESSON_ID, schedule.getLesson().getId())
                .setParameter(Constants.DAY_OF_WEEK, schedule.getDayOfWeek())
                .setParameter(Constants.EVEN_ODD, schedule.getEvenOdd())
                .setParameter(Constants.ROOM_ID, schedule.getRoom().getId())
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Schedule> getScheduleBySemester(Long semesterId) {
        log.info("In getScheduleBySemester(semesterId = [{}])", semesterId);

        return sessionFactory.getCurrentSession().createQuery(
                        "SELECT distinct s " +
                                "from Schedule s " +
                                "join fetch s.lesson sl " +
                                "join fetch sl.semester slm " +
                                "join fetch slm.periods " +
                                "join fetch slm.groups " +
                                "join fetch slm.daysOfWeek " +
                                "where s.lesson.semester.id = :semesterId " + NOT_DISABLED_SQL)
                .setParameter(Constants.SEMESTER_ID, semesterId)
                .getResultList();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Schedule> getAll() {
        log.info("In getAll()");
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> from = cq.from(Schedule.class);
        cq.where(cb.equal(from.get(Constants.ROOM).get(Constants.DISABLE), false),
                cb.equal(from.get(Constants.LESSON).get(Constants.SEMESTER).get(Constants.DISABLE), false),
                cb.equal(from.get(Constants.LESSON).get(Constants.GROUP).get(Constants.DISABLE), false),
                cb.equal(from.get(Constants.LESSON).get(Constants.SUBJECT).get(Constants.DISABLE), false),
                cb.equal(from.get(Constants.LESSON).get(Constants.TEACHER).get(Constants.DISABLE), false));

        TypedQuery<Schedule> tq = sessionFactory.getCurrentSession().createQuery(cq);
        return tq.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Schedule> scheduleByDateRangeForTeacher(LocalDate fromDate, LocalDate toDate, Long teacherId) {
        log.info("In scheduleByDateRangeForTeacher with fromDate = {} and toDate = {}", fromDate, toDate);
        return sessionFactory.getCurrentSession().createQuery("SELECT s from Schedule s " +
                        "where s.lesson.semester.startDay <= :toDate  and s.lesson.semester.endDay >= :fromDate and s.lesson.teacher.id = :teacherId")
                .setParameter(Constants.FROM_DATE, fromDate)
                .setParameter(Constants.TO_DATE, toDate)
                .setParameter(Constants.TEACHER_ID, teacherId)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Schedule> scheduleForRoomBySemester(Long semesterId, Long roomId) {
        log.info("In scheduleForRoomBySemester with semesterId = {} and roomId = {}", semesterId, roomId);
        return sessionFactory.getCurrentSession().createQuery(
                        "SELECT s from Schedule s " +
                                "where s.room.id = :roomId " +
                                "and s.lesson.semester.id = :semesterId order by s.period.startTime asc ")
                .setParameter(Constants.ROOM_ID, roomId)
                .setParameter(Constants.SEMESTER_ID, semesterId)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteSchedulesBySemesterId(Long semesterId) {
        log.info("In deleteSchedulesBySemesterId with semesterId = {}", semesterId);
        sessionFactory.getCurrentSession().createQuery(
                        "delete from Schedule s " +
                                "where s.id in (select sch.id from Schedule sch where sch.lesson.semester.id = :semesterId)")
                .setParameter(Constants.SEMESTER_ID, semesterId).executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countInputLessonsInScheduleByLessonId(Long lessonId) {
        log.info("In countInputLessonsInScheduleByLessonId(lessonId = [{}])", lessonId);
        return (Long) sessionFactory.getCurrentSession().createQuery(
                        "select count (s.id) from  Schedule s where s.lesson.id = :lessonId " + NOT_DISABLED_SQL)
                .setParameter(Constants.LESSON_ID, lessonId)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countByLessonIdPeriodIdEvenOddDayOfWeek(Long lessonId, Long periodId, EvenOdd evenOdd, DayOfWeek day) {
        log.info("In countByLessonIdPeriodIdEvenOddDayOfWeek(lessonId = [{}], periodId = [{}], evenOdd = [{}], day = [{}])",
                lessonId, periodId, evenOdd, day);
        return (Long) sessionFactory.getCurrentSession().createQuery(
                        "select count (s.id) from Schedule s " +
                                "where s.lesson.id = :lessonId " +
                                "and s.period.id = :periodId " +
                                "and s.dayOfWeek =:dayOfWeek " +
                                "and (s.evenOdd =:evenOdd or s.evenOdd = 'WEEKLY')" + NOT_DISABLED_SQL)
                .setParameter(Constants.LESSON_ID, lessonId)
                .setParameter(Constants.PERIOD_ID, periodId)
                .setParameter(Constants.DAY_OF_WEEK, day)
                .setParameter(Constants.EVEN_ODD, evenOdd)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Schedule> getAllOrdered(Long semesterId) {
        log.debug("Entered getAllOrdered()");
        return sessionFactory.getCurrentSession()
                .createQuery(GET_ALL_ORDERED_BY_ROOMS_DAYS_PERIODS, Schedule.class)
                .setParameter(Constants.SEMESTER_ID, semesterId)
                .getResultList();
    }
}
