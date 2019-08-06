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
public class CargoDaoImpl extends AbstractDao<String, CargoEntity> implements CargoDao {
    // empty
}
