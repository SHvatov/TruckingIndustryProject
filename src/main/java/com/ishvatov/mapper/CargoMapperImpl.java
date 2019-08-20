package com.ishvatov.mapper;

import com.ishvatov.model.dto.CargoDto;
import com.ishvatov.model.entity.buisness.CargoEntity;
import org.springframework.stereotype.Component;

/**
 * {@link Mapper} interface implementation for city.
 *
 * @author Sergey Khvatov
 */
@Component
public class CargoMapperImpl implements Mapper<CargoEntity, CargoDto> {

    /**
     * Maps existing entity object from DB to DTO.
     *
     * @param src entity object.
     * @return new DTO initialized with values from entity.
     */
    @Override
    public CargoDto map(CargoEntity src) {
        return new CargoDto(src.getId(), src.getName(), src.getMass(), src.getStatus());
    }
}
