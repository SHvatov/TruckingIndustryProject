package com.ishvatov.model.dto;

import com.ishvatov.model.entity.enum_types.TruckConditionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Basic truck DTO implementation.
 *
 * @author Sergey Khvatov
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TruckDto implements BaseDtoInterface<String> {

    /**
     * Unique id of the truck.
     */
    private String uniqueIdentificator;

    /**
     * Truck's capacity in tons.
     */
    private Double truckCapacity;

    /**
     * DriverEntity shift size in hours.
     */
    private Integer truckDriverShiftSize;

    /**
     * Truck status.
     */
    private TruckConditionType truckCondition;

    /**
     * City, where truck is located.
     */
    private String truckCityUID;

    /**
     * Drivers, who are using this truck.
     */
    private Set<String> truckDriversUIDSet = new HashSet<>();

    /**
     * Orders, that is assigned to this truck.
     */
    private String truckOrderUID;
}
