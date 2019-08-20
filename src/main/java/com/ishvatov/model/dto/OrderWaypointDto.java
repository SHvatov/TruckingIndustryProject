package com.ishvatov.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * Basic DTO class, which is used to retrieve information
 * about order and its waypoints.
 *
 * @author Sergey Khvatov
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderWaypointDto {

    /**
     * Unique id of the order.
     */
    private String uniqueIdentificator;

    /**
     * Unique id of the truck.
     */
    private String truckId;

    /**
     * Unique id of the drivers.
     */
    private Set<String> assignedDrivers;

    /**
     * Array of waypoint elements.
     */
    private List<WayPointDto> waypoints;
}
