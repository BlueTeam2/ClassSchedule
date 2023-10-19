package com.softserve.repository.impl;

import com.softserve.entity.TemporarySchedule;
import com.softserve.repository.TemporaryScheduleRepository;
import com.softserve.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@Slf4j
public class TemporaryScheduleRepositoryImpl extends BasicRepositoryImpl<TemporarySchedule, Long> implements TemporaryScheduleRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    public Long isExistTemporaryScheduleByVacationByDate(LocalDate date, Long semesterId, boolean vacation) {
        log.info("In isExistTemporaryScheduleByVacationByDate(semesterId = [{}], date = [{}], vacation = [{}])", semesterId, date, vacation);
        return (Long) sessionFactory.getCurrentSession().createQuery(
                        "select count (t.id) from TemporarySchedule t " +
                                "where t.date = :date " +
                                "and t.vacation = :vacation " +
                                "and t.semester.id = :semesterId " +
                                "and t.scheduleId = null " +
                                "and t.teacher = null " +
                                "and t.period = null")
                .setParameter(Constants.DATE, date)
                .setParameter(Constants.SEMESTER_ID, semesterId)
                .setParameter(Constants.VACATION, vacation)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long isExistTemporaryScheduleByVacationByDateWithIgnoreId(Long id, LocalDate date, Long semesterId, boolean vacation) {
        log.info("In isExistTemporaryScheduleByVacationByDateWithIgnoreId(semesterId = [{}], date = [{}])", semesterId, date);
        return (Long) sessionFactory.getCurrentSession().createQuery("select count (t.id) from  TemporarySchedule t " +
                        "where  t.date = :date and t.vacation = :vacation and t.semester.id = :semesterId and t.id!= :id and t.period = null " +
                        "and t.teacher = null  and t.scheduleId = null ")
                .setParameter(Constants.DATE, date)
                .setParameter(Constants.SEMESTER_ID, semesterId)
                .setParameter(Constants.ID, id)
                .setParameter(Constants.VACATION, vacation)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long isExistTemporaryScheduleByVacationByDateAndTeacher(LocalDate date, Long semesterId, Long teacherId, boolean vacation) {
        log.info("In isExistVacationByDate(semesterId = [{}], date = [{}])", semesterId, date);
        return (Long) sessionFactory.getCurrentSession().createQuery(
                        "select count (t.id) from TemporarySchedule t " +
                                "where t.date = :date " +
                                "and t.vacation = :vacation " +
                                "and t.semester.id = :semesterId " +
                                "and t.scheduleId = null " +
                                "and t.period.id = null")
                .setParameter(Constants.DATE, date)
                .setParameter(Constants.SEMESTER_ID, semesterId)
                .setParameter(Constants.VACATION, vacation)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long isExistTemporaryScheduleByVacationByDateAndTeacherWithIgnoreId(Long id, LocalDate date, Long semesterId, Long teacherId,
                                                                               boolean vacation) {
        log.info("In isExistVacationByDate(semesterId = [{}], date = [{}])", semesterId, date);
        return (Long) sessionFactory.getCurrentSession().createQuery("select count (t.id) from  TemporarySchedule t" +
                        " where  t.date = :date and t.vacation =  :vacation and t.semester.id = :semesterId and t.id!= :id  " +
                        "and t.scheduleId = null  and t.period.id = null")
                .setParameter(Constants.DATE, date)
                .setParameter(Constants.SEMESTER_ID, semesterId)
                .setParameter(Constants.ID, id)
                .setParameter(Constants.VACATION, vacation)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long isExistTemporarySchedule(TemporarySchedule object, boolean vacation) {
        log.info("In isExistTemporarySchedule(object = [{}]", object);
        return (Long) sessionFactory.getCurrentSession().createQuery(
                        "select count (s.id) from TemporarySchedule s " +
                                "where s.date = :date " +
                                "and s.vacation = false " +
                                "and s.room.id = :roomId " +
                                "and s.group.id = :groupId " +
                                "and s.period.id = :periodId " +
                                "and s.subject.id = :subjectId " +
                                "and s.scheduleId = :scheduleId " +
                                "and s.lessonType = :lessonType " +
                                "and s.semester.id = :semesterId " +
                                "and s.vacation = :vacation")
                .setParameter(Constants.DATE, object.getDate())
                .setParameter(Constants.ROOM_ID, object.getRoom().getId())
                .setParameter(Constants.GROUP_ID, object.getGroup().getId())
                .setParameter(Constants.PERIOD_ID, object.getPeriod().getId())
                .setParameter(Constants.SUBJECT_ID, object.getSubject().getId())
                .setParameter(Constants.SCHEDULE_ID, object.getScheduleId())
                .setParameter(Constants.LESSON_TYPE, object.getLessonType())
                .setParameter(Constants.SEMESTER_ID, object.getSemester().getId())
                .setParameter(Constants.VACATION, vacation)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long isExistTemporaryScheduleByDateAndScheduleId(TemporarySchedule object, boolean vacation) {
        log.info("In isExistTemporarySchedule(object = [{}]", object);
        return (Long) sessionFactory.getCurrentSession().createQuery(
                        "select count (t.id) from TemporarySchedule t " +
                                "where t.date = :date " +
                                "and t.vacation = false " +
                                "and t.scheduleId = :scheduleId " +
                                "and t.semester.id = :semesterId " +
                                "and t.vacation = :vacation")
                .setParameter(Constants.DATE, object.getDate())
                .setParameter(Constants.SCHEDULE_ID, object.getScheduleId())
                .setParameter(Constants.SEMESTER_ID, object.getSemester().getId())
                .setParameter(Constants.VACATION, vacation)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long isExistTemporaryScheduleByDateAndScheduleIdWithIgnoreId(TemporarySchedule object, boolean vacation) {
        log.info("In isExistTemporarySchedule(object = [{}]", object);
        return (Long) sessionFactory.getCurrentSession().createQuery(
                        "select count (t.id) from TemporarySchedule t " +
                                "where t.date = :date " +
                                "and t.vacation = false " +
                                "and t.scheduleId = :scheduleId " +
                                "and t.semester.id = :semesterId " +
                                "and t.vacation = :vacation " +
                                "and t.id != :id")
                .setParameter(Constants.DATE, object.getDate())
                .setParameter(Constants.SCHEDULE_ID, object.getScheduleId())
                .setParameter(Constants.SEMESTER_ID, object.getSemester().getId())
                .setParameter(Constants.ID, object.getId())
                .setParameter(Constants.VACATION, vacation)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long isExistTemporaryScheduleWithIgnoreId(TemporarySchedule object) {
        log.info("In isExistTemporarySchedule(object = [{}]", object);
        return (Long) sessionFactory.getCurrentSession().createQuery(
                        "select count (s.id) from TemporarySchedule s " +
                                "where s.date = :date " +
                                "and s.vacation = false " +
                                "and s.room.id = :roomId " +
                                "and s.group.id = :groupId " +
                                "and s.period.id = :periodId " +
                                "and s.subject.id = :subjectId " +
                                "and s.scheduleId = :scheduleId " +
                                "and s.lessonType = :lessonType " +
                                "and s.semester.id = :semesterId " +
                                "and s.id != :id")
                .setParameter(Constants.DATE, object.getDate())
                .setParameter(Constants.ROOM_ID, object.getRoom().getId())
                .setParameter(Constants.GROUP_ID, object.getGroup().getId())
                .setParameter(Constants.PERIOD_ID, object.getPeriod().getId())
                .setParameter(Constants.SUBJECT_ID, object.getSubject().getId())
                .setParameter(Constants.SCHEDULE_ID, object.getScheduleId())
                .setParameter(Constants.LESSON_TYPE, object.getLessonType())
                .setParameter(Constants.SEMESTER_ID, object.getSemester().getId())
                .setParameter(Constants.ID, object.getId())
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TemporarySchedule> temporaryScheduleByDateRangeForTeacher(LocalDate fromDate, LocalDate toDate, Long teacherId) {
        log.info("In temporaryScheduleByDateRangeForTeacher with fromDate = {} and toDate = {} and teacherId = {}", fromDate, toDate, teacherId);
        return sessionFactory.getCurrentSession().createQuery("SELECT t from TemporarySchedule t " +
                        "where t.date <= :toDate  and t.date >= :fromDate and t.teacher.id = :teacherId  and t.vacation=false  ORDER BY t.date asc ")
                .setParameter(Constants.FROM_DATE, fromDate)
                .setParameter(Constants.TO_DATE, toDate)
                .setParameter(Constants.TEACHER_ID, teacherId)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TemporarySchedule> getAllByTeacherAndRange(LocalDate fromDate, LocalDate toDate, Long teacherId) {
        log.info("In getAllByTeacherAndRange(teacherId = [{}], fromDate = [{}], toDate = [{}]", teacherId, fromDate, teacherId);
        return sessionFactory.getCurrentSession().createQuery(
                        "SELECT t from TemporarySchedule t " +
                                "join Schedule s on t.scheduleId = s.id " +
                                "join Lesson l on s.lesson.id = l.id " +
                                "where t.date <= :toDate " +
                                "and t.date >= :fromDate " +
                                "and (t.teacher.id = :teacherId or l.teacher.id = :teacherId ) ORDER BY t.date asc ")
                .setParameter(Constants.FROM_DATE, fromDate)
                .setParameter(Constants.TO_DATE, toDate)
                .setParameter(Constants.TEACHER_ID, teacherId)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TemporarySchedule> getAllBySemester(Long semesterId) {
        log.info("In getAllBySemester(semesterId = [{}]", semesterId);
        return sessionFactory.getCurrentSession().createQuery("SELECT t from TemporarySchedule t " +
                        "where t.semester.id = :semesterId ORDER BY t.date asc ")
                .setParameter(Constants.SEMESTER_ID, semesterId)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TemporarySchedule> getAllByRange(LocalDate fromDate, LocalDate toDate) {
        log.info("In getAllByRange");
        return sessionFactory.getCurrentSession().createQuery("SELECT t from TemporarySchedule t " +
                        "where t.date <= :toDate  and t.date >= :fromDate  ORDER BY t.date asc ")
                .setParameter(Constants.FROM_DATE, fromDate)
                .setParameter(Constants.TO_DATE, toDate)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TemporarySchedule> getAllBySemesterAndRange(Long semesterId, LocalDate fromDate, LocalDate toDate) {
        log.info("In getAllBySemesterAndRange");
        return sessionFactory.getCurrentSession().createQuery("SELECT t from TemporarySchedule t " +
                        "where t.date <= :toDate  and t.date >= :fromDate and t.semester.id = :semesterId  ORDER BY t.date asc ")
                .setParameter(Constants.FROM_DATE, fromDate)
                .setParameter(Constants.TO_DATE, toDate)
                .setParameter(Constants.SEMESTER_ID, semesterId)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TemporarySchedule> vacationByDateRangeForTeacher(LocalDate fromDate, LocalDate toDate) {
        log.info("In vacationByDateRangeForTeacher");
        return sessionFactory.getCurrentSession().createQuery("SELECT t from TemporarySchedule t " +
                        "where t.date <= :toDate  and t.date >= :fromDate and t.vacation = true  ORDER BY t.date asc ")
                .setParameter(Constants.FROM_DATE, fromDate)
                .setParameter(Constants.TO_DATE, toDate)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTemporarySchedulesBySemesterId(Long semesterId) {
        log.info("In deleteTemporarySchedulesBySemesterId with semesterId = {}", semesterId);
        sessionFactory.getCurrentSession().createQuery(
                        "delete from TemporarySchedule t " +
                                "where t.id in (select temp.id from TemporarySchedule temp where temp.semester.id = :semesterId)")
                .setParameter(Constants.SEMESTER_ID, semesterId).executeUpdate();
    }
}
