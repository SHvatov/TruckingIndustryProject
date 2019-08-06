package com.ishvatov.service;

import com.ishvatov.exception.DAOException;
import com.ishvatov.mapper.Mapper;
import com.ishvatov.model.dao.BaseDaoInterface;
import com.ishvatov.model.dto.BaseDtoInterface;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract service class with default methods implementations.
 *
 * @param <U>  type of the unique key.
 * @param <T1> entity type.
 * @param <T2> dto type.
 * @author Sergey Khvatov
 */
public abstract class AbstractService<U, T1, T2 extends BaseDtoInterface<U>> implements BaseService<U, T2> {

    /**
     * Reference to the implementation of the
     * {@link BaseDaoInterface} with minimal
     * functionality to access the DAO layer.
     */
    private final BaseDaoInterface<U, T1> daoInterface;

    /**
     * {@link Mapper} interface implementation.
     */
    private Mapper<T1, T2> mapper;

    /**
     * Default class constructor, which
     * is used to init the criteria building system..
     */
    @SuppressWarnings("unchecked")
    protected AbstractService(BaseDaoInterface<U, T1> daoInterface, Mapper<T1, T2> mapper) {
        this.daoInterface = daoInterface;
        this.mapper = mapper;
    }

    /**
     * Deletes entity from the DB if it exists.
     *
     * @param dtoObj entity to delete.
     */
    @Transactional
    @Override
    public void delete(T2 dtoObj) {
        if (daoInterface.exists(dtoObj.getUniqueIdentificator())) {
            T1 entity = daoInterface.findByUniqueKey(dtoObj.getUniqueIdentificator());
            daoInterface.delete(entity);
        }
    }

    /**
     * Finds entity by it's id in the DB.
     *
     * @param id id of the entity in DB.
     * @return T2 object with this unique id.
     * @throws DAOException if no entity with such id exists.
     */
    @Transactional
    public T2 findById(int id) {
        T1 entity = daoInterface.findById(id);
        if (entity == null) {
            throw new DAOException(getClass(), "findById", "No entity with such id");
        } else {
            return mapper.map(entity);
        }
    }

    /**
     * Finds all the entities in the DB.
     *
     * @return list with all the entities, empty list if nothing found.
     */
    @Transactional
    public List<T2> findAll() {
        return daoInterface.findAll().stream().map(mapper::map).collect(Collectors.toList());
    }

    /**
     * Finds entity by it's unique id.
     *
     * @param key unique key of the id.
     * @return Unique entity with this entity.
     * @throws DAOException if no entity with such unique key exists.
     */
    @Transactional
    public T2 findByUniqueKey(U key) {
        T1 entity = daoInterface.findByUniqueKey(key);
        if (entity == null) {
            throw new DAOException(getClass(), "findByUniqueKey", "No entity with such unique key exists.");
        } else {
            return mapper.map(entity);
        }
    }

    /**
     * Deletes entity by it's unique id if it exists.
     *
     * @param key unique key of the entity.
     */
    @Transactional
    public void deleteByUniqueKey(U key) {
        T1 entity = daoInterface.findByUniqueKey(key);
        if (entity != null) {
            daoInterface.delete(entity);
        }
    }

    /**
     * Checks if the input key is unique or not.
     *
     * @param key key to check.
     * @return true, if this key is unique in the DB, false otherwise.
     */
    @Transactional
    public boolean isUniqueKey(U key) {
        return !daoInterface.exists(key);
    }
}
