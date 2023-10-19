package com.softserve.service;

import com.softserve.entity.Department;
import com.softserve.entity.Teacher;

import java.util.List;

public interface DepartmentService extends BasicService<Department, Long> {

    /**
     * Returns all disabled departments.
     *
     * @return the list of disabled departments
     */
    List<Department> getDisabled();

    /**
     * Returns all teachers with the given department id.
     *
     * @param departmentId the id of the department
     * @return the list of the teachers
     */
    List<Teacher> getAllTeachers(Long departmentId);
}
