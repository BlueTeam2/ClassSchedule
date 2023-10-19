package com.softserve.service;

import com.softserve.entity.Lesson;
import com.softserve.entity.Semester;
import com.softserve.entity.enums.LessonType;

import java.util.List;

public interface LessonService extends BasicService<Lesson, Long> {

    /**
     * Saves new lessons from the given list of the lessons in the repository.
     * Saves only lessons that did not exist in the repository.
     *
     * @param lessons the list of the lessons
     * @return the list of the saved lessons
     */
    List<Lesson> save(List<Lesson> lessons);

    /**
     * Returns all lessons with the given group id from the repository.
     *
     * @param groupId the id of the group
     * @return the list of the lessons with the given group id
     */
    List<Lesson> getAllForGroup(Long groupId);

    /**
     * Returns all lessons with the given teacher id from the repository.
     *
     * @param teacherId the id of the teacher
     * @return the list of the lessons with the given teacher id
     */
    List<Lesson> getLessonByTeacher(Long teacherId);

    /**
     * Returns all types of lessons defined in the enum LessonType.
     *
     * @return the list of the lesson types
     */
    List<LessonType> getAllLessonTypes();

    /**
     * Checks if the given lesson already exists in the repository.
     *
     * @param lesson the lesson entity that needs to be checked
     * @return {@code true} if the given lesson already exists
     */
    boolean isLessonForGroupExists(Lesson lesson);

    /**
     * Checks if the given lesson already exists in the repository ignoring the id of given lesson.
     *
     * @param lesson the lesson entity that needs to be checked
     * @return {@code true} if the given lesson already exists in the repository
     */
    boolean isLessonForGroupExistsAndIgnoreWithId(Lesson lesson);

    /**
     * Returns all lessons from the repository with given semester id.
     *
     * @param semesterId the id of the semester
     * @return the list of lessons with the given semester id
     */
    List<Lesson> getLessonsBySemester(Long semesterId);

    /**
     * Saves lessons in the repository by copy from one semester to another.
     *
     * @param lessons    the list of the lessons from the previous semester
     * @param toSemester the semester where the lessons will be saved
     * @return the list of saved lessons
     */
    List<Lesson> copyLessonsFromOneToAnotherSemester(List<Lesson> lessons, Semester toSemester);

    /**
     * Saves lessons in the repository.
     *
     * @param lesson the lesson
     * @return the saved lesson
     */
    Lesson saveLessonDuringCopy(Lesson lesson);

    /**
     * Deletes lessons with the given semester id from the repository.
     *
     * @param semesterId the id of the semester
     */
    void deleteLessonBySemesterId(Long semesterId);

    /**
     * Returns all lessons from the repository by subject id, teacher id, semester id and lessonType and exclude given lesson id.
     *
     * @param lesson the lesson object for getting lessons from repository by its params
     * @return the list of lessons
     */
    List<Lesson> getLessonsBySubjectIdTeacherIdSemesterIdLessonTypeAndExcludeCurrentLessonId(Lesson lesson);

    /**
     * Returns all lessons which are grouped with given lesson.
     *
     * @param lesson the lesson object
     * @return the list of lessons which are grouped with given lesson
     */
    List<Lesson> getAllGroupedLessonsByLesson(Lesson lesson);

    /**
     * Updates links to meeting for lessons.
     *
     * @param lesson the lesson with a new link to the meeting
     * @return the number of links that was updated
     */
    Integer updateLinkToMeeting(Lesson lesson);
}
