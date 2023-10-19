package com.softserve.repository.impl;

import com.softserve.entity.Period;
import com.softserve.repository.PeriodRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@SuppressWarnings("unchecked")
public class PeriodRepositoryImpl extends BasicRepositoryImpl<Period, Long> implements PeriodRepository {

    /**
     * Returns all periods from database ordered by start time.
     *
     * @return the list of periods ordered by start time
     */
    @Override
    public List<Period> getAll() {
        log.info("Enter into getAll of PeriodRepositoryImpl");
        return sessionFactory.getCurrentSession().
                createQuery("select p from " + basicClass.getName() + " p" +
                        " order by p.startTime ASC").getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Period update(Period entity) {
        sessionFactory.getCurrentSession().clear();
        return super.update(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Period> findByName(String name) {
        log.info("Enter into findByName method with name: {}", name);
        TypedQuery<Period> query = sessionFactory.getCurrentSession()
                .createNamedQuery("findName", Period.class)
                .setMaxResults(1)
                .setParameter("name", name);
        List<Period> periods = query.getResultList();
        if (periods.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(query.getResultList().get(0));
    }

    /**
     * Checks if period is used in Schedule tables.
     *
     * @param period the period to be checked
     * @return {@code true} if period is used in Schedule tables, otherwise {@code false}
     */
    @Override
    protected boolean checkReference(Period period) {
        log.info("In checkReference(period = [{}])", period);
        Long count = (Long) sessionFactory.getCurrentSession().createQuery(
                        "select count (s.id) " +
                                "from Schedule s where s.period.id = :periodId")
                .setParameter("periodId", period.getId())
                .getSingleResult();
        return count != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Period> getFistFourPeriods() {
        log.info("In getFistFourPeriods()");
        return sessionFactory.getCurrentSession().createQuery("select p from Period p order by startTime")
                .setMaxResults(4)
                .getResultList();
    }
}
