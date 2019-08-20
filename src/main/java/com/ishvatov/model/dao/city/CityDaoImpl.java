package com.ishvatov.model.dao.city;

import com.ishvatov.model.dao.AbstractDao;
import com.ishvatov.model.entity.buisness.CityEntity;
import org.springframework.stereotype.Repository;

/**
 * {@link CityDao} implementation.
 *
 * @author Sergey Khvatov
 */
@Repository("cityDao")
public class CityDaoImpl extends AbstractDao<String, CityEntity> implements CityDao {
    // empty
}
