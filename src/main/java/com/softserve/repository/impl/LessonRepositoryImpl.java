package com.softserve.repository.impl;

import com.softserve.entity.Lesson;
import com.softserve.repository.LessonRepository;
import com.softserve.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class LessonRepositoryImpl extends BasicRepositoryImpl<Lesson, Long> implements LessonRepository {

    private static final String SELECT_GROUPED
            = "select l from Lesson l "
            + "where l.grouped = true "
            + "and l.subject.id = :subjectId "
            + "and l.hours = :hours "
            + "and l.teacher.id = :teacherId "
            + "and l.semester.id = :semesterId "
            + "and l.lessonType = :lessonType "
            + "and l.subjectForSite = :subjectForSite";

    private static final String SET_GROUPED
            = "update Lesson "
            + "set grouped = true "
            + "where id = :id";

    private static final String UPDATE_GROUPED_TEACHER_OR_SUBJECT
            = "update Lesson "
            + "set subject.id = :subjectId, "
            + "hours = :hours, "
            + "teacher.id = :teacherId, "
            + "lessonType = :lessonType, "
            + "subjectForSite = :subjectForSite, "
            + "linkToMeeting = :linkToMeeting "
            + "where grouped = true "
            + "and subject.id = :initialSubjectId "
            + "and teacher.id = :initialTeacherId "
            + "and semester.id = :initialSemesterId";

    private static final String UPDATE_GROUPED
            = "update Lesson "
            + "set subject.id = :subjectId, "
            + "hours = :hours, "
            + "teacher.id = :teacherId, "
            + "lessonType = :lessonType, "
            + "subjectForSite = :subjectForSite, "
            + "linkToMeeting = :linkToMeeting "
            + "where grouped = true "
            + "and subject.id = :initialSubjectId "
            + "and hours = :initialHours "
            + "and teacher.id = :initialTeacherId "
            + "and semester.id = :initialSemesterId "
            + "and lessonType = :initialLessonType "
            + "and subjectForSite = :initialSubjectForSite";

    private static final String DELETE_GROUPED
            = "delete Lesson l "
            + "where l.grouped = true "
            + "and l.subject.id = :subjectId "
            + "and l.hours = :hours "
            + "and l.teacher.id = :teacherId "
            + "and l.semester.id = :semesterId "
            + "and l.lessonType = :lessonType "
            + "and l.subjectForSite = :subjectForSite";

    private static final String COUNT_QUERY
            = "select count (s.id) "
            + "from Schedule s "
            + "where s.lesson.id = :lessonId";

    private static final String GET_BY_SEMESTER_ID
            = "select l from Lesson l "
            + "where l.semester.id= :semesterId "
            + "order by l.subjectForSite ASC ";

    private static final String GET_SUBJECT_TEACHER_SEMESTER
            = "select l from Lesson l " +
            "where l.subject.id= :subjectId " +
            "and l.teacher.id= :teacherId " +
            "and l.semester.id= :semesterId " +
            "and l.lessonType= :lessonType " +
            "and l.id != :lessonId";

    private static final String DELETE_BY_SEMESTER_ID
            = "delete from Lesson l " +
            "where l.id " +
            "in (select les.id " +
            "from Lesson les " +
            "where les.semester.id = :semesterId)";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lesson> getAll() {
        log.info("In getAll()");
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Lesson> cq = cb.createQuery(Lesson.class);
        Root<Lesson> from = cq.from(Lesson.class);
        cq.where(cb.equal(from.get(Constants.TEACHER).get(Constants.DISABLE), false),
                cb.equal(from.get(Constants.SUBJECT).get(Constants.DISABLE), false),
                cb.equal(from.get(Constants.GROUP).get(Constants.DISABLE), false));
        cq.orderBy(cb.asc(from.get(Constants.SUBJECT_FOR_SITE)));

        TypedQuery<Lesson> tq = sessionFactory.getCurrentSession().createQuery(cq);
        return tq.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lesson> getAllForGroup(Long groupId, Long semesterId) {
        log.info("In getAllForGroup(groupId = [{}])", groupId);
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Lesson> cq = cb.createQuery(Lesson.class);
        Root<Lesson> from = cq.from(Lesson.class);

        cq.where(cb.equal(from.get(Constants.TEACHER).get(Constants.DISABLE), false),
                cb.equal(from.get(Constants.SUBJECT).get(Constants.DISABLE), false),

                cb.equal(from.get(Constants.GROUP).get(Constants.DISABLE), false),
                cb.equal(from.get(Constants.GROUP).get(Constants.ID), groupId),
                cb.equal(from.get(Constants.SEMESTER).get(Constants.ID), semesterId));
        cq.orderBy(cb.asc(from.get(Constants.SUBJECT_FOR_SITE)));
        TypedQuery<Lesson> tq = sessionFactory.getCurrentSession().createQuery(cq);
        return tq.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lesson> getLessonByTeacher(Long teacherId, Long semesterId) {
        log.info("In getLessonByTeacher(groupId = [{}])", teacherId);
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Lesson> cq = cb.createQuery(Lesson.class);
        Root<Lesson> from = cq.from(Lesson.class);

        cq.where(cb.equal(from.get(Constants.TEACHER).get(Constants.DISABLE), false),
                cb.equal(from.get(Constants.TEACHER).get(Constants.ID), teacherId),
                cb.equal(from.get(Constants.SUBJECT).get(Constants.DISABLE), false),

                cb.equal(from.get(Constants.GROUP).get(Constants.DISABLE), false),
                cb.equal(from.get(Constants.SEMESTER).get(Constants.ID), semesterId));
        cq.orderBy(cb.asc(from.get(Constants.SUBJECT_FOR_SITE)));
        TypedQuery<Lesson> tq = sessionFactory.getCurrentSession().createQuery(cq);
        return tq.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countLessonDuplicates(Lesson lesson) {
        log.info("In countLessonDuplicates(lesson = [{}])", lesson);

        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Lesson> from = cq.from(Lesson.class);

        cq.where(
                cb.equal(from.get(Constants.TEACHER).get(Constants.ID), lesson.getTeacher().getId()),
                cb.equal(from.get(Constants.SUBJECT).get(Constants.ID), lesson.getSubject().getId()),
                cb.equal(from.get(Constants.GROUP).get(Constants.ID), lesson.getGroup().getId()),
                cb.equal(from.get(Constants.SEMESTER).get(Constants.ID), lesson.getSemester().getId()),
                cb.equal(from.get(Constants.LESSON_TYPE), lesson.getLessonType()));

        cq.select(cb.count(from));
        Query<Long> query = sessionFactory.getCurrentSession().createQuery(cq);
        return query.getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countLessonDuplicatesWithIgnoreId(Lesson lesson) {
        log.info("In countLessonDuplicates(lesson = [{}])", lesson);

        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Lesson> from = cq.from(Lesson.class);

        cq.where(cb.equal(from.get(Constants.TEACHER).get(Constants.DISABLE), false),
                cb.equal(from.get(Constants.TEACHER).get(Constants.ID), lesson.getTeacher().getId()),

                cb.equal(from.get(Constants.SUBJECT).get(Constants.DISABLE), false),
                cb.equal(from.get(Constants.SUBJECT).get(Constants.ID), lesson.getSubject().getId()),

                cb.equal(from.get(Constants.GROUP).get(Constants.DISABLE), false),
                cb.equal(from.get(Constants.GROUP).get(Constants.ID), lesson.getGroup().getId()),

                cb.notEqual(from.get(Constants.ID), lesson.getId()),
                cb.equal(from.get(Constants.SEMESTER).get(Constants.ID), lesson.getSemester().getId()),
                cb.equal(from.get(Constants.LESSON_TYPE), lesson.getLessonType()));
        cq.select(cb.count(from));
        Query<Long> query = sessionFactory.getCurrentSession().createQuery(cq);
        return query.getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lesson> getLessonsBySemester(Long semesterId) {
        log.info("In getLessonsBySemester(semesterId = [{}])", semesterId);
        return sessionFactory.getCurrentSession()
                .createQuery(GET_BY_SEMESTER_ID, Lesson.class)
                .setParameter(Constants.SEMESTER_ID, semesterId)
                .getResultList();
    }

    @Override
    public void deleteLessonsBySemesterId(Long semesterId) {
        log.info("In deleteLessonBySemesterId(semesterId = [{}])", semesterId);
        sessionFactory.getCurrentSession()
                .createQuery(DELETE_BY_SEMESTER_ID)
                .setParameter(Constants.SEMESTER_ID, semesterId).executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lesson> getLessonsBySubjectIdTeacherIdSemesterIdLessonTypeAndExcludeCurrentLessonId(Lesson lesson) {
        log.info("In getLessonsBySubjectIdTeacherIdSemesterIdLessonTypeAndExcludeCurrentLessonId(lesson = [{}]", lesson);
        return sessionFactory.getCurrentSession()
                .createQuery(GET_SUBJECT_TEACHER_SEMESTER, Lesson.class)
                .setParameter(Constants.SUBJECT_ID, lesson.getSubject().getId())
                .setParameter(Constants.TEACHER_ID, lesson.getTeacher().getId())
                .setParameter(Constants.SEMESTER_ID, lesson.getSemester().getId())
                .setParameter(Constants.LESSON_TYPE, lesson.getLessonType())
                .setParameter(Constants.LESSON_ID, lesson.getId())
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lesson> getGroupedLessonsByLesson(Lesson lesson) {
        log.info("getGroupedLessonsByLessonId(lesson = [{}]", lesson);
        return sessionFactory.getCurrentSession()
                .createQuery(SELECT_GROUPED, Lesson.class)
                .setParameter(Constants.SUBJECT_ID, lesson.getSubject().getId())
                .setParameter(Constants.HOURS, lesson.getHours())
                .setParameter(Constants.TEACHER_ID, lesson.getTeacher().getId())
                .setParameter(Constants.SEMESTER_ID, lesson.getSemester().getId())
                .setParameter(Constants.LESSON_TYPE, lesson.getLessonType())
                .setParameter(Constants.SUBJECT_FOR_SITE, lesson.getSubjectForSite())
                .getResultList();
    }

    /**
     * Checks if lesson is used in Schedule table.
     *
     * @param lesson the lesson entity to be checked
     * @return {@code true} if lesson is used in Schedule table, otherwise {@code false}
     */
    @Override
    protected boolean checkReference(Lesson lesson) {
        log.info("In checkReference(lesson = [{}])", lesson);
        Long count = sessionFactory.getCurrentSession().createQuery(COUNT_QUERY, Long.class)
                .setParameter(Constants.LESSON_ID, lesson.getId())
                .getSingleResult();
        return count != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer updateLinkToMeeting(Lesson lesson) {
        log.info("In repository updateLinkToMeeting lesson = [{}]", lesson);
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaUpdate<Lesson> criteriaUpdate = cb.createCriteriaUpdate(Lesson.class);

        Root<Lesson> root = criteriaUpdate.from(Lesson.class);

        criteriaUpdate.set("linkToMeeting", lesson.getLinkToMeeting());

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get(Constants.SEMESTER).get(Constants.ID), lesson.getSemester().getId()));
        predicates.add(cb.equal(root.get(Constants.TEACHER).get(Constants.ID), lesson.getTeacher().getId()));

        if (lesson.getSubject().getId() != null) {
            predicates.add(cb.equal(root.get(Constants.SUBJECT).get(Constants.ID), lesson.getSubject().getId()));
        }

        if (lesson.getLessonType() != null) {
            predicates.add(cb.equal(root.get(Constants.LESSON_TYPE), lesson.getLessonType()));
        }

        criteriaUpdate.where(predicates.toArray(new Predicate[0]));

        return sessionFactory.getCurrentSession().createQuery(criteriaUpdate).executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Lesson updateGrouped(Lesson oldLesson, Lesson updatedLesson, boolean isTeacherOrSubjectUpdated) {
        log.info("Entered updateGroup({}, {})", oldLesson, updatedLesson);
        Session currentSession = sessionFactory.getCurrentSession();
        Query<Lesson> query = isTeacherOrSubjectUpdated
                ? currentSession.createQuery(UPDATE_GROUPED_TEACHER_OR_SUBJECT)
                : currentSession.createQuery(UPDATE_GROUPED);
        query = query.setParameter("initialSubjectId", oldLesson.getSubject().getId())
                .setParameter("initialTeacherId", oldLesson.getTeacher().getId())
                .setParameter("initialSemesterId", oldLesson.getSemester().getId())
                .setParameter("linkToMeeting", updatedLesson.getLinkToMeeting())
                .setParameter(Constants.SUBJECT_ID, updatedLesson.getSubject().getId())
                .setParameter(Constants.HOURS, updatedLesson.getHours())
                .setParameter(Constants.TEACHER_ID, updatedLesson.getTeacher().getId())
                .setParameter(Constants.LESSON_TYPE, updatedLesson.getLessonType())
                .setParameter(Constants.SUBJECT_FOR_SITE, updatedLesson.getSubjectForSite());
        query = isTeacherOrSubjectUpdated
                ? query
                : query.setParameter("initialSubjectForSite", oldLesson.getSubjectForSite())
                .setParameter("initialHours", oldLesson.getHours())
                .setParameter("initialLessonType", oldLesson.getLessonType());
        int updated = query.executeUpdate();
        log.debug("Updated group lessons {}", updated);
        return updatedLesson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Lesson deleteGrouped(Lesson lesson) {
        log.info("Entered deleteGrouped({})", lesson);
        int deleted = sessionFactory.getCurrentSession()
                .createQuery(DELETE_GROUPED)
                .setParameter(Constants.SUBJECT_ID, lesson.getSubject().getId())
                .setParameter(Constants.HOURS, lesson.getHours())
                .setParameter(Constants.TEACHER_ID, lesson.getTeacher().getId())
                .setParameter(Constants.SEMESTER_ID, lesson.getSemester().getId())
                .setParameter(Constants.LESSON_TYPE, lesson.getLessonType())
                .setParameter(Constants.SUBJECT_FOR_SITE, lesson.getSubjectForSite())
                .executeUpdate();
        log.debug("Deleted group lessons {}", deleted);
        return lesson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int setGrouped(Long lessonId) {
        log.info("Entered setGrouped({})", lessonId);
        return sessionFactory.getCurrentSession()
                .createQuery(SET_GROUPED)
                .setParameter(Constants.ID, lessonId)
                .executeUpdate();
    }
}
