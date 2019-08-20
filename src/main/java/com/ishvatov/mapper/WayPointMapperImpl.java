package com.ishvatov.mapper;

import com.ishvatov.model.dto.WayPointDto;
import com.ishvatov.model.entity.buisness.WayPointEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * {@link Mapper} interface implementation for waypoint.
 *
 * @author Sergey Khvatov
 */
@Component
public class WayPointMapperImpl implements Mapper<WayPointEntity, WayPointDto> {

    /**
     * Maps existing entity object from DB to DTO.
     *
     * @param src entity object.
     * @return new DTO initialized with values from entity.
     */
    @Override
    public WayPointDto map(WayPointEntity src) {
        WayPointDto wayPointDto = new WayPointDto(src.getId(),
            src.getAction(), src.getStatus(), null, null, null);

        Optional.ofNullable(src.getCargo())
            .ifPresent(entity -> wayPointDto.setCargoId(entity.getId()));

        Optional.ofNullable(src.getOrder())
            .ifPresent(entity -> wayPointDto.setOrderId(entity.getUniqueIdentificator()));

        Optional.ofNullable(src.getCity())
            .ifPresent(entity -> wayPointDto.setCityId(entity.getUniqueIdentificator()));
        
        return wayPointDto;
    }
}
