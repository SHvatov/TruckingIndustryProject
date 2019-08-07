package com.ishvatov.mapper;

import com.ishvatov.model.dto.CargoDto;
import com.ishvatov.model.entity.buisness.CargoEntity;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@link Mapper} interface implementation for city.
 *
 * @author Sergey Khvatov
 */
@Component
public class CargoMapperImpl implements Mapper<CargoEntity, CargoDto> {

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
    public CargoDto map(CargoEntity src) {
        return mapper.map(src, CargoDto.class);
    }
}
