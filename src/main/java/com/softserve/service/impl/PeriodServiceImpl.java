package com.softserve.service.impl;

import com.softserve.entity.Period;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.exception.FieldAlreadyExistsException;
import com.softserve.exception.IncorrectTimeException;
import com.softserve.exception.PeriodConflictException;
import com.softserve.repository.PeriodRepository;
import com.softserve.service.PeriodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
public class PeriodServiceImpl implements PeriodService {

    private final PeriodRepository periodRepository;

    @Autowired
    public PeriodServiceImpl(PeriodRepository periodRepository) {
        this.periodRepository = periodRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Period getById(Long id) {
        log.info("Enter into getById of PeriodServiceImpl with id {}", id);
        return periodRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(Period.class, "id", id.toString())
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Period> getAll() {
        log.info("Enter into getAll of PeriodServiceImpl");
        return periodRepository.getAll();
    }

    /**
     * {@inheritDoc}
     *
     * @throws IncorrectTimeException      if the start time of the period was after its end or the start time was equal to the end time
     * @throws PeriodConflictException     if given period intersect with other periods
     * @throws FieldAlreadyExistsException if period's name already exists
     */
    @Override
    public Period save(Period object) {
        log.info("Enter into save of PeriodServiceImpl with entity: {}", object);
        if (isTimeInvalid(object)) {
            throw new IncorrectTimeException("Incorrect time in period");
        }
        if (isPeriodFree(getAll(), object)) {
            if (nameExists(object.getName())) {
                throw new FieldAlreadyExistsException(Period.class, "name", object.getName());
            }
            return periodRepository.save(object);
        } else {
            throw new PeriodConflictException("Your period has conflict with already existed periods");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Period> save(List<Period> periods) {
        log.info("Enter into save of PeriodServiceImpl with entities:{}", periods);
        if (periods.stream().anyMatch(this::isTimeInvalid)) {
            throw new IncorrectTimeException("Incorrect time in period");
        }
        if (isListOfPeriodsFree(getAll(), periods)) {
            return periods.stream().map(periodRepository::save).collect(Collectors.toList());
        } else {
            throw new PeriodConflictException("Some periods have conflict with already existed periods");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws FieldAlreadyExistsException if periods name already exists
     * @throws PeriodConflictException     if given period intersect with other periods
     */
    @Override
    public Period update(Period object) {
        log.info("Enter into update of PeriodServiceImpl with entity:{}", object);
        if (isTimeInvalid(object)) {
            throw new IncorrectTimeException("incorrect time in period");
        }
        if (isPeriodFree(getAll(), object)) {
            getById(object.getId());
            periodRepository.findByName(object.getName()).ifPresent(period -> {
                if (!Objects.equals(period.getId(), object.getId())) {
                    throw new FieldAlreadyExistsException(Period.class, "name", object.getName());
                }
            });
            return periodRepository.update(object);
        } else {
            throw new PeriodConflictException("your period has conflict with already existed periods");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Period delete(Period object) {
        log.info("Enter into delete of PeriodServiceImpl with entity:{}", object);
        return periodRepository.delete(object);
    }

    private boolean isListOfPeriodsFree(List<Period> oldPeriods, List<Period> newPeriods) {
        log.info("Enter into isListOfPeriodsFree of PeriodServiceImpl with entities " +
                        "oldPeriods: {}, newPeriods: {}",
                oldPeriods, newPeriods);
        for (Period newPeriod : newPeriods) {
            if (!isPeriodFree(newPeriods, newPeriod) || !isPeriodFree(oldPeriods, newPeriod)) {
                return false;
            }
        }
        return true;
    }

    private boolean isPeriodFree(List<Period> oldPeriods, Period newPeriod) {
        log.info("Enter into isPeriodFree of PeriodServiceImpl with entities oldPeriods: {} and newPeriod: {}",
                oldPeriods, newPeriod);
        return oldPeriods.stream().noneMatch(oldPeriod ->
                (isPeriodsGlued(newPeriod, oldPeriod) || isPeriodsIntersect(newPeriod, oldPeriod)) &&
                        !Objects.equals(newPeriod.getId(), oldPeriod.getId())
        );
    }

    private boolean isPeriodsIntersect(Period newPeriod, Period oldPeriod) {
        log.info("Enter into isPeriodsIntersect of PeriodServiceImpl with entities oldPeriod: {}, newPeriod: {}",
                oldPeriod, newPeriod);
        return newPeriod.getStartTime().
                isBefore(oldPeriod.getEndTime())
                && newPeriod.getEndTime().
                isAfter(oldPeriod.getStartTime())
                && !newPeriod.equals(oldPeriod);
    }

    private boolean isPeriodsGlued(Period newPeriod, Period oldPeriod) {
        log.info("Enter into isPeriodsGlued of PeriodServiceImpl with entities oldPeriods: {}, newPeriods: {}",
                oldPeriod, newPeriod);
        return newPeriod.getStartTime().equals(oldPeriod.getEndTime())
                || newPeriod.getEndTime().equals(oldPeriod.getStartTime());
    }

    private boolean isTimeInvalid(Period object) {
        log.info("Enter into isTimeInvalid of PeriodServiceImpl with entity: {}", object);
        return object.getStartTime().isAfter(object.getEndTime()) ||
                object.getStartTime().equals(object.getEndTime());
    }

    /**
     * Checks if the period with given name exists in the repository.
     *
     * @param name the string represents tha namen of the period
     * @return {@code true} if the period with given name exists, otherwise {@code false}
     */
    private boolean nameExists(String name) {
        log.info("Enter into nameExists method with name:{}", name);
        return periodRepository.findByName(name).isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Period> getFirstFourPeriods() {
        return periodRepository.getFistFourPeriods();
    }
}


