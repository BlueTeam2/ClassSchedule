package com.softserve.repository.impl;

import com.softserve.entity.Teacher;
import com.softserve.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@SuppressWarnings("unchecked")
public class TeacherRepositoryImpl extends BasicRepositoryImpl<Teacher, Long> implements TeacherRepository {

    private Session getSession() {
        Session session = sessionFactory.getCurrentSession();
        Filter filter = session.enableFilter("teachersDisableFilter");
        filter.setParameter("disable", false);
        return session;
    }

    /**
     * Returns all teachers from database.
     *
     * @return the list of teachers ordered by surname
     */
    @Override
    public List<Teacher> getAll() {
        log.info("Enter into getAll of TeacherRepositoryImpl");
        Session session = getSession();
        return session.createQuery("select t from " + basicClass.getName() + " t" +
                " order by t.surname ASC").getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher update(Teacher entity) {
        sessionFactory.getCurrentSession().clear();
        return super.update(entity);
    }

    /**
     * Checks if teacher is used in Lesson table.
     *
     * @param teacher the teacher to be checked
     * @return {@code true} if exists lesson related with given teacher, otherwise {@code false}
     */
    @Override
    protected boolean checkReference(Teacher teacher) {
        log.info("In checkReference(teacher = [{}])", teacher);
        Long count = (Long) sessionFactory.getCurrentSession().createQuery(
                        "select count (l.id) " +
                                "from Lesson l where l.teacher.id = :teacherId")
                .setParameter("teacherId", teacher.getId()).getSingleResult();

        return count != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Teacher> findByUserId(Long userId) {
        return sessionFactory.getCurrentSession().createQuery(
                        "select t from Teacher t " +
                                "where t.userId= :userId")
                .setParameter("userId", userId)
                .uniqueResultOptional();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Teacher> getAllTeacherWithoutUser() {
        log.info("Enter into getAllTeacherWithoutUser of TeacherRepositoryImpl");
        return sessionFactory.getCurrentSession().createQuery(
                        "select t from Teacher t " +
                                " where t.userId = null ")
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Teacher> getExistingTeacher(Teacher teacher) {
        return sessionFactory.getCurrentSession().createQuery(
                        "select t from Teacher t " +
                                "where t.name = :tName and " +
                                "t.surname = :tSurname and " +
                                "t.patronymic = :tPatronymic and " +
                                "t.position = :tPosition")
                .setParameter("tName", teacher.getName())
                .setParameter("tSurname", teacher.getSurname())
                .setParameter("tPatronymic", teacher.getPatronymic())
                .setParameter("tPosition", teacher.getPosition())
                .uniqueResultOptional();

    }

}
