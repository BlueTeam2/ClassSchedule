package com.softserve.service;

import com.softserve.entity.Teacher;
import com.softserve.entity.TemporarySchedule;
import com.softserve.entity.enums.EvenOdd;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TemporaryScheduleService extends BasicService<TemporarySchedule, Long> {

    /**
     * Returns all temporary schedules with related schedules and lessons from the repository for a specific date range and with given teacher id.
     *
     * @param teacherId the id of the teacher
     * @param fromDate  the start of the date range
     * @param toDate    the end of the date range
     * @return the list of temporary schedules
     */
    List<TemporarySchedule> getAllByTeacherAndRange(LocalDate fromDate, LocalDate toDate, Long teacherId);

    /**
     * Returns all temporary schedules from the repository for a specific date range and with given teacher id.
     *
     * @param fromDate  the start of the date range
     * @param toDate    the end of the date range
     * @param teacherId the id of the teacher
     * @return the list of temporary schedules for a specific date range and with given teacher id
     */
    List<TemporarySchedule> getTemporaryScheduleByTeacherAndRange(LocalDate fromDate, LocalDate toDate, Long teacherId);

    /**
     * Returns all temporary schedules from the repository with a semester id equal to the current one.
     *
     * @return the list of temporary schedules for current semester
     */
    List<TemporarySchedule> getAllByCurrentSemester();

    /**
     * Returns all temporary schedules from the repository with given semester id.
     *
     * @param semesterId the id of the semester
     * @return the list of temporary schedules with given semester id.
     */
    List<TemporarySchedule> getAllBySemesterId(Long semesterId);

    /**
     * Returns all temporary schedules from the repository for a specific date range.
     *
     * @param fromDate the start of the date range
     * @param toDate   the end of the date range
     * @return the list of temporary schedules for a specific date range
     */
    List<TemporarySchedule> getAllByRange(LocalDate fromDate, LocalDate toDate);

    /**
     * Returns all temporary schedules with given semester id for a specific date range.
     *
     * @param semesterId the id of the semester
     * @param fromDate   the start of the date range
     * @param toDate     the end of the date range
     * @return the list of temporary schedules with given semester id for a specific date range
     */
    List<TemporarySchedule> getAllBySemesterAndRange(Long semesterId, LocalDate fromDate, LocalDate toDate);

    /**
     * Returns all temporary schedules with vacation for a specific date range.
     *
     * @param fromDate the start of the date range
     * @param toDate   the end of the date range
     * @return the list of  temporary schedules with vacation for a specific date range
     */
    List<TemporarySchedule> vacationByDateRange(LocalDate fromDate, LocalDate toDate);

    /**
     * Saves the given temporary schedule in the repository in a specific date range.
     *
     * @param from   the start of the date range
     * @param to     the end of the date range
     * @param object temporary schedule
     * @return the list of messages about occurred errors while saving the temporary schedule
     */
    List<String> addRange(LocalDate from, LocalDate to, TemporarySchedule object);

    /**
     * Deletes all temporary schedule from the repository with given semester id.
     *
     * @param semesterId the id of the semester
     */
    void deleteTemporarySchedulesBySemesterId(Long semesterId);

    /**
     * Returns teacher's email.
     *
     * @param teacher the teacher entity
     * @return the string represents email of the given teacher. Returns {@code null} if teacher equals to {@code null}
     */
    String getTeacherEmailFromTemporarySchedule(Teacher teacher);

    /**
     * Returns teacher entity by the given schedule id.
     *
     * @param scheduleId the id of the schedule
     * @return teacher entity by the given schedule id or {@code null} if the schedule id equal to {@code null}
     */
    Teacher getTeacherByScheduleId(Long scheduleId);

    /**
     * Returns the lists of temporary schedules grouped into map by the days of the week and then grouped into map by type of the week.
     *
     * @param semesterId the id of the semester
     * @return the lists of temporary schedules grouped into map by the days of the week and then grouped into map by type of the week
     */
    Map<EvenOdd, Map<DayOfWeek, List<TemporarySchedule>>> getTemporaryScheduleForEvenOddWeeks(Long semesterId);
}

