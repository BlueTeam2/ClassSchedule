package com.softserve.repository;

import com.softserve.entity.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends BasicRepository<Teacher, Long> {

    /**
     * {@inheritDoc}
     */
    List<Teacher> getDisabled();

    /**
     * Retrieves a teacher by user id.
     *
     * @param userId the id of the user
     * @return an Optional describing the teacher with the given user id or an empty Optional if none found
     */
    Optional<Teacher> findByUserId(Long userId);

    /**
     * Returns all teachers from database, that don't registered in system.
     *
     * @return the list of teachers
     */
    List<Teacher> getAllTeacherWithoutUser();

    /**
     * Retrieves a teacher entity by his full name and position.
     *
     * @param teacher the teacher with given full name and position
     * @return an Optional describing the teacher with the given user id or an empty Optional if none found
     */
    Optional<Teacher> getExistingTeacher(Teacher teacher);

}
