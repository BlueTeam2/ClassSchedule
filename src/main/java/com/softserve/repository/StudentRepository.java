package com.softserve.repository;

import com.softserve.entity.Student;

import java.util.Optional;

public interface StudentRepository extends BasicRepository<Student, Long> {

    /**
     * Retrieves a student from the database by full name.
     *
     * @param student the student with given full name
     * @return an Optional describing the student with the given id or an empty Optional if none found
     */
    Optional<Student> getExistingStudent(Student student);

    /**
     * Returns {@code true} if given email exists in the database.
     *
     * @param email the string represents the email
     * @return {@code true} if given email exists, otherwise {@code false}
     */
    boolean isEmailInUse(String email);

    /**
     * Returns {@code true} if there is a student with given email and id in the database.
     *
     * @param email the string represents an email
     * @param id    the id of the student
     * @return {@code true} if there is a student with given email and id, otherwise {@code false}
     */
    boolean isEmailForThisStudent(String email, Long id);

    /**
     * Returns {@code true} if there is a student with given id, otherwise false.
     *
     * @param id the id of the student
     * @return {@code true} if there is a student with given id, otherwise false
     */
    boolean isIdPresent(Long id);

}
