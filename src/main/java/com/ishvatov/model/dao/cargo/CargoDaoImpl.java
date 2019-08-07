package com.ishvatov.model.dao.cargo;


import com.ishvatov.model.dao.AbstractDao;
import com.ishvatov.model.entity.buisness.CargoEntity;
import org.springframework.stereotype.Repository;

/**
 * {@link CargoDao} implementation.
 *
 * @author Sergey Khvatov.
 */
@Repository("cargoDao")
public class CargoDaoImpl extends AbstractDao<Integer, CargoEntity> implements CargoDao {

    /**
     * Finds entity by it's unique id.
     *
     * @param key unique key of the id.
     * @return Unique entity with this id.
     */
    @Override
    public CargoEntity findByUniqueKey(Integer key) {
        return findById(key);
    }
}
