package com.softserve.repository;

import com.softserve.entity.Period;
import com.softserve.entity.Semester;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface SemesterRepository extends BasicRepository<Semester, Long> {

    /**
     * Count the number of semesters with given description and year.
     *
     * @param description the string represents the description
     * @param year        the searched year
     * @return the number of semesters
     */
    Long countSemesterDuplicatesByDescriptionAndYear(String description, int year);

    /**
     * Retrieves semester with a value of currentSemester equal to {@code true}.
     *
     * @return an Optional describing the semester, otherwise - empty Optional
     */
    Optional<Semester> getCurrentSemester();

    /**
     * Returns a semester with given description and year.
     *
     * @param description the string represents the description
     * @param year        the searched year
     * @return an Optional describing the semester if such exists, otherwise - empty Optional
     */
    Optional<Semester> getSemesterByDescriptionAndYear(String description, int year);

    /**
     * {@inheritDoc}
     */
    List<Semester> getDisabled();

    /**
     * Sets the field currentSemester to {@code false} for all entities which have it {@code true}.
     *
     * @return the number of updated rows
     */
    int updateAllSemesterCurrentToFalse();

    /**
     * Sets the field currentSemester to {@code true} for semester with given id.
     *
     * @param semesterId the id of the semester
     * @return the number of updated rows
     */
    int setCurrentSemester(Long semesterId);

    /**
     * Returns all semesters with a value of defaultSemester equal to {@code true}.
     *
     * @return Optional with Semester if such exist, else return empty Optional
     */
    Optional<Semester> getDefaultSemester();

    /**
     * Sets the field defaultSemester equal to false for all semesters which have it equal to {@code true}.
     *
     * @return the number of updated rows
     */
    int updateAllSemesterDefaultToFalse();

    /**
     * Sets the field defaultSemester equal to {@code true} for semester with given id.
     *
     * @param semesterId the id of the semester
     * @return the number of updated rows
     */
    int setDefaultSemester(Long semesterId);

    /**
     * Returns all unique days with lessons in the given semester.
     *
     * @param semesterId the id of the semester
     * @return the list of days
     */
    List<DayOfWeek> getDaysWithLessonsBySemesterId(Long semesterId);

    /**
     * Returns all periods with lessons in the given semester.
     *
     * @param semesterId the id of the semester
     * @return the list of periods
     */
    List<Period> getPeriodsWithLessonsBySemesterId(Long semesterId);
}
