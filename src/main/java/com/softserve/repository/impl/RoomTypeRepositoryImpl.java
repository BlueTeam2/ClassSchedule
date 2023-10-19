package com.softserve.repository.impl;

import com.softserve.entity.RoomType;
import com.softserve.repository.RoomTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RoomTypeRepositoryImpl extends BasicRepositoryImpl<RoomType, Long> implements RoomTypeRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countRoomTypesWithDescription(String description) {
        log.info("In countRoomTypesWithDescription(description = [{}])", description);
        return (Long) sessionFactory.getCurrentSession().createQuery("SELECT count (*) FROM RoomType r WHERE r.description = :description")
                .setParameter("description", description).getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countByRoomTypeId(Long id) {
        log.info("In countByRoomTypeId(id = [{}])", id);
        return (Long) sessionFactory.getCurrentSession().createQuery("SELECT count (*) FROM RoomType r WHERE r.id = :id")
                .setParameter("id", id).getSingleResult();
    }

    /**
     * Checks if given type of room is used in Room table.
     *
     * @param roomType the type of room to be checked
     * @return {@code true} if type of room is used in Room table, otherwise {@code false}
     */
    @Override
    protected boolean checkReference(RoomType roomType) {
        log.info("In checkReference(roomType = [{}])", roomType);
        Long count = (Long) sessionFactory.getCurrentSession().createQuery(
                        "select count (r.id) " +
                                "from Room r where r.type.id = :roomTypeId")
                .setParameter("roomTypeId", roomType.getId())
                .getSingleResult();
        return count != 0;
    }
}
