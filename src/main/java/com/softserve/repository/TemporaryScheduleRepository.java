package com.softserve.repository;

import com.softserve.entity.TemporarySchedule;

import java.time.LocalDate;
import java.util.List;

public interface TemporaryScheduleRepository extends BasicRepository<TemporarySchedule, Long> {

    /**
     * Counts the number of temporary schedule records in the database for given date and vacation in the given semester.
     *
     * @param date       the required date
     * @param semesterId the id of the semester
     * @param vacation   the boolean value represents vacation
     * @return the number of records in db
     */
    Long isExistTemporaryScheduleByVacationByDate(LocalDate date, Long semesterId, boolean vacation);

    /**
     * Counts the number of temporary schedule records in the database for given date and vacation in the given semester ignoring the specified id.
     *
     * @param id         the id of temporary schedule that will be ignored
     * @param date       the required date
     * @param semesterId the id of the semester
     * @param vacation   the boolean value represents vacation
     * @return the number of records in db
     */
    Long isExistTemporaryScheduleByVacationByDateWithIgnoreId(Long id, LocalDate date, Long semesterId, boolean vacation);

    Long isExistTemporaryScheduleByVacationByDateAndTeacher(LocalDate date, Long semesterId, Long teacherId, boolean vacation);

    /**
     * Counts the number of temporary schedule records in the database for given date,
     * teacher and vacation in the given semester ignoring the specified id.
     *
     * @param id         the id of temporary schedule that will be ignored
     * @param date       the required date
     * @param semesterId the id of the semester
     * @param teacherId  the id of the teacher
     * @param vacation   the boolean value represents vacation
     * @return the number of records in db
     */
    Long isExistTemporaryScheduleByVacationByDateAndTeacherWithIgnoreId(Long id, LocalDate date, Long semesterId, Long teacherId, boolean vacation);

    /**
     * Counts the number of temporary schedule records in the database that corresponds to the specified temporary schedule and given vacation.
     *
     * @param object   the temporary schedule
     * @param vacation the boolean value represents vacation
     * @return the number of records in db
     */
    Long isExistTemporarySchedule(TemporarySchedule object, boolean vacation);

    /**
     * Counts the number of temporary schedule records in the database that corresponds
     * to the specified date, schedule id, semester id and given vacation.
     *
     * @param object   the temporary schedule
     * @param vacation the boolean value represents vacation
     * @return the number of records in db
     */
    Long isExistTemporaryScheduleByDateAndScheduleId(TemporarySchedule object, boolean vacation);

    /**
     * Counts the number of temporary schedule records in the database that corresponds
     * to the specified temporary schedule and given vacation ignoring the specified id.
     *
     * @param object   the temporary schedule
     * @param vacation the boolean value represents vacation
     * @return the number of records in db
     */
    Long isExistTemporaryScheduleByDateAndScheduleIdWithIgnoreId(TemporarySchedule object, boolean vacation);

    /**
     * Counts the number of temporary schedule records in the database that corresponds
     * to the specified temporary schedule ignoring the id of given schedule.
     *
     * @param object the temporary schedule
     * @return the number of records in db
     */
    Long isExistTemporaryScheduleWithIgnoreId(TemporarySchedule object);

    /**
     * Returns all temporary schedules from database for a specific date range and with given teacher id.
     *
     * @param fromDate  the start of the date range
     * @param toDate    the end of the date range
     * @param teacherId the id of the teacher
     * @return the list of temporary schedules for a specific date range and with given teacher id
     */
    List<TemporarySchedule> temporaryScheduleByDateRangeForTeacher(LocalDate fromDate, LocalDate toDate, Long teacherId);

    /**
     * Returns all temporary schedules from database for a specific date range and with given semester id.
     *
     * @param semesterId the id of the semester
     * @param fromDate   the start of the date range
     * @param toDate     the end of the date range
     * @return the list of temporary schedules with given semester id for a specific date range
     */
    List<TemporarySchedule> getAllBySemesterAndRange(Long semesterId, LocalDate fromDate, LocalDate toDate);

    /**
     * Returns all temporary schedules with related schedules and lessons from database for a specific date range and with given teacher id.
     *
     * @param fromDate  the start of the date range
     * @param toDate    the end of the date range
     * @param teacherId the id of the teacher
     * @return the list of temporary schedules for a specific date range and with given teacher id
     */
    List<TemporarySchedule> getAllByTeacherAndRange(LocalDate fromDate, LocalDate toDate, Long teacherId);

    /**
     * Returns all temporary schedules from database with given semester id.
     *
     * @param semesterId the id of the semester
     * @return the list of temporary schedules with given semester id
     */
    List<TemporarySchedule> getAllBySemester(Long semesterId);

    /**
     * Returns all temporary schedules from database for a specific date range.
     *
     * @param fromDate the start of the date range
     * @param toDate   the end of the date range
     * @return the list of temporary schedules for a specific date range
     */
    List<TemporarySchedule> getAllByRange(LocalDate fromDate, LocalDate toDate);

    /**
     * Returns all temporary schedules with vacation from database for a specific date range.
     *
     * @param fromDate the start of the date range
     * @param toDate   the end of the date range
     * @return the list of temporary schedules with vacation for a specific date range
     */
    List<TemporarySchedule> vacationByDateRangeForTeacher(LocalDate fromDate, LocalDate toDate);

    /**
     * Deletes all temporary schedules from database in with given semester id.
     *
     * @param semesterId the id of the semester
     */
    void deleteTemporarySchedulesBySemesterId(Long semesterId);
}
