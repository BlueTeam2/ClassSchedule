package com.softserve.service;

import com.softserve.entity.Group;
import com.softserve.entity.Period;
import com.softserve.entity.Semester;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public interface SemesterService extends BasicService<Semester, Long> {

    /**
     * Returns the semester with field currentSemester equal to true.
     *
     * @return the semester if such exist, otherwise throw exception
     * @throws com.softserve.exception.ScheduleConflictException if current semester for managers work isn't specified
     */
    Semester getCurrentSemester();

    /**
     * Returns all disabled semesters.
     *
     * @return list of disabled semesters
     */
    List<Semester> getDisabled();

    /**
     * Changes the current semester that the Manager is working on.
     *
     * @param semesterId the id of the semester that needs to be set as current
     * @return the current semester
     */
    Semester changeCurrentSemester(Long semesterId);

    /**
     * Returns semester with field defaultSemester equal to true.
     *
     * @return the default semester if such exist, otherwise throw exception
     * @throws com.softserve.exception.ScheduleConflictException if default semester isn't specified
     */
    Semester getDefaultSemester();

    /**
     * Changes the default semester that the Manager is working on.
     *
     * @param semesterId the id of the semester that needs to be set as default
     * @return the default semester
     */
    Semester changeDefaultSemester(Long semesterId);

    /**
     * Adds given group to the specified semester.
     *
     * @param semester the semester in which the group will be added
     * @param group    the group
     * @return the changed Semester
     */
    Semester addGroupToSemester(Semester semester, Group group);

    /**
     * Adds a list of the groups to the specified semester.
     *
     * @param semester the semester in which the groups will be added
     * @param groupIds the list of the groups
     * @return the changed semester
     */
    Semester addGroupsToSemester(Semester semester, List<Long> groupIds);

    /**
     * Adds a list of days of the week to the specified semester.
     *
     * @param semester   the semester in which a list of days of the week will be added
     * @param daysOfWeek the list of days of the week
     * @return the changed semester
     */
    Semester addDaysOfWeekToSemester(Semester semester, Set<DayOfWeek> daysOfWeek);

    /**
     * Adds a list of the periods to the specified semester.
     *
     * @param semester the semester in which a list of periods will be added
     * @param periods  the list of periods
     * @return the changed semester
     */
    Semester addPeriodsToSemester(Semester semester, Set<Period> periods);

    /**
     * Deletes the given group from the specified semester.
     *
     * @param semester the semester in which the given group will be deleted
     * @param group    the group to delete
     * @return the changed semester
     */
    Semester deleteGroupFromSemester(Semester semester, Group group);

    /**
     * Deletes groups, periods and days Of the weeks from the specified semester.
     *
     * @param semester the semester in which the groups, periods and days Of the weeks will be deleted
     * @return the changed semester
     */
    Semester deleteAllContentFromSemester(Semester semester);

    /**
     * Deletes groups from the specified semester.
     *
     * @param semester the semester in which given groups will be deleted
     * @param groups   the groups to delete
     * @return the changed semester
     */
    Semester deleteGroupsFromSemester(Semester semester, List<Group> groups);

    /**
     * Copies groups, periods, days of weeks and schedule from one to another semester.
     *
     * @param fromSemesterId the id of the semester from which groups, periods, days Of weeks and schedule will be copied
     * @param toSemesterId   the id semester in which groups, periods, days of weeks and schedule will be copied
     * @return the copied semester
     */
    Semester copySemester(Long fromSemesterId, Long toSemesterId);
}
