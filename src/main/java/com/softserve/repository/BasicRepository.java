package com.softserve.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BasicRepository<T extends Serializable, I extends Serializable> {

    /**
     * Retrieves an entity by its id from the database.
     *
     * @param id the id of the entity
     * @return an Optional describing the entity with the given id or an empty Optional if none found
     */
    Optional<T> findById(I id);

    /**
     * Returns all entities from database.
     *
     * @return the list of entities
     */
    List<T> getAll();

    /**
     * Saves the given entity in the database.
     *
     * @param entity the entity to be saved
     * @return the saved entity
     */
    T save(T entity);

    /**
     * Updates the given entity in the database.
     *
     * @param entity the entity to be updated
     * @return the updated entity
     */
    T update(T entity);

    /**
     * Deletes the given entity from the database.
     *
     * @param entity the entity to be deleted
     * @return the deleted entity
     * @throws com.softserve.exception.DeleteDisabledException if there are still references to given entity
     */
    T delete(T entity);

    /**
     * Returns all disabled entities.
     *
     * @return the list of disabled entities
     */
    List<T> getDisabled();
}
