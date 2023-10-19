package com.softserve.service;

import com.softserve.dto.ScheduleFullForArchiveDTO;
import com.softserve.dto.SemesterDTO;

import java.util.List;

public interface ArchiveService {

    /**
     * Returns a schedule with given semester id.
     *
     * @param semesterId the id of the semester for getting schedule by this id
     * @return the schedule with given semester id
     * @throws com.softserve.exception.EntityNotFoundException if schedule by given semester id not found
     */
    ScheduleFullForArchiveDTO getArchiveScheduleBySemesterId(Long semesterId);

    /**
     * Returns all semesters in the archived schedules.
     *
     * @return the list of semesters
     */
    List<SemesterDTO> getAllSemestersInArchiveSchedule();

    /**
     * Saves given schedule in archive.
     *
     * @param scheduleFullForArchiveDTO the schedule to save in archive
     * @return the saved schedule
     */
    ScheduleFullForArchiveDTO saveScheduleForArchive(ScheduleFullForArchiveDTO scheduleFullForArchiveDTO);

    /**
     * Deletes schedule from the archive by semester id.
     *
     * @param semesterId the id of the semester to be deleted
     */
    void deleteArchiveScheduleBySemesterId(Long semesterId);
}
