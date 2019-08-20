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
        DriverDto driverDto = new DriverDto(src.getUniqueIdentificator(),
            null,
            src.getName(),
            src.getSurname(),
            src.getWorkedHours(),
            src.getLastUpdated(),
            src.getStatus(),
            null, null, null);

        Optional.ofNullable(src.getTruck())
            .ifPresent(entity -> driverDto.setTruckId(entity.getUniqueIdentificator()));

        Optional.ofNullable(src.getOrder())
            .ifPresent(entity -> driverDto.setOrderId(entity.getUniqueIdentificator()));

        Optional.ofNullable(src.getCity())
            .ifPresent(entity -> driverDto.setCityId(entity.getUniqueIdentificator()));

        return driverDto;
    }
}
