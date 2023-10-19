package com.softserve.service.impl;

import com.softserve.entity.Department;
import com.softserve.entity.Teacher;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.exception.FieldAlreadyExistsException;
import com.softserve.repository.DepartmentRepository;
import com.softserve.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Department getById(Long id) {
        log.info("In getById(id = [{}])", id);
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Department.class, "id", id.toString()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Department> getAll() {
        log.info("In getAll()");
        return repository.getAll();
    }

    /**
     * {@inheritDoc}
     *
     * @throws FieldAlreadyExistsException if there is a department with a name as the given department has
     */
    @Override
    public Department save(Department object) {
        log.info("In save(entity = [{}]", object);
        checkNameForUniqueness(object);
        return repository.save(object);
    }

    /**
     * {@inheritDoc}
     *
     * @throws FieldAlreadyExistsException if there is a department with a name as the given department has
     */
    @Override
    public Department update(Department object) {
        log.info("In update(entity = [{}]", object);
        checkNameForUniquenessIgnoringId(object);
        return repository.update(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Department delete(Department object) {
        log.info("In delete(entity = [{}])", object);
        return repository.delete(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Department> getDisabled() {
        log.info("Enter into getAll of getDisabled");
        return repository.getDisabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Teacher> getAllTeachers(Long departmentId) {
        log.info("Enter into getAllTeachers with department id {}", departmentId);
        return repository.getAllTeachers(departmentId);
    }

    /**
     * Checks if there is no department with the same name as the given department in the repository.
     *
     * @param object the department
     * @throws FieldAlreadyExistsException if there is a department with the name as the given department has
     */
    private void checkNameForUniqueness(Department object) {
        if (repository.isExistsByName(object.getName())) {
            throw new FieldAlreadyExistsException(Department.class, "name", object.getName());
        }
    }

    /**
     * Checks the uniqueness of the department name in the repository.
     *
     * @param object the department
     * @throws FieldAlreadyExistsException if the name of the given department not unique
     */
    private void checkNameForUniquenessIgnoringId(Department object) {
        if (repository.isExistsByNameIgnoringId(object.getName(), object.getId())) {
            throw new FieldAlreadyExistsException(Department.class, "name", object.getName());
        }
    }
}
