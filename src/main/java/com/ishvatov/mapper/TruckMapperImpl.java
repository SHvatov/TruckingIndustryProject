package com.ishvatov.mapper;

import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.model.entity.buisness.TruckEntity;
import org.springframework.stereotype.Component;

/**
 * {@link Mapper} interface implementation for truck.
 *
 * @author Sergey Khvatov
 */
@Component
public class TruckMapperImpl implements Mapper<TruckEntity, TruckDto> {

    /**
     * Maps existing entity object from DB to DTO.
     *
     * @param src entity object.
     * @return new DTO initialized with values from entity.
     */
    @Override
    public TruckDto map(TruckEntity src) {
        return null;
    }
}
