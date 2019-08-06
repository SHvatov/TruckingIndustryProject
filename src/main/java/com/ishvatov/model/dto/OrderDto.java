package com.ishvatov.model.dto;

import com.ishvatov.model.entity.enum_types.OrderStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Basic way point DTO implementation.
 *
 * @author Sergey Khvatov
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto implements BaseDtoInterface<String> {

    /**
     * Unique id of the order.
     */
    private String uniqueIdentificator;

    /**
     * Status of the order.
     */
    private OrderStatusType orderStatus;

    /**
     * Truck, that is assigned to this order.
     */
    private Set<String> truckUIDSet;

    /**
     * Set of drivers, who are assigned to this order.
     */
    private Set<String> driverUIDSet;

    /**
     * Set of waypoints, that are located in the city.
     */
    private Set<Integer> assignedWaypointsIdSet;
}
