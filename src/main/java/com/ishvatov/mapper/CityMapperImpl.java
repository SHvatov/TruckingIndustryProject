package com.ishvatov.mapper;

import com.ishvatov.model.dto.CityDto;
import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.buisness.CityEntity;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@link Mapper} interface implementation for city.
 *
 * @author Sergey Khvatov
 */
@Component
public class CityMapperImpl implements Mapper<CityEntity, CityDto> {

    /**
     * Autowired {@link DozerBeanMapper} instance.
     */
    @Autowired
    private DozerBeanMapper mapper;

    /**
     * Maps existing entity object from DB to DTO.
     *
     * @param src entity object.
     * @return new DTO initialized with values from entity.
     */
    @Override
    public CityDto map(CityEntity src) {
        CityDto cityDto = new CityDto(src.getUniqueIdentificator(), null, null);

        cityDto.setLocatedDrivers(src.getLocatedDrivers()
            .stream()
            .filter(Objects::nonNull)
            .map(AbstractEntity::getUniqueIdentificator)
            .collect(Collectors.toSet())
        );

        cityDto.setLocatedTrucks(src.getLocatedTrucks()
            .stream()
            .filter(Objects::nonNull)
            .map(AbstractEntity::getUniqueIdentificator)
            .collect(Collectors.toSet())
        );

        return cityDto;
    }
}
