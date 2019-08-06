package com.ishvatov.model.dao.driver;

import com.ishvatov.model.dao.AbstractDao;
import com.ishvatov.model.entity.buisness.DriverEntity;
import org.springframework.stereotype.Repository;

/**
 * {@link DriverDao} implementation.
 *
 * @author Sergey Khvatov
 */
@Repository("driverDao")
public class DriverDaoImpl extends AbstractDao<String, DriverEntity> implements DriverDao {
}
