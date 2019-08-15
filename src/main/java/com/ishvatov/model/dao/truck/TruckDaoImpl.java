package com.ishvatov.model.dao.truck;

import com.ishvatov.model.dao.AbstractDao;
import com.ishvatov.model.entity.buisness.TruckEntity;
import org.springframework.stereotype.Repository;

/**
 * {@link TruckDao} implementation.
 *
 * @author Sergey Khvatov
 */
@Repository("truckDao")
public class TruckDaoImpl extends AbstractDao<String, TruckEntity> implements TruckDao {
}
