package com.ishvatov.model.dto;

import com.ishvatov.model.entity.enum_types.CargoActionType;
import com.ishvatov.model.entity.enum_types.WayPointStatusType;
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
    private CargoActionType action;

    /**
     * Defines the status of the waypoint (completed or not).
     */
    private WayPointStatusType status;

    /**
     * Order, this waypoint is assigned to.
     */
    private String orderId;

    /**
     * Cargo, that is assigned to this waypoint.
     */
    private Integer cargoId;

    /**
     * City, that is assigned to this waypoint.
     */
    private String cityId;

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
