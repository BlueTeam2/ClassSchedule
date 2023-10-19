package com.softserve.repository.impl;

import com.softserve.entity.Room;
import com.softserve.entity.enums.EvenOdd;
import com.softserve.repository.RoomRepository;
import com.softserve.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class RoomRepositoryImpl extends BasicRepositoryImpl<Room, Long> implements RoomRepository {

    private static final String GET_ALL_QUERY =
            "from Room order by name ASC";
    private static final String CHECK_REFERENCE =
            "select count (s.id) " +
                    "from Schedule s where s.room.id = :roomId";
    private static final String GET_NOT_AVAILABLE_ROOMS_FOR_SCHEDULE =
            "select r1 from Room r1 " +
                    "where r1.id in " +
                    "(select r.id from Schedule s " +
                    "join s.room r " +
                    "where  s.lesson.semester.id = :semesterId " +
                    "and s.dayOfWeek = :dayOfWeek " +
                    "and s.period.id = :classId )";
    private static final String GET_NOT_AVAILABLE_ROOMS_FOR_SCHEDULE_2 =
            "select r1 from Room r1 " +
                    "where r1.id in " +
                    "(select r.id from Schedule s " +
                    "join s.room r " +
                    "where  s.lesson.semester.id = :semesterId " +
                    "and s.dayOfWeek = :dayOfWeek " +
                    "and s.period.id = :classId " +
                    "and ( s.evenOdd = :evenOdd or s.evenOdd = 'WEEKLY') )";
    private static final String GET_AVAILABLE_ROOMS_FOR_SCHEDULE =
            "select r1 from Room r1 " +
                    "where r1.id not in " +
                    "(select r.id from Schedule s " +
                    "join s.room r " +
                    "where  s.lesson.semester.id = :semesterId " +
                    "and s.dayOfWeek = :dayOfWeek " +
                    "and s.period.id = :classId )";
    private static final String GET_AVAILABLE_ROOMS_FOR_SCHEDULE_2 =
            "select r1 from Room r1 " +
                    "where r1.id not in " +
                    "(select r.id from Schedule s " +
                    "join s.room r " +
                    "where  s.lesson.semester.id = :semesterId " +
                    "and s.dayOfWeek = :dayOfWeek " +
                    "and s.period.id = :classId " +
                    "and ( s.evenOdd = :evenOdd or s.evenOdd = 'WEEKLY') )";

    private static final String COUNT_ROOM_DUPLICATES =
            "select count(*) from Room r " +
                    "where r.name = :name  and r.id != :id " +
                    "and r.type.id = :typeId ";

    private static final String IS_EXISTS_BY_ID =
            "SELECT 1 " +
                    "FROM Room r " +
                    "WHERE r.id = :id";

    private static final String GET_ALL_ORDERED =
            "SELECT r " +
                    "FROM Room r " +
                    "ORDER BY r.sortOrder ASC";

    private static final String MOVE_SORT_ORDER =
            "UPDATE Room r " +
                    "SET r.sortOrder = r.sortOrder + :offset " +
                    "WHERE r.sortOrder >= :lowerPosition ";

    private static final String MOVE_SORT_ORDER_RANGE = MOVE_SORT_ORDER + "AND r.sortOrder <= :upperBound";

    private static final String GET_ORDER_BY_ID =
            "SELECT r.sortOrder " +
                    "FROM Room r " +
                    "WHERE r.id = :id";

    private static final String GET_MAX_SORT_ORDER =
            "SELECT max(r.sortOrder) " +
                    "FROM Room r";

    private Session getSession() {
        Session session = sessionFactory.getCurrentSession();
        Filter filter = session.enableFilter("roomDisableFilter");
        filter.setParameter(Constants.DISABLE, false);
        return session;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Room> getAll() {
        log.info("In getAll()");
        Session session = getSession();
        return session.createQuery(GET_ALL_QUERY, Room.class)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Room> freeRoomBySpecificPeriod(Long idOfPeriod, DayOfWeek dayOfWeek, EvenOdd evenOdd) {
        log.info("Enter into freeRoomBySpecificPeriod of RoomRepositoryImpl with id {}, dayOfWeek {} and evenOdd {} ",
                idOfPeriod, dayOfWeek, evenOdd);
        Session session = getSession();
        Query<Room> query = session.createQuery(
                        "select r1 from " + basicClass.getName() + " r1" +
                                " where r1.id not in" +
                                "(select r.id from Schedule s" +
                                " join s.room r " +
                                " where  s.period.id = :idOfPeriod" +
                                " and s.dayOfWeek = :dayOfWeek" +
                                " and s.evenOdd = :evenOdd)", Room.class)
                .setParameter("idOfPeriod", idOfPeriod)
                .setParameter(Constants.DAY_OF_WEEK, dayOfWeek)
                .setParameter(Constants.EVEN_ODD, evenOdd);
        return query.getResultList();
    }

    /**
     * Checks if room is used in schedule table.
     *
     * @param room the room to be checked
     * @return {@code true} if room is used in schedule table, otherwise {@code false}
     */
    @Override
    protected boolean checkReference(Room room) {
        log.info("In checkReference(room = [{}])", room);
        Long count = (Long) sessionFactory.getCurrentSession().createQuery(CHECK_REFERENCE)
                .setParameter(Constants.ROOM_ID, room.getId())
                .getSingleResult();
        return count != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Room> getNotAvailableRoomsForSchedule(Long semesterId, DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId) {
        log.info("Enter into getNotAvailableRooms with semesterId = {}, dayOfWeek = {}, evenOdd = {}, classId = {} ",
                semesterId, dayOfWeek, evenOdd, classId);
        Session session = getSession();
        if (evenOdd == EvenOdd.WEEKLY) {
            return session.createQuery(GET_NOT_AVAILABLE_ROOMS_FOR_SCHEDULE, Room.class)
                    .setParameter(Constants.SEMESTER_ID, semesterId)
                    .setParameter(Constants.DAY_OF_WEEK, dayOfWeek)
                    .setParameter(Constants.CLASS_ID, classId)
                    .getResultList();
        } else {
            return session.createQuery(GET_NOT_AVAILABLE_ROOMS_FOR_SCHEDULE_2, Room.class)
                    .setParameter(Constants.SEMESTER_ID, semesterId)
                    .setParameter(Constants.DAY_OF_WEEK, dayOfWeek)
                    .setParameter(Constants.CLASS_ID, classId)
                    .setParameter(Constants.EVEN_ODD, evenOdd)
                    .getResultList();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Room> getAvailableRoomsForSchedule(Long semesterId, DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId) {
        log.info("Enter into getAvailableRooms with semesterId = {}, dayOfWeek = {}, evenOdd = {}, classId = {} ",
                semesterId, dayOfWeek, evenOdd, classId);
        Session session = getSession();
        if (evenOdd == EvenOdd.WEEKLY) {
            return session.createQuery(GET_AVAILABLE_ROOMS_FOR_SCHEDULE, Room.class)
                    .setParameter(Constants.SEMESTER_ID, semesterId)
                    .setParameter(Constants.DAY_OF_WEEK, dayOfWeek)
                    .setParameter(Constants.CLASS_ID, classId)
                    .getResultList();
        } else {
            return session.createQuery(GET_AVAILABLE_ROOMS_FOR_SCHEDULE_2, Room.class)
                    .setParameter(Constants.SEMESTER_ID, semesterId)
                    .setParameter(Constants.DAY_OF_WEEK, dayOfWeek)
                    .setParameter(Constants.CLASS_ID, classId)
                    .setParameter(Constants.EVEN_ODD, evenOdd)
                    .getResultList();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countRoomDuplicates(Room room) {
        log.info("In countRoomDuplicates(room = [{}])", room);
        Session session = getSession();
        return (Long) session.createQuery(COUNT_ROOM_DUPLICATES)
                .setParameter("id", room.getId())
                .setParameter("name", room.getName())
                .setParameter("typeId", room.getType().getId())
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Room> getAllOrdered() {
        log.trace("In getAllOrdered()");
        return getSession()
                .createQuery(GET_ALL_ORDERED, Room.class)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(Long id) {
        log.trace("In exists(id = [{}])", id);
        return getSession()
                .createQuery(IS_EXISTS_BY_ID)
                .setParameter("id", id)
                .uniqueResult() != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Integer> getLastSortOrder() {
        log.trace("Entered getLastSortOrder()");
        return getSession().createQuery(GET_MAX_SORT_ORDER, Integer.class)
                .uniqueResultOptional();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shiftSortOrderRange(Integer lowerBound, Integer upperBound, Direction direction) {
        log.trace("Entered into shiftSortOrderRange with lowerBound = {}, upperBound = {}, direction = {} ", lowerBound, upperBound, direction);
        TypedQuery<Room> roomTypedQuery;
        Session session = sessionFactory.getCurrentSession();
        session.disableFilter("roomDisableFilter");
        if (upperBound != null) {
            roomTypedQuery = session.createQuery(MOVE_SORT_ORDER_RANGE);
            roomTypedQuery.setParameter("upperBound", upperBound);
        } else {
            roomTypedQuery = session.createQuery(MOVE_SORT_ORDER);
        }
        roomTypedQuery.setParameter("lowerPosition", lowerBound);

        if (direction == Direction.UP) {
            roomTypedQuery.setParameter("offset", -1);
        } else {
            roomTypedQuery.setParameter("offset", 1);
        }
        int updated = roomTypedQuery.executeUpdate();
        log.debug("Updated sortOrder of {} rooms", updated);
    }
}
