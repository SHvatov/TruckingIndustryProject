package com.ishvatov.mapper;

import com.ishvatov.model.dto.WayPointDto;
import com.ishvatov.model.entity.buisness.WayPointEntity;
import org.springframework.stereotype.Component;

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
        WayPointDto wayPointDto = new WayPointDto();

        wayPointDto.setId(src.getId());
        wayPointDto.setCargoAction(src.getCargoAction());
        wayPointDto.setWaypointCargoUID(src.getWaypointCargoEntity().getId());
        wayPointDto.setWaypointCityUID(src.getWaypointCityEntity().getUniqueIdentificator());
        wayPointDto.setWaypointOrderUID(src.getWaypointOrderEntity().getUniqueIdentificator());

        return wayPointDto;
    }
}
