package com.softserve.repository;

import com.softserve.entity.Lesson;

import java.util.List;

public interface LessonRepository extends BasicRepository<Lesson, Long> {

    /**
     * Returns the list of lessons with the given group id and semester id from the  database.
     *
     * @param groupId    the id of the group for which need to find all lessons
     * @param semesterId the id of the semester for which need to find all lessons
     * @return the list of filtered lessons
     */
    List<Lesson> getAllForGroup(Long groupId, Long semesterId);

    /**
     * Returns the list of lessons with given teacher id and semester id from the database.
     *
     * @param teacherId  the id of the teacher
     * @param semesterId the id of the semester
     * @return the list of lessons
     */
    List<Lesson> getLessonByTeacher(Long teacherId, Long semesterId);

    /**
     * Counts the number of duplicates of given lesson in the database.
     *
     * @param lesson the lesson entity to be count
     * @return the number of duplicates
     */
    Long countLessonDuplicates(Lesson lesson);

    /**
     * Counts the number of duplicates of given lesson in the database other than the id of the given lesson.
     *
     * @param lesson the lesson to be verified
     * @return the number of duplicates
     */
    Long countLessonDuplicatesWithIgnoreId(Lesson lesson);

    /**
     * Returns the list of lessons from database by semesterId.
     *
     * @param semesterId the id of the semester for getting all lessons by this id from db
     * @return the list of lessons
     */
    List<Lesson> getLessonsBySemester(Long semesterId);

    /**
     * Deletes lessons with the given semester id in the database.
     *
     * @param semesterId the id of the semester for delete all lessons by this id in db
     */
    void deleteLessonsBySemesterId(Long semesterId);

    /**
     * Returns the list of lessons with the same subject, teacher and semester as the given lesson, except given lesson.
     *
     * @param lesson the lesson entity
     * @return the list of lessons
     */
    List<Lesson> getLessonsBySubjectIdTeacherIdSemesterIdLessonTypeAndExcludeCurrentLessonId(Lesson lesson);

    /**
     * Returns all lessons from database which are grouped with the given lesson.
     *
     * @param lesson the lesson entity
     * @return the list of lessons
     */
    List<Lesson> getGroupedLessonsByLesson(Lesson lesson);

    /**
     * Updates links to lesson's meeting.
     * <p>
     * By default, link to meeting is updated by semester id and teacher id.
     * But update can be more specific by providing additional subject id and/or lesson type in a lesson object.
     *
     * @param lesson the lesson with a new link to the meeting
     * @return the number of updated links
     */
    Integer updateLinkToMeeting(Lesson lesson);

    /**
     * Updates grouped lessons.
     *
     * @param oldLesson                 the lesson
     * @param updatedLesson             grouped lesson that needs to be updated
     * @param isTeacherOrSubjectUpdated {@code true} if teacher or subject is updated
     * @return the updated lesson
     */
    Lesson updateGrouped(Lesson oldLesson, Lesson updatedLesson, boolean isTeacherOrSubjectUpdated);

    /**
     * Deletes all grouped lessons.
     *
     * @param lesson the grouped lesson to delete
     * @return the deleted lesson
     */
    Lesson deleteGrouped(Lesson lesson);

    /**
     * Set the lesson as grouped.
     *
     * @param lessonId the id of the lesson
     * @return the number of updated lessons
     */
    int setGrouped(Long lessonId);
}
