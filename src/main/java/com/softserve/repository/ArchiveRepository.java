package com.softserve.repository;

import com.softserve.dto.ScheduleFullForArchiveDTO;

import java.util.List;
import java.util.Optional;


public interface ArchiveRepository {

    /**
     * Retrieves schedule from MongoDB by semester id.
     *
     * @param semesterId the id of the semester for getting a schedule by this id from MongoDB
     * @return an Optional describing the schedule with the specified semester id, otherwise an empty Optional
     */
    Optional<ScheduleFullForArchiveDTO> getArchiveScheduleBySemesterId(Long semesterId);

    /**
     * Returns all archived schedules from MongoDB.
     *
     * @return the list of schedules
     */
    List<ScheduleFullForArchiveDTO> getAllArchiveSchedule();

    /**
     * Saves a given schedule in MongoDB.
     *
     * @param fullScheduleForArchiveDTO the schedule to save in MongoDB
     * @return the saved schedule
     */
    ScheduleFullForArchiveDTO saveScheduleForArchive(ScheduleFullForArchiveDTO fullScheduleForArchiveDTO);

    /**
     * Deletes schedule from MongoDB database with the given id.
     *
     * @param semesterId the id of the semester. Uses for delete schedule by this id from MongoDB
     */
    void deleteArchiveScheduleBySemesterId(Long semesterId);
}
