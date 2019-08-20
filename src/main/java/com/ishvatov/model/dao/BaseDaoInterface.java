package com.ishvatov.model.dao;

import java.util.List;

/**
 * Base DAO interface.
 *
 * @param <U> type of the unique key.
 * @param <T> type of the entity.
 */
public interface BaseDaoInterface<U, T> {

    /**
     * Finds entity by it's id in the DB.
     *
     * @param id id of the entity in DB.
     * @return T object with this unique id.
     */
    T findById(int id);

    /**
     * Adds entity to the DB.
     *
     * @param entity new entity to add.
     */
    void save(T entity);

    /**
     * Deletes entity from the DB.
     *
     * @param entity entity to delete.
     */
    void delete(T entity);

    /**
     * Deletes entity by it's id.
     *
     * @param id id of the entity.
     */
    void deleteById(int id);

    /**
     * Finds all the entities in the DB.
     *
     * @return list with all the entities.
     */
    List<T> findAll();

    /**
     * Finds entity by it's unique id.
     *
     * @param key unique key of the id.
     * @return Unique entity with this id.
     */
    T findByUniqueKey(U key);

    /**
     * Checks if entity with such key exists in teh database.
     *
     * @param key unique key of the id.
     * @return true, if exists, false otherwise.
     */
    boolean exists(U key);
}
