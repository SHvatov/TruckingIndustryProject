package com.ishvatov.service;

import java.util.List;

/**
 * Basic service interface.
 *
 * @param <T>  DTO type.
 * @param <UK> type of the unique key.
 */
public interface BaseService<UK, T> {

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
     * Updates entity from the DB.
     *
     * @param entity entity to delete.
     */
    void update(T entity);

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
     * @return Unique entity with this entity.
     */
    T findByUniqueKey(UK key);

    /**
     * Deletes entity by it's unique id.
     *
     * @param key unique key of the entity.
     */
    void deleteByUniqueKey(UK key);

    /**
     * Checks if the input key is unique or not.
     *
     * @param key key to check.
     * @return true, if this key is unique in the DB, false otherwise.
     */
    boolean isUniqueKey(UK key);
}
