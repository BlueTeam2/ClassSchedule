package com.softserve.service.impl;

import com.softserve.dto.ScheduleFullForArchiveDTO;
import com.softserve.dto.SemesterDTO;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.repository.ArchiveRepository;
import com.softserve.service.ArchiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@Slf4j
public class ArchiveServiceImpl implements ArchiveService {

    private final ArchiveRepository archiveRepository;

    @Autowired
    public ArchiveServiceImpl(ArchiveRepository archiveRepository) {
        this.archiveRepository = archiveRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduleFullForArchiveDTO getArchiveScheduleBySemesterId(Long semesterId) {
        log.info("In getArchiveScheduleBySemesterId with semesterId = {}", semesterId);
        return archiveRepository.getArchiveScheduleBySemesterId(semesterId).orElseThrow(
                () -> new EntityNotFoundException(ScheduleFullForArchiveDTO.class, "semesterId", semesterId.toString())
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SemesterDTO> getAllSemestersInArchiveSchedule() {
        log.info("In getAllSemestersInArchiveSchedule");
        List<ScheduleFullForArchiveDTO> schedules = archiveRepository.getAllArchiveSchedule();
        List<SemesterDTO> semesters = new ArrayList<>();
        for (ScheduleFullForArchiveDTO schedule : schedules) {
            semesters.add(schedule.getSemester());
        }
        return semesters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduleFullForArchiveDTO saveScheduleForArchive(ScheduleFullForArchiveDTO scheduleFullForArchiveDTO) {
        log.info("In saveScheduleForArchive with scheduleForArchiveDTO = {}", scheduleFullForArchiveDTO);
        return archiveRepository.saveScheduleForArchive(scheduleFullForArchiveDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteArchiveScheduleBySemesterId(Long semesterId) {
        log.info("In removeArchiveScheduleBySemesterId with semesterId = {}", semesterId);
        archiveRepository.deleteArchiveScheduleBySemesterId(semesterId);
    }
}
