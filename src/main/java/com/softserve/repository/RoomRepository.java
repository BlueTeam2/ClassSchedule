package com.softserve.repository;

import com.softserve.entity.Room;
import com.softserve.entity.enums.EvenOdd;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends BasicRepository<Room, Long> {

    enum Direction {
        UP,
        DOWN
    }

    /**
     * Returns all free rooms by specific period, day of week and number of week from database.
     *
     * @param idOfPeriod the id of the period
     * @param dayOfWeek  the day of the week
     * @param evenOdd    the type of the week
     * @return the list of rooms
     */
    List<Room> freeRoomBySpecificPeriod(Long idOfPeriod, DayOfWeek dayOfWeek, EvenOdd evenOdd);

    /**
     * Returns all rooms that are not available.
     *
     * @param semesterId the id of the semester
     * @param dayOfWeek  the day of the week
     * @param evenOdd    the type of the week
     * @param classId    the id of the class
     * @return the list of not available rooms
     */
    List<Room> getNotAvailableRoomsForSchedule(Long semesterId, DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId);

    /**
     * Returns all available rooms.
     *
     * @param semesterId the id of the semester
     * @param dayOfWeek  the day of the week
     * @param evenOdd    the type of the week
     * @param classId    the id of the class
     * @return the list of available rooms
     */
    List<Room> getAvailableRoomsForSchedule(Long semesterId, DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId);

    /**
     * Returns the number of duplicates of given room.
     *
     * @param room the room entity that needs to be count
     * @return the number of duplicates of given room
     */
    Long countRoomDuplicates(Room room);

    /**
     * {@inheritDoc}
     */
    List<Room> getDisabled();

    /**
     * Returns all rooms from database ordered by sortOrder.
     *
     * @return the list of the rooms ordered by sortOrder
     */
    List<Room> getAllOrdered();

    /**
     * Returns {@code true} if room with the given id exists.
     *
     * @param id the id of the room
     * @return {@code true} if room with given id exists, otherwise {@code false}
     */
    boolean exists(Long id);

    /**
     * Returns the last occupied position in sort order.
     *
     * @return an Optional describing the last occupied position in sort order
     */
    Optional<Integer> getLastSortOrder();

    /**
     * Shifts the sort order range by 1 in the specified direction. Bounds are included.
     *
     * @param lowerBound the lower bound of sort order
     * @param upperBound the upper bound of sort order. May be {@code null}
     * @param direction  the direction of shift
     */
    void shiftSortOrderRange(Integer lowerBound, Integer upperBound, Direction direction);

}
