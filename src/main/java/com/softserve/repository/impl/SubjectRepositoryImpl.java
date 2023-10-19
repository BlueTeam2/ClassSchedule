package com.softserve.repository.impl;

import com.softserve.dto.SubjectWithTypeDTO;
import com.softserve.entity.Subject;
import com.softserve.repository.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class SubjectRepositoryImpl extends BasicRepositoryImpl<Subject, Long> implements SubjectRepository {

    private static final String GET_SUBJECTS = "SELECT new com.softserve.dto.SubjectWithTypeDTO(l.subject, l.lessonType) " +
            "FROM Lesson l WHERE l.teacher.id = :teacherId AND l.semester.id = :semesterId";

    /**
     * Returns the current session with the "subjectDisableFilter" filter enabled.
     *
     * @return the current session with the "subjectDisableFilter" filter enabled
     */
    private Session getSession() {
        Session session = sessionFactory.getCurrentSession();
        Filter filter = session.enableFilter("subjectDisableFilter");
        filter.setParameter("disable", false);
        return session;
    }

    /**
     * Returns all subjects from database with ascending sorting by name.
     *
     * @return the list of subjects with ascending sorting by name
     */
    @Override
    public List<Subject> getAll() {
        log.info("In getAll()");
        Session session = getSession();
        return session.createQuery("from Subject ORDER BY name ASC", Subject.class)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countSubjectsWithName(String name) {
        log.info("In countSubjectsWithName(name = [{}])", name);
        return (Long) sessionFactory.getCurrentSession().createQuery("SELECT count (*) FROM Subject s WHERE s.name = :name")
                .setParameter("name", name).getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countSubjectsWithNameAndIgnoreWithId(Long id, String name) {
        log.info("In countSubjectsWithName(name = [{}])", name);
        return (Long) sessionFactory.getCurrentSession().createQuery("SELECT count (*) FROM Subject s WHERE s.name = :name and id!=:id")
                .setParameter("name", name).setParameter("id", id).getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countBySubjectId(Long id) {
        log.info("In countBySubjectId(id = [{}])", id);
        return (Long) sessionFactory.getCurrentSession().createQuery("SELECT count (*) FROM Subject s WHERE s.id = :id")
                .setParameter("id", id).getSingleResult();
    }

    /**
     * Checks if subject is used in Lesson table.
     *
     * @param subject the group entity to be checked
     * @return {@code true} if exists lesson related with given subject, otherwise {@code false}
     */
    @Override
    protected boolean checkReference(Subject subject) {
        log.info("In checkReference(subject = [{}])", subject);
        Long count = (Long) sessionFactory.getCurrentSession().createQuery(
                        "select count (l.id) " +
                                "from Lesson l where l.subject.id = :subjectId")
                .setParameter("subjectId", subject.getId())
                .getSingleResult();
        return count != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SubjectWithTypeDTO> getSubjectsWithTypes(Long semesterId, Long teacherId) {
        log.info("In repository getSubjects(semesterId = [{}], teacherId = [{}])", semesterId, teacherId);
        return sessionFactory.getCurrentSession().createQuery(GET_SUBJECTS, SubjectWithTypeDTO.class)
                .setParameter("teacherId", teacherId)
                .setParameter("semesterId", semesterId)
                .getResultList();
    }
}
