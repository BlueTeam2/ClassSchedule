package com.softserve.repository;

import com.softserve.dto.SubjectWithTypeDTO;
import com.softserve.entity.Subject;

import java.util.List;

public interface SubjectRepository extends BasicRepository<Subject, Long> {

    /**
     * Counts the number of subjects with given name in the database.
     *
     * @param name the string represents the name of subject
     * @return the number of subjects with given name
     */
    Long countSubjectsWithName(String name);

    /**
     * Counts the number of subjects with given name and id in the database.
     *
     * @param id   the id of the subject
     * @param name the string represents the name of the subject
     * @return the number of subjects with given name and id
     */
    Long countSubjectsWithNameAndIgnoreWithId(Long id, String name);

    /**
     * Counts the number of subjects with given id.
     *
     * @param id the id of the subject
     * @return the number of subjects with given id
     */
    Long countBySubjectId(Long id);

    /**
     * {@inheritDoc}
     */
    List<Subject> getDisabled();

    /**
     * Returns subjects with given id of the semester and id of the teacher.
     *
     * @param semesterId the id of the semester from which subjects will be taken
     * @param teacherId  the id of the teacher who teaches those subjects
     * @return the list of subjects with their types
     */
    List<SubjectWithTypeDTO> getSubjectsWithTypes(Long semesterId, Long teacherId);

}
