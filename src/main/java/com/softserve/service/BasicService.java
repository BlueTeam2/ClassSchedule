package com.softserve.service;

import java.io.Serializable;
import java.util.List;


interface BasicService<T extends Serializable, I extends Serializable> {

    /**
     * Retrieves an entity by the given id from the repository.
     *
     * @param id the id of the entity
     * @return the entity with the given id
     * @throws com.softserve.exception.EntityNotFoundException if entity not found
     */
    T getById(I id);

    /**
     * Returns all entities from the repository.
     *
     * @return the list of entities
     */
    List<T> getAll();

    /**
     * Saves a given entity in the repository.
     *
     * @param object the entity to be saved
     * @return the saved entity
     */
    T save(T object);

    /**
     * Updates a given entity in the repository.
     *
     * @param object the entity to be updated
     * @return the updated entity
     */
    T update(T object);

    /**
     * Deletes a given entity from the repository.
     *
     * @param object the entity to be deleted
     * @return the deleted entity
     * @throws com.softserve.exception.DeleteDisabledException if there are still references to given object
     */
    T delete(T object);
}
