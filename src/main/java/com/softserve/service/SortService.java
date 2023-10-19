package com.softserve.service;

public interface SortService<T> {

    /**
     * Saves T after the specific row to get desired order.
     *
     * @param t   entity that must be saved
     * @param afterId the id of the group after which must be saved the new one
     * @return the saved entity with set order and id
     */
    T createAfterOrder(T t, Long afterId);

    /**
     * Updates the sort order of the given entity and adjacent entities in sort order to place the given entity after the entity with specified id.
     * If afterId equal {@code zero}, places the entity at the first position.
     *
     * @param t    the entity to be updated
     * @param afterId the id of the entity after which the given entity will be placed. May be {@code null}
     * @return the updated entity with a set sort order
     * @throws com.softserve.exception.EntityNotFoundException if given entity not found
     */
    T updateAfterOrder(T t, Long afterId);

}
