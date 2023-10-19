package com.softserve.repository.impl;

import com.softserve.entity.Group;
import com.softserve.repository.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class GroupRepositoryImpl extends BasicRepositoryImpl<Group, Long> implements GroupRepository {

    public static final String FROM_GROUP = " FROM Group g ";

    private static final String GET_ALL_QUERY
            = "SELECT g"
            + FROM_GROUP
            + "ORDER BY g.title ASC";
    private static final String GET_WITH_STUDENTS_BY_ID_QUERY
            = "SELECT g"
            + FROM_GROUP
            + "LEFT JOIN FETCH g.students "
            + "WHERE g.id = :id";
    private static final String IS_EXISTS_BY_TITLE_QUERY
            = "SELECT (count(*) > 0)"
            + FROM_GROUP
            + "WHERE lower(g.title) = lower(:title)";
    private static final String IS_EXISTS_BY_TITLE_IGNORING_ID_QUERY = IS_EXISTS_BY_TITLE_QUERY + " AND g.id!=:id";
    private static final String IS_EXISTS_BY_ID_QUERY
            = "SELECT (count(*) > 0)"
            + FROM_GROUP
            + "WHERE g.id = :id";

    private static final String FROM_LESSON = " FROM Lesson l ";

    private static final String IS_LESSONS_EXIST_FOR_GROUP_ID_QUERY
            = "SELECT (count(l.id) > 0)"
            + FROM_LESSON
            + "WHERE l.group.id = :groupId";
    private static final String GET_BY_TEACHER_ID
            = "SELECT DISTINCT l.group"
            + FROM_LESSON
            + "WHERE l.teacher.id = :id AND l.semester.defaultSemester = true";
    private static final String GET_GROUPS_BY_IDS
            = "select g"
            + FROM_GROUP
            + "where g.id in (:ids)";
    private static final String GET_ALL_ORDERED
            = "SELECT g"
            + FROM_GROUP
            + "ORDER BY g.sortOrder ASC";
    private static final String UPDATE_GROUP_OFFSET
            = "UPDATE Group g "
            + "SET g.sortOrder = g.sortOrder+1 "
            + "WHERE g.sortOrder >= :lowerPosition and g.sortOrder < :upperPosition";

    private Session getSession() {
        Session session = sessionFactory.getCurrentSession();
        Filter filter = session.enableFilter("groupDisableFilter");
        filter.setParameter("disable", false);
        return session;
    }

    /**
     * Returns all groups from database with ascending sorting by title.
     *
     * @return the list of all groups
     */
    @Override
    public List<Group> getAll() {
        log.info("In getAll()");
        return getSession()
                .createQuery(GET_ALL_QUERY, Group.class)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> getByTeacherId(Long id) {
        log.info("In repository getByTeacherId(id = [{}])", id);
        return getSession()
                .createQuery(GET_BY_TEACHER_ID, Group.class)
                .setParameter("id", id)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> getAllBySortOrder() {
        log.debug("Entered getAllBySortOrder()");
        return getSession()
                .createQuery(GET_ALL_ORDERED, Group.class)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeGroupOrderOffset(Integer lowerBound, Integer upperBound) {
        log.info("Entered changeGroupOffset({}, {})", lowerBound, upperBound);
        TypedQuery<Group> groupTypedQuery = getSession().createQuery(UPDATE_GROUP_OFFSET);
        groupTypedQuery.setParameter("lowerPosition", lowerBound);
        groupTypedQuery.setParameter("upperPosition", upperBound);
        int updated = groupTypedQuery.executeUpdate();
        log.debug("Updated order of {} groups", updated);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Group> getWithStudentsById(Long id) {
        log.info("In getWithStudentsById(id = [{}])", id);
        return sessionFactory.getCurrentSession()
                .createQuery(GET_WITH_STUDENTS_BY_ID_QUERY, Group.class)
                .setParameter("id", id)
                .uniqueResultOptional();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExistsByTitle(String title) {
        log.info("In isExistsByTitle(title = [{}])", title);
        if (title == null) {
            return false;
        }
        return (boolean) sessionFactory.getCurrentSession()
                .createQuery(IS_EXISTS_BY_TITLE_QUERY)
                .setParameter("title", title)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExistsByTitleIgnoringId(String title, Long id) {
        log.info("In isExistsByTitleIgnoringId(id = [{}], title = [{}])", id, title);
        if (title == null) {
            return false;
        }
        return (boolean) sessionFactory.getCurrentSession()
                .createQuery(IS_EXISTS_BY_TITLE_IGNORING_ID_QUERY)
                .setParameter("title", title)
                .setParameter("id", id)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExistsById(Long id) {
        log.info("In isExistsById(id = [{}])", id);
        return (boolean) sessionFactory.getCurrentSession()
                .createQuery(IS_EXISTS_BY_ID_QUERY)
                .setParameter("id", id)
                .getSingleResult();
    }

    /**
     * Checks if group is used in lesson table.
     *
     * @param group the group entity to be checked
     * @return {@code true} if exists lesson related with given group
     */
    @Override
    protected boolean checkReference(Group group) {
        log.info("In checkReference(group = [{}])", group);
        return (boolean) sessionFactory.getCurrentSession()
                .createQuery(IS_LESSONS_EXIST_FOR_GROUP_ID_QUERY)
                .setParameter("groupId", group.getId())
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> getGroupsByGroupIds(List<Long> groupIds) {
        return getSession()
                .createQuery(GET_GROUPS_BY_IDS, Group.class)
                .setParameterList("ids", groupIds)
                .getResultList();
    }

}
