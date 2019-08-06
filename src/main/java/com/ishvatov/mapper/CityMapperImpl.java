package com.ishvatov.mapper;

import com.ishvatov.model.dto.CityDto;
import com.ishvatov.model.entity.buisness.CityEntity;
import org.springframework.stereotype.Component;

/**
 * {@link Mapper} interface implementation for city.
 *
 * @author Sergey Khvatov
 */
@Component
public class CityMapperImpl implements Mapper<CityEntity, CityDto> {

    /**
     * Maps existing entity object from DB to DTO.
     *
     * @param src entity object.
     * @return new DTO initialized with values from entity.
     */
    @Override
    public CityDto map(CityEntity src) {
        return null;
    }
}
