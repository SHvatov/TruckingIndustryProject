package com.ishvatov.mapper;

import com.ishvatov.model.dto.CityMapDto;
import com.ishvatov.model.entity.buisness.CityMapEntity;
import org.springframework.stereotype.Component;

/**
 * {@link Mapper} interface implementation for city.
 *
 * @author Sergey Khvatov
 */
@Component
public class CityMapMapperImpl implements Mapper<CityMapEntity, CityMapDto> {

    /**
     * Maps existing entity object from DB to DTO.
     *
     * @param src entity object.
     * @return new DTO initialized with values from entity.
     */
    @Override
    public CityMapDto map(CityMapEntity src) {
        return new CityMapDto(src.getId(), src.getFrom(), src.getTo(), src.getDistance(), src.getAverageSpeed());
    }
}
