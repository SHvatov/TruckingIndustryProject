package com.ishvatov.service.inner.city_map;

import com.ishvatov.exception.CustomProjectException;
import com.ishvatov.exception.DAOException;
import com.ishvatov.exception.ValidationException;
import com.ishvatov.mapper.Mapper;
import com.ishvatov.model.dao.city.CityDao;
import com.ishvatov.model.dto.CityDto;
import com.ishvatov.model.entity.buisness.CityEntity;
import com.ishvatov.service.inner.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic {@link CityMapService} interface implementation.
 *
 * @author Sergey Khvatov
 */
@Service("cityMapService")
@Transactional
public class CityMapServiceImpl extends AbstractService<String, CityEntity, CityDto> implements CityMapService {

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
    public CityMapServiceImpl(CityDao cityDao, Mapper<CityEntity, CityDto> mapper) {
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
        throw new CustomProjectException(getClass(), "save", "Save method is not supported");
    }

    /**
     * Updates data in the database. Updates
     * entity fields using all not-null fields from the
     * DTO object.
     *
     * @param dtoObj values to update in the entity.
     * @throws DAOException if entity with this UID already exists
     */
    @Override
    public void update(CityDto dtoObj) {
        throw new CustomProjectException(getClass(), "update", "Update method is not supported");
    }

    /**
     * Deletes entity from the DB if it exists.
     *
     * @param key UID of the entity.
     */
    @Override
    public void delete(String key) {
        throw new CustomProjectException(getClass(), "delete", "Delete method is not supported");
    }

    /**
     * Validates the input DTO object and throws NPE,
     * if object or one of required fields is null.
     *
     * @param dto DTO object.
     */
    private void validateRequiredFields(CityDto dto) {
        if (dto == null || dto.getUniqueIdentificator() == null) {
            throw new ValidationException();
        }
    }
}
