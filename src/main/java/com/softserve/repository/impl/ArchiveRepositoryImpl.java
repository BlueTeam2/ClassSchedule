package com.softserve.repository.impl;

import com.softserve.dto.ScheduleFullForArchiveDTO;
import com.softserve.repository.ArchiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class ArchiveRepositoryImpl implements ArchiveRepository {

    private final MongoOperations mongoOperations;

    @Autowired
    public ArchiveRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ScheduleFullForArchiveDTO> getArchiveScheduleBySemesterId(Long semesterId) {
        log.info("In getArchiveScheduleBySemesterId with semesterId = {}", semesterId);
        return Optional.ofNullable(
                mongoOperations.findOne(new Query().addCriteria(Criteria.where("semester.id").is(semesterId)), ScheduleFullForArchiveDTO.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScheduleFullForArchiveDTO> getAllArchiveSchedule() {
        log.info("In getAllArchiveSchedule");
        return mongoOperations.findAll(ScheduleFullForArchiveDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduleFullForArchiveDTO saveScheduleForArchive(ScheduleFullForArchiveDTO fullScheduleForArchiveDTO) {
        log.info("In saveScheduleForArchive with scheduleForArchiveDTO = {}", fullScheduleForArchiveDTO);
        return mongoOperations.insert(fullScheduleForArchiveDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteArchiveScheduleBySemesterId(Long semesterId) {
        log.info("In removeArchiveScheduleBySemesterId with semesterId = {}", semesterId);
        mongoOperations.remove(new Query().addCriteria(Criteria.where("semester.id").is(semesterId)), ScheduleFullForArchiveDTO.class);
    }
}
