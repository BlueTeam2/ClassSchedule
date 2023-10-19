package com.softserve.repository;

import com.softserve.entity.Department;
import com.softserve.entity.Teacher;

import java.util.List;

public interface DepartmentRepository extends BasicRepository<Department, Long> {

    /**
     * Returns {@code true} if there is a department with the specified name.
     *
     * @param name the string represents the name of the department
     * @return {@code true} if there is such department, otherwise {@code false}
     */
    boolean isExistsByName(String name);

    /**
     * Returns {@code true} if there is a department with the specified name other than the department with the given id.
     *
     * @param name the string represents the name of the department
     * @param id   the id of the department to be excluded from the search result
     * @return {@code true} if there is such department, otherwise {@code false}
     */
    boolean isExistsByNameIgnoringId(String name, Long id);

    /**
     * Returns all teachers with the given department id.
     *
     * @param departmentId the id of the department
     * @return the list of teachers
     */
    List<Teacher> getAllTeachers(Long departmentId);
}
