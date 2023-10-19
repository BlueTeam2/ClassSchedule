package com.softserve.service;

import com.softserve.dto.CreateScheduleInfoDTO;
import com.softserve.dto.ScheduleForGroupDTO;
import com.softserve.dto.ScheduleForTeacherDTO;
import com.softserve.dto.ScheduleFullDTO;
import com.softserve.entity.Room;
import com.softserve.entity.Schedule;
import com.softserve.entity.enums.EvenOdd;

import javax.mail.MessagingException;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface ScheduleService extends BasicService<Schedule, Long> {

    /**
     * Creates a list of schedules in accordance to grouped lessons.
     *
     * @param schedule the schedule with grouped lessons
     * @return the list of the schedules for grouped lessons
     */
    List<Schedule> schedulesForGroupedLessons(Schedule schedule);

    /**
     * Returns all schedules for grouped lessons from the repository.
     *
     * @param schedule the schedule
     * @return the list of the schedules
     */
    List<Schedule> getSchedulesForGroupedLessons(Schedule schedule);

    /**
     * Checks if lessons with this group title already exists and if schedule item for group already exists.
     *
     * @param schedule the schedule
     * @throws com.softserve.exception.EntityAlreadyExistsException if lessons with this group title already exists
     * @throws com.softserve.exception.ScheduleConflictException    if schedule for group already exists
     */
    void checkReferences(Schedule schedule);

    /**
     * Returns necessary info to finish saving schedule.
     *
     * @param semesterId the id of the semester
     * @param dayOfWeek  the day of the week
     * @param evenOdd    the type of the week
     * @param classId    the id of the class
     * @param lessonId   the id of the lesson
     * @return the necessary info to finish saving schedule
     * @throws com.softserve.exception.ScheduleConflictException if schedule for group already exists
     */
    CreateScheduleInfoDTO getInfoForCreatingSchedule(Long semesterId, DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId, Long lessonId);

    /**
     * Checks if group has conflict in schedule when it saves.
     *
     * @param semesterId the id of the semester
     * @param dayOfWeek  the day of the week
     * @param evenOdd    the type of the week
     * @param classId    the id of the class
     * @param lessonId   the id of the lesson
     * @return {@code true} if group has conflict in schedule when it saves
     */
    boolean isConflictForGroupInSchedule(Long semesterId, DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId, Long lessonId);

    /**
     * Returns full schedule for specified group in given semester.
     *
     * @param semesterId the id of the semester
     * @param groupId    the id of the group
     * @return the filled schedule for specified group in given semester
     */
    List<ScheduleForGroupDTO> getFullScheduleForGroup(Long semesterId, Long groupId);

    /**
     * Returns full schedule in specified semester.
     *
     * @param semesterId the id of the semester
     * @return the filled schedule for all groups that have any lessons in specified semester
     */
    ScheduleFullDTO getFullScheduleForSemester(Long semesterId);

    /**
     * Returns full schedule for teacher in specified semester.
     *
     * @param semesterId the id of the semester
     * @param teacherId  the id of the teacher
     * @return the filled schedule for teacher in specified semester
     */
    ScheduleForTeacherDTO getScheduleForTeacher(Long semesterId, Long teacherId);

    /**
     * Returns all schedules with given teacher id and semester id.
     *
     * @param teacherId  the id of the teacher
     * @param semesterId the id of the semester
     * @return the list of the schedules with given teacher id and semester id
     */
    List<Schedule> getAllSchedulesByTeacherIdAndSemesterId(Long teacherId, Long semesterId);

    /**
     * Returns all schedules with given semester id.
     *
     * @param semesterId the id of the semester
     * @return the list of the schedules with given semester id
     */
    List<Schedule> getSchedulesBySemester(Long semesterId);

    /**
     * Deletes all schedules from the repository in with given semester id.
     *
     * @param semesterId the id of the semester to delete
     */
    void deleteSchedulesBySemesterId(Long semesterId);

    /**
     * Saves the given schedule in the repository.
     *
     * @param schedule the schedule to save in the repository
     * @return the saved schedule
     */
    Schedule saveScheduleDuringCopy(Schedule schedule);

    /**
     * Updates the given schedule in the repository without checks.
     *
     * @param schedule the schedule
     * @return the updated schedule
     */
    Schedule updateWithoutChecks(Schedule schedule);

    /**
     * Counts the number of schedule records in the repository with given lesson id.
     *
     * @param lessonId the id of the lesson
     * @return the number of records in the repository
     */
    Long countInputLessonsInScheduleByLessonId(Long lessonId);

    /**
     * Checks if given lesson exist in schedule with given period id, type of the week and day of the week.
     *
     * @param lessonId the id of the lesson
     * @param periodId the id of the period
     * @param evenOdd  the type of the week
     * @param day      the day of the week
     * @return {@code true} if given lesson exist in schedule with given period id, type of the week and day of the week
     */
    boolean isLessonInScheduleByLessonIdPeriodIdEvenOddDayOfWeek(Long lessonId, Long periodId, EvenOdd evenOdd, DayOfWeek day);

    /**
     * Sends the schedules to the teachers.
     *
     * @param semesterId the id of the semester
     * @param teachersId the array of ids of teachers to whom the schedule will be sent
     * @param language   the locale of the message
     * @throws com.softserve.exception.MessageNotSendException if message with the schedule not send
     */
    void sendScheduleToTeachers(Long semesterId, Long[] teachersId, Locale language);

    /**
     * Sends the schedules to one teacher.
     *
     * @param semesterId the id of the semester
     * @param teacherId  the id of teacher to whom the schedule should be sent
     * @param language   the locale of the message
     * @throws MessagingException if an error occurred while sending the letter
     */
    void sendScheduleToTeacher(Long semesterId, Long teacherId, Locale language) throws MessagingException;

    /**
     * Returns lists of schedules grouped by rooms.
     *
     * @param semesterId the id of the semester
     * @return the lists of schedules grouped by rooms
     */
    Map<Room, List<Schedule>> getAllOrdered(Long semesterId);
}

