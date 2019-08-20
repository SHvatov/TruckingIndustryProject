package com.ishvatov.model.dto;

import com.ishvatov.model.entity.enum_types.OrderStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Basic DTO class, which is used to retrieve information
 * about driver and its attributes.
 *
 * @author Sergey Khvatov
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverInfoDto {

    /**
     * Info about this  driver.
     */
    private DriverDto driver;

    /**
     * Status of the order.
     */
    private OrderStatusType orderStatus;

    /**
     * UID of the second driver.
     */
    private String secondDriverId;

    /**
     * Array of waypoint elements.
     */
    private List<WayPointDto> waypoints;
}
