package com.ishvatov.mapper;

import com.ishvatov.model.dto.DriverDto;
import com.ishvatov.model.entity.buisness.DriverEntity;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

        if (src.getDriverTruckEntity() != null) {
            driverDto.setCurrentCityUID(src.getDriverTruckEntity().getUniqueIdentificator());
        }

        if (src.getDriverOrder() != null) {
            driverDto.setDriverOrderUID(src.getDriverOrder().getUniqueIdentificator());
        }

        if (src.getDriverCurrentCity() != null) {
            driverDto.setDriverTruckUID(src.getDriverOrder().getUniqueIdentificator());
        }

        return driverDto;
    }
}
