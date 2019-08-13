package com.ishvatov.mapper;

import com.ishvatov.model.dto.WayPointDto;
import com.ishvatov.model.entity.buisness.WayPointEntity;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    public WayPointDto map(WayPointEntity src) {
        WayPointDto wayPointDto = mapper.map(src, WayPointDto.class);

        Optional.ofNullable(src.getWaypointCargo())
            .ifPresent(entity -> wayPointDto.setWaypointCargoUID(entity.getId()));

        Optional.ofNullable(src.getWaypointOrder())
            .ifPresent(entity -> wayPointDto.setWaypointOrderUID(entity.getUniqueIdentificator()));

        Optional.ofNullable(src.getWaypointCity())
            .ifPresent(entity -> wayPointDto.setWaypointCityUID(entity.getUniqueIdentificator()));
        
        return wayPointDto;
    }
}
