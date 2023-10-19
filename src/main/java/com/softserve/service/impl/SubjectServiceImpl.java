package com.softserve.service.impl;

import com.softserve.dto.SubjectWithTypeDTO;
import com.softserve.entity.Group;
import com.softserve.entity.Subject;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.exception.FieldAlreadyExistsException;
import com.softserve.repository.SubjectRepository;
import com.softserve.service.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Subject getById(Long id) {
        log.info("In getById(id = [{}])", id);
        return subjectRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(Subject.class, "id", id.toString()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Subject> getAll() {
        log.info("In getAll()");
        return subjectRepository.getAll();
    }

    /**
     * {@inheritDoc}
     *
     * @throws FieldAlreadyExistsException if subject with given name already exists.
     */
    @Override
    public Subject save(Subject object) {
        log.info("In save(entity = [{}]", object);
        if (isSubjectExistsWithName(object.getName())) {
            log.error("Subject with name {} already exists", object.getName());
            throw new FieldAlreadyExistsException(Subject.class, "name", object.getName());
        }
        return subjectRepository.save(object);
    }

    /**
     * {@inheritDoc}
     *
     * @throws FieldAlreadyExistsException if subject with given name already exists.
     * @throws EntityNotFoundException     if given subject not found
     */
    @Override
    public Subject update(Subject object) {
        log.info("In update(entity = [{}]", object);
        if (isExistsWithId(object.getId())) {
            if (isSubjectExistsWithNameAndIgnoreWithId(object.getId(), object.getName())) {
                log.error("Subject with name [{}] already exists", object.getName());
                throw new FieldAlreadyExistsException(Subject.class, "name", object.getName());
            }
            return subjectRepository.update(object);
        } else {
            throw new EntityNotFoundException(Group.class, "id", object.getId().toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Subject delete(Subject object) {
        log.info("In delete(object = [{}])", object);
        return subjectRepository.delete(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSubjectExistsWithName(String name) {
        log.info("In isSubjectExistsWithName(name = [{}])", name);
        return subjectRepository.countSubjectsWithName(name) != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSubjectExistsWithNameAndIgnoreWithId(Long id, String name) {
        log.info("In isSubjectExistsWithNameAndIgnoreWithId(id = [{}], name = [{}])", id, name);
        return subjectRepository.countSubjectsWithNameAndIgnoreWithId(id, name) != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExistsWithId(Long id) {
        log.info("In isExistsWithId(id = [{}])", id);
        return subjectRepository.countBySubjectId(id) != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Subject> getDisabled() {
        log.info("Enter into getAll of getDisabled");
        return subjectRepository.getDisabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SubjectWithTypeDTO> getSubjectsWithTypes(Long semesterId, Long teacherId) {
        log.info("In service getSubjects(semesterId = [{}], teacherId = [{}])", semesterId, teacherId);
        return subjectRepository.getSubjectsWithTypes(semesterId, teacherId);
    }
}
