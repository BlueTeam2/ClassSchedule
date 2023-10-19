package com.softserve.service;

import com.softserve.dto.SubjectWithTypeDTO;
import com.softserve.entity.Subject;

import java.util.List;

public interface SubjectService extends BasicService<Subject, Long> {

    /**
     * Checks if subject with given name already exists.
     *
     * @param name the string represents the subject name
     * @return {@code true} if Subject with such name already exists, otherwise {@code false}
     */
    boolean isSubjectExistsWithName(String name);

    /**
     * Checks if subject with given name already exists in the repository ignoring the given group id.
     *
     * @param id   the id of the subject
     * @param name the string represents the subject name
     * @return {@code true} if subject with given name already exists, otherwise {@code false}
     */
    boolean isSubjectExistsWithNameAndIgnoreWithId(Long id, String name);

    /**
     * Checks if subject with given id exists in the repository.
     *
     * @param id the id of the subject
     * @return {@code true} if subject with given id exists, otherwise {@code false}
     */
    boolean isExistsWithId(Long id);

    /**
     * Returns all disabled subjects.
     *
     * @return the list of disabled subjects
     */
    List<Subject> getDisabled();

    /**
     * Returns all subjects with their types from repository with the given semester id and teacher id.
     *
     * @param semesterId the id of the semester from which subjects will be taken
     * @param teacherId  the id of the teacher who teaches subjects
     * @return the list of subjects with their types
     */
    List<SubjectWithTypeDTO> getSubjectsWithTypes(Long semesterId, Long teacherId);

}
