package com.ishvatov.model.dto;

import com.ishvatov.model.entity.enum_types.OrderStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.HashSet;
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
    private String truckUID;

    /**
     * Date - start of the order.
     */
    private Timestamp orderStart;

    /**
     * Date - end of the order.
     */
    private Timestamp orderEnd;

    /**
     * Set of drivers, who are assigned to this order.
     */
    private Set<String> driverUIDSet = new HashSet<>();

    /**
     * Set of waypoints, that are located in the city.
     */
    private Set<Integer> waypointsIDSet = new HashSet<>();
}
