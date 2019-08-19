package com.ishvatov.mapper;

import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.buisness.TruckEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        TruckDto truckDto = new TruckDto(src.getUniqueIdentificator(),
            src.getCapacity(),
            src.getShiftSize(),
            src.getStatus(),
            null,
            new HashSet<>(),
            null);

        Optional.ofNullable(src.getCity())
            .ifPresent(entity -> truckDto.setCityId(entity.getUniqueIdentificator()));

        Optional.ofNullable(src.getOrder())
            .ifPresent(entity -> truckDto.setOrderId(entity.getUniqueIdentificator()));

        truckDto.setAssignedDrivers(src.getAssignedDrivers()
            .stream()
            .filter(Objects::nonNull)
            .map(AbstractEntity::getUniqueIdentificator)
            .collect(Collectors.toSet())
        );

        return truckDto;
    }
}
