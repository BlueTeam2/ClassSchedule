package com.softserve.repository.impl;

import com.softserve.entity.Department;
import com.softserve.entity.Teacher;
import com.softserve.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DepartmentRepositoryImpl extends BasicRepositoryImpl<Department, Long> implements DepartmentRepository {

    private static final String HQL_IS_EXISTS_BY_NAME
            = "SELECT (count(*) > 0) "
            + "FROM Department d "
            + "WHERE lower(d.name) = lower(:name)";

    private static final String HQL_IS_EXISTS_BY_NAME_IGNORING_ID = HQL_IS_EXISTS_BY_NAME + " AND d.id!=:id";

    private static final String HQL_SELECT_TEACHERS_FOR_DEPARTMENT_ID
            = "SELECT t "
            + "FROM Teacher t "
            + "WHERE t.department.id = :department_id";

    private static final String HQL_SELECT_ALL = "FROM Department ORDER BY name ASC";

    private Session getSession() {
        Session session = sessionFactory.getCurrentSession();
        Filter filter = session.enableFilter("departmentDisableFilter");
        filter.setParameter("disable", false);
        return session;
    }

    /**
     * Returns the list of all departments with ascending sort by name from the database.
     *
     * @return the list of departments with ascending sort by name
     */
    @Override
    public List<Department> getAll() {
        log.info("In getAll()");
        return getSession()
                .createQuery(HQL_SELECT_ALL, Department.class)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Department update(Department entity) {
        sessionFactory.getCurrentSession().clear();
        return super.update(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExistsByName(String name) {
        log.info("In isExistsByName(name = [{}])", name);
        if (name == null) {
            return false;
        }
        return (boolean) sessionFactory.getCurrentSession()
                .createQuery(HQL_IS_EXISTS_BY_NAME)
                .setParameter("name", name)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExistsByNameIgnoringId(String name, Long id) {
        log.info("In isExistsByTitleIgnoringId(id = [{}], name = [{}])", id, name);
        if (name == null) {
            return false;
        }
        return (boolean) sessionFactory.getCurrentSession()
                .createQuery(HQL_IS_EXISTS_BY_NAME_IGNORING_ID)
                .setParameter("name", name)
                .setParameter("id", id)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Teacher> getAllTeachers(Long departmentId) {
        log.info("In getAllTeachers (departmentId = [{}])", departmentId);
        return sessionFactory.getCurrentSession()
                .createQuery(HQL_SELECT_TEACHERS_FOR_DEPARTMENT_ID, Teacher.class)
                .setParameter("department_id", departmentId)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkReference(Department department) {
        return sessionFactory.getCurrentSession()
                .createQuery(HQL_SELECT_TEACHERS_FOR_DEPARTMENT_ID)
                .setParameter("department_id", department.getId())
                .setMaxResults(1)
                .uniqueResultOptional()
                .isPresent();
    }
}
