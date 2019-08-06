package com.ishvatov.mapper;

import com.ishvatov.model.dto.DriverDto;
import com.ishvatov.model.entity.buisness.DriverEntity;
import org.springframework.stereotype.Component;

/**
 * {@link Mapper} interface implementation for driver.
 *
 * @author Sergey Khvatov
 */
@Component
public class DriverMapperImpl implements Mapper<DriverEntity, DriverDto> {

    /**
     * Maps existing entity object from DB to DTO.
     *
     * @param src entity object.
     * @return new DTO initialized with values from entity.
     */
    @Override
    public DriverDto map(DriverEntity src) {
        return null;
    }
}
