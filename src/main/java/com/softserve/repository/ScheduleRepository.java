package com.softserve.repository;

import com.softserve.entity.*;
import com.softserve.entity.enums.EvenOdd;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends BasicRepository<Schedule, Long> {

    /**
     * Counts the number of conflicts for particular group in schedule.
     * if there are any saved records in schedule for particular group.
     *
     * @param semesterId the id of the semester for which the search is performed
     * @param dayOfWeek  the day of the week for which the search is performed
     * @param evenOdd    the type of the week for which the search is performed
     * @param classId    the class id for which the search is performed
     * @param groupId    the group id for which the search is performed
     * @return the number conflicts for particular group in schedule
     */
    Long conflictForGroupInSchedule(Long semesterId, DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId, Long groupId);

    /**
     * Counts the number of conflicts for particular teacher in schedule.
     * Method searches if teacher has schedule at some semester at some day(by even odd week) for some period.
     *
     * @param semesterId the id of the semester for which the search is performed
     * @param dayOfWeek  the day of the week for which the search is performed
     * @param evenOdd    the type of the week for which the search is performed
     * @param classId    the class id for which the search is performed
     * @param teacherId  the id of the teacher for which the search is performed
     * @return the number of conflicts for particular teacher in schedule
     */
    Long conflictForTeacherInSchedule(Long semesterId, DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId, Long teacherId);

    /**
     * Returns a list of unique groups in semester.
     *
     * @param semesterId the id of the semester
     * @return the list of unique groups in semester
     */
    List<Group> uniqueGroupsInScheduleBySemester(Long semesterId);

    /**
     * Returns a list of periods for group in some semester at some day.
     *
     * @param semesterId the id of the semester
     * @param groupId    the id of the group
     * @param day        the day of the week
     * @return the list of periods
     */
    List<Period> periodsForGroupByDayBySemester(Long semesterId, Long groupId, DayOfWeek day);

    /**
     * Retrieves a lesson for group in given semester at given day(even/odd) and period.
     *
     * @param semesterId the id of the semester
     * @param groupId    the id of the group
     * @param periodId   the id of the period
     * @param day        the day of the week
     * @param evenOdd    the type of the week
     * @return an Optional describing the lesson
     */
    Optional<Lesson> lessonForGroupByDayBySemesterByPeriodByWeek(Long semesterId, Long groupId, Long periodId, DayOfWeek day, EvenOdd evenOdd);

    /**
     * Retrieves a room for lesson in given semester at given day(even/odd) and at given period.
     *
     * @param semesterId the id of the semester
     * @param periodId   the id of the period
     * @param lessonId   the id of the lesson
     * @param day        the day of the week
     * @param evenOdd    the type of the week
     * @return an Optional describing the room
     */
    Room getRoomForLesson(Long semesterId, Long periodId, Long lessonId, DayOfWeek day, EvenOdd evenOdd);

    /**
     * Returns unique days when group have classes in given semester.
     *
     * @param semesterId the id of the semester
     * @param groupId    the id of the group
     * @return the list of days
     */
    List<DayOfWeek> getDaysWhenGroupHasClassesBySemester(Long semesterId, Long groupId);

    /**
     * Counts the number of schedule records in database for group in the semester.
     *
     * @param semesterId the id of the semester
     * @param groupId    the id og the group
     * @return the number of records in database
     */
    Long countSchedulesForGroupInSemester(Long semesterId, Long groupId);

    /**
     * Returns unique days when teacher has classes in given semester.
     *
     * @param semesterId the id of the semester
     * @param teacherId  the id of the teacher
     * @return the list of days
     */
    List<DayOfWeek> getDaysWhenTeacherHasClassesBySemester(Long semesterId, Long teacherId);

    /**
     * Returns periods for teacher in given semester at given day.
     *
     * @param semesterId the id of the semester
     * @param teacherId  the id of the teacher
     * @param day        the day of the week
     * @param evenOdd    the type of the week
     * @return the list of periods
     */
    List<Period> periodsForTeacherBySemesterByDayByWeek(Long semesterId, Long teacherId, DayOfWeek day, EvenOdd evenOdd);

    /**
     * Returns lessons for teacher in given semester at given day(even/odd) and at given period.
     *
     * @param semesterId the id of the semester
     * @param teacherId  the id of the teacher
     * @param periodId   the id of the period
     * @param day        the day of the week
     * @param evenOdd    the type of the week
     * @return the list of lessons
     */
    List<Lesson> lessonsForTeacherBySemesterByDayByPeriodByWeek(Long semesterId, Long teacherId, Long periodId, DayOfWeek day, EvenOdd evenOdd);

    /**
     * Returns all schedules for given teacher in given semester.
     *
     * @param teacherId  the id of the teacher
     * @param semesterId the id of the semester
     * @return the list of schedules
     */
    List<Schedule> getAllSchedulesByTeacherIdAndSemesterId(Long teacherId, Long semesterId);

    /**
     * Retrieves a schedule by given schedule with all schedule parameters.
     *
     * @param schedule the schedule with parameters
     * @return the schedule with all schedule parameters
     */
    Schedule getScheduleByObject(Schedule schedule);

    /**
     * Returns all schedules from db in particular semester.
     *
     * @param semesterId the id of the semester
     * @return the list of schedules
     */
    List<Schedule> getScheduleBySemester(Long semesterId);

    /**
     * Returns all schedules for given room and semester.
     *
     * @param semesterId the id of the semester
     * @param roomId     the id of the room
     * @return the list of schedules
     */
    List<Schedule> scheduleForRoomBySemester(Long semesterId, Long roomId);

    /**
     * Returns all schedules from db in particular date range.
     *
     * @param fromDate  the start of the date range
     * @param toDate    the end of the date range
     * @param teacherId the id of the teacher
     * @return the list of schedules
     */
    List<Schedule> scheduleByDateRangeForTeacher(LocalDate fromDate, LocalDate toDate, Long teacherId);

    /**
     * Deletes all schedules from db with given semester id.
     *
     * @param semesterId the id of the semester
     */
    void deleteSchedulesBySemesterId(Long semesterId);

    /**
     * Counts the number of schedule records in the database with given lessons id.
     *
     * @param lessonId the id of the lesson
     * @return the number of records in db
     */
    Long countInputLessonsInScheduleByLessonId(Long lessonId);

    /**
     * Counts the number of schedule records in the database with given lesson id, period id, type of the week and day of the week.
     *
     * @param lessonId the id of the lesson
     * @param periodId the id of the Period
     * @param evenOdd  the type of the week
     * @param day      the day of the week
     * @return the number of records in db
     */
    Long countByLessonIdPeriodIdEvenOddDayOfWeek(Long lessonId, Long periodId, EvenOdd evenOdd, DayOfWeek day);

    /**
     * Returns all schedules for given semester in ordered form.
     *
     * @param semesterId the id of the semester
     * @return the list of schedules
     */
    List<Schedule> getAllOrdered(Long semesterId);
}
