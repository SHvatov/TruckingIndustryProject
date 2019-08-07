package com.ishvatov.mapper;

import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.buisness.TruckEntity;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@link Mapper} interface implementation for truck.
 *
 * @author Sergey Khvatov
 */
@Component
public class TruckMapperImpl implements Mapper<TruckEntity, TruckDto> {

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
    public TruckDto map(TruckEntity src) {
        TruckDto truckDto = mapper.map(src, TruckDto.class);

        if (src.getTruckCity() != null) {
            truckDto.setTruckCityUID(src.getTruckCity().getUniqueIdentificator());
        }

        if (src.getTruckOrder() != null) {
            truckDto.setTruckOrderUID(src.getTruckOrder().getUniqueIdentificator());
        }

        if (src.getTruckDriversSet() != null) {
            truckDto.setTruckDriverUIDSet(
                src.getTruckDriversSet()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(AbstractEntity::getUniqueIdentificator)
                    .collect(Collectors.toSet()));
        }

        return truckDto;
    }
}
