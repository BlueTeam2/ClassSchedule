package com.softserve.repository;

import com.softserve.entity.Period;

import java.util.List;
import java.util.Optional;

public interface PeriodRepository extends BasicRepository<Period, Long> {

    /**
     * Retrieves period with given name from database.
     *
     * @param name the string represent name of period
     * @return an Optional describing the period with given name or an empty Optional if none found
     */
    Optional<Period> findByName(String name);

    /**
     * Returns the list of first four periods.
     *
     * @return the list of periods
     */
    List<Period> getFistFourPeriods();
}
