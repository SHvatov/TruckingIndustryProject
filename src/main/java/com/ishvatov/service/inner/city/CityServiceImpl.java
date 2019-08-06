package com.ishvatov.service.inner.city;

import com.ishvatov.exception.DAOException;
import com.ishvatov.mapper.Mapper;
import com.ishvatov.model.dao.city.CityDao;
import com.ishvatov.model.dto.CityDto;
import com.ishvatov.model.entity.buisness.CityEntity;
import com.ishvatov.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Basic {@link CityService} interface implementation.
 *
 * @author Sergey Khvatov
 */
@Service("cityService")
public class CityServiceImpl extends AbstractService<String, CityEntity, CityDto> implements CityService {

    /**
     * Autowired DAO field.
     */
    private CityDao cityDao;

    /**
     * Default class constructor, that is used
     * to inject DAO interface implementation and
     * initialize the super class.
     *
     * @param mapper  {@link Mapper} implementation.
     * @param cityDao autowired {@link CityDao} impl.
     */
    @Autowired
    public CityServiceImpl(CityDao cityDao, Mapper<CityEntity, CityDto> mapper) {
        super(cityDao, mapper);
        this.cityDao = cityDao;
    }

    /**
     * Adds entity to the DB. Check if entity already exists.
     *
     * @param dtoObj new entity to add.
     * @throws DAOException if entity with this UID already exists
     */
    @Override
    public void save(CityDto dtoObj) {
        if (cityDao.exists(dtoObj.getUniqueIdentificator())) {
            throw new DAOException(getClass(), "save", "Entity with such UID already exists");
        } else {
            // create new instance
            CityEntity entity = new CityEntity();

            // set UID
            entity.setUniqueIdentificator(dtoObj.getUniqueIdentificator());

            // save entity
            cityDao.save(entity);
        }
    }
}
