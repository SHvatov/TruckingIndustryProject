package com.ishvatov.service;

import com.ishvatov.exception.DAOException;
import com.ishvatov.model.dto.BaseDtoInterface;

import java.util.List;

/**
 * Basic service interface.
 *
 * @param <T> DTO type.
 * @param <U> type of the unique key.
 */
public interface BaseService<U, T extends BaseDtoInterface<U>> {

    /**
     * Finds entity by it's id in the DB.
     *
     * @param id id of the entity in DB.
     * @return T object with this unique id.
     * @throws DAOException if no entity with such id exists.
     */
    T findById(int id);

    /**
     * Adds entity to the DB. Check if entity already exists.
     *
     * @param dtoObj new entity to add.
     * @throws DAOException if entity with this UID already exists
     */
    void save(T dtoObj);

    /**
     * Deletes entity from the DB if it exists.
     *
     * @param dtoObj entity to delete.
     */
    void delete(T dtoObj);

    /**
     * Finds all the entities in the DB.
     *
     * @return list with all the entities, empty list if nothing found.
     */
    List<T> findAll();

    /**
     * Finds entity by it's unique id.
     *
     * @param key unique key of the id.
     * @return Unique entity with this entity.
     * @throws DAOException if no entity with such unique key exists.
     */
    T findByUniqueKey(U key);

    /**
     * Deletes entity by it's unique id if it exists.
     *
     * @param key unique key of the entity.
     */
    void deleteByUniqueKey(U key);

    /**
     * Checks if the input key is unique or not.
     *
     * @param key key to check.
     * @return true, if this key is unique in the DB, false otherwise.
     */
    boolean isUniqueKey(U key);
}
