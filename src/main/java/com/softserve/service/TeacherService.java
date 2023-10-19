package com.softserve.service;

import com.softserve.dto.TeacherDTO;
import com.softserve.dto.TeacherForUpdateDTO;
import com.softserve.dto.TeacherImportDTO;
import com.softserve.entity.Teacher;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeacherService extends BasicService<Teacher, Long> {

    /**
     * Saves given teacher in the repository and register user if email exists.
     *
     * @param teacherDTO the teacher to be saved
     * @return the saved teacher
     */
    Teacher save(TeacherDTO teacherDTO);

    /**
     * Updates an existing teacher in the repository and register user if email was added.
     *
     * @param teacherForUpdateDTO the teacher with info to be updated
     * @return the updated teacher
     */
    Teacher update(TeacherForUpdateDTO teacherForUpdateDTO);

    /**
     * Returns joined teacher with user.
     *
     * @param teacherId the id of the teacher
     * @param userId    the id of the user
     * @return the joined teacher with user
     * @throws com.softserve.exception.EntityAlreadyExistsException if user already exist in some teacher/manager or teacher contains some user id
     */
    Teacher joinTeacherWithUser(Long teacherId, Long userId);

    /**
     * Returns all disabled teachers.
     *
     * @return the list of disabled teachers
     */
    List<Teacher> getDisabled();

    /**
     * Returns teacher by user id.
     *
     * @param userId the id of the user
     * @return the founded teacher
     * @throws com.softserve.exception.EntityNotFoundException if teacher doesn't exist
     * @throws com.softserve.exception.FieldNullException      if user id is null
     */
    Teacher findByUserId(Long userId);

    /**
     * Returns all teachers from repository, that don't registered in system.
     *
     * @return the list of teachers without registered user
     */
    List<Teacher> getAllTeacherWithoutUser();

    /**
     * Imports teachers from file and saves in the repository.
     *
     * @param file         the file with teachers data
     * @param departmentId the id of the department
     * @return tje list of created teachers
     */
    List<TeacherImportDTO> saveFromFile(MultipartFile file, Long departmentId);

    /**
     * Saves given teacher with given department id.
     *
     * @param departmentId the id of the department
     * @param teacher      the teacher entity which
     * @return the saved teacher
     */
    TeacherImportDTO saveTeacher(Long departmentId, TeacherImportDTO teacher);
}

