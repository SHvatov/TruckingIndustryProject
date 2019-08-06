package com.ishvatov.model.dto;

import com.ishvatov.model.entity.enum_types.CargoActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Basic way point DTO implementation.
 *
 * @author Sergey Khvatov
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WayPointDto implements BaseDtoInterface<Integer> {

    /**
     * Unique id of the truck in the DB.
     */
    private Integer id;

    /**
     * Defines whether cargo is being loaded or
     * unloaded in this city.
     */
    private CargoActionType cargoAction;

    /**
     * Order, this waypoint is assigned to.
     */
    private String waypointOrderUID;

    /**
     * Cargo, that is assigned to this waypoint.
     */
    private String waypointCargoUID;

    /**
     * City, that is assigned to this waypoint.
     */
    private String waypointCityUID;

    /**
     * Get the unique identificator of the entity method.
     *
     * @return UID of the entity in the database.
     */
    @Override
    public Integer getUniqueIdentificator() {
        return id;
    }
}
