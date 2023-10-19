package com.softserve.service;

import com.softserve.dto.RoomForScheduleInfoDTO;
import com.softserve.entity.Room;
import com.softserve.entity.enums.EvenOdd;

import java.time.DayOfWeek;
import java.util.List;

public interface RoomService extends BasicService<Room, Long>, SortService<Room> {

    /**
     * Returns all free rooms by the given day of the week, period and type of the week.
     *
     * @param idOfPeriod the id of the period
     * @param dayOfWeek  the day of the week
     * @param evenOdd    the type of the week
     * @return the list of free rooms
     */
    List<Room> freeRoomBySpecificPeriod(Long idOfPeriod, DayOfWeek dayOfWeek, EvenOdd evenOdd);

    /**
     * Returns all not available rooms by given semester id, day of the week, type of the week and class id.
     *
     * @param semesterId the id of the semester
     * @param dayOfWeek  the day of the week
     * @param evenOdd    the type of the week
     * @param classId    the id of the class
     * @return the list of not available rooms
     */
    List<Room> getNotAvailableRoomsForSchedule(Long semesterId, DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId);

    /**
     * Returns all available rooms by given semester id, day of the week, type of the week and class id.
     *
     * @param semesterId the id of the semester
     * @param dayOfWeek  the day of the week
     * @param evenOdd    the type of the week
     * @param classId    the id of the class
     * @return the list of available rooms
     */
    List<Room> getAvailableRoomsForSchedule(Long semesterId, DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId);

    /**
     * Returns all rooms by given semester id, day of the week, type of the week and class id.
     *
     * @param semesterId the id of the semester
     * @param dayOfWeek  the day of the week
     * @param evenOdd    the type of the week
     * @param classId    the id of the class
     * @return the list of rooms
     */
    List<RoomForScheduleInfoDTO> getAllRoomsForCreatingSchedule(Long semesterId, DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId);

    /**
     * Checks if the given room already exists in the repository.
     *
     * @param room the room entity that needs to be checked
     * @return {@code true} if the given room already exists in the repository.
     */
    boolean isRoomExists(Room room);

    /**
     * Returns all disabled rooms.
     *
     * @return the list of disabled rooms
     */
    List<Room> getDisabled();

    /**
     * Returns all rooms ordered by sortOrder.
     *
     * @return the list of rooms ordered by sortOrder
     */
    List<Room> getAllOrdered();
}
