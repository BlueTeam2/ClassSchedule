package com.softserve.service;

import com.softserve.dto.StudentDTO;
import com.softserve.dto.StudentImportDTO;
import com.softserve.entity.Student;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface StudentService extends BasicService<Student, Long> {

    /**
     * Saves student in the repository and register user if email exists.
     *
     * @param studentDTO the student to be saved
     * @return the saved student
     * @throws com.softserve.exception.FieldNullException          if user's email was empty
     * @throws com.softserve.exception.FieldAlreadyExistsException if email was already in use
     */
    Student save(StudentDTO studentDTO);

    /**
     * Updates student in the repository.
     *
     * @param studentDTO the student with new information to be updated
     * @return the updated student
     * @throws com.softserve.exception.FieldAlreadyExistsException if student with given input email already exists
     */
    Student update(StudentDTO studentDTO);

    /**
     * Imports students from csv file.
     *
     * @param file    the string represents a file name with data of students
     * @param groupId the id of the group
     * @return list of imported and saved students
     */
    CompletableFuture<List<StudentImportDTO>> saveFromFile(MultipartFile file, Long groupId);

}
