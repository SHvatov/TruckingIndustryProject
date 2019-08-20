package com.ishvatov.service.inner;

import com.ishvatov.exception.DAOException;
import com.ishvatov.exception.ValidationException;
import com.ishvatov.mapper.Mapper;
import com.ishvatov.model.dao.BaseDaoInterface;
import com.ishvatov.model.dto.BaseDtoInterface;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Abstract service class with default methods implementations.
 *
 * @param <U>  type of the unique key.
 * @param <T1> entity type.
 * @param <T2> dto type.
 * @author Sergey Khvatov
 */
@Transactional
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
    protected AbstractService(BaseDaoInterface<U, T1> daoInterface, Mapper<T1, T2> mapper) {
        this.daoInterface = daoInterface;
        this.mapper = mapper;
    }

    /**
     * Finds entity by it's id in the DB.
     *
     * @param id id of the entity in DB.
     * @return T2 object with this unique id.
     * @throws DAOException if no entity with such id exists.
     */
    @Override
    public T2 findById(int id) {
        Optional<T1> entity = Optional.ofNullable(daoInterface.findById(id));
        return mapper.map(
            entity.orElseThrow(
                () -> new DAOException(getClass(), "findById", "No entity with such unique key exists.")
            )
        );
    }

    /**
     * Finds all the entities in the DB.
     *
     * @return list with all the entities, empty list if nothing found.
     */
    @Override
    public List<T2> findAll() {
        return daoInterface.findAll()
            .stream()
            .filter(Objects::nonNull)
            .map(mapper::map)
            .collect(Collectors.toList());
    }

    /**
     * Finds entity by it's unique id.
     *
     * @param key unique key of the id.
     * @return Unique entity with this entity.
     * @throws DAOException if no entity with such unique key exists.
     */
    @Override
    public T2 find(U key) {
        Optional<T1> entity = Optional.ofNullable(
            daoInterface.findByUniqueKey(
                Optional.ofNullable(key).orElseThrow(
                    () -> new ValidationException(getClass(), "find", "Key is null")
                )
            )
        );
        return mapper.map(
            entity.orElseThrow(
                () -> new DAOException(getClass(), "find", "No entity with such unique key exists.")
            )
        );
    }

    /**
     * Checks if the input key is unique or not.
     *
     * @param key key to check.
     * @return true, if this key is unique in the DB, false otherwise.
     * @throws ValidationException if key is null.
     */
    @Override
    public boolean exists(U key) {
        if (key == null) {
            throw new ValidationException();
        } else {
            return daoInterface.exists(key);
        }
    }
}
