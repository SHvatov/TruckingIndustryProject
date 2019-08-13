package com.ishvatov.mapper;

import com.ishvatov.model.dto.DriverDto;
import com.ishvatov.model.entity.buisness.DriverEntity;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * {@link Mapper} interface implementation for driver.
 *
 * @author Sergey Khvatov
 */
@Component
public class DriverMapperImpl implements Mapper<DriverEntity, DriverDto> {

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
    public DriverDto map(DriverEntity src) {
        DriverDto driverDto = mapper.map(src, DriverDto.class);

        Optional.ofNullable(src.getDriverTruck())
            .ifPresent(entity -> driverDto.setCurrentCityUID(entity.getUniqueIdentificator()));

        Optional.ofNullable(src.getDriverOrder())
            .ifPresent(entity -> driverDto.setDriverOrderUID(entity.getUniqueIdentificator()));

        Optional.ofNullable(src.getDriverCity())
            .ifPresent(entity -> driverDto.setDriverTruckUID(entity.getUniqueIdentificator()));

        return driverDto;
    }
}
