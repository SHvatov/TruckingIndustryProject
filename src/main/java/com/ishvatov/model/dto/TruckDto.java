package com.ishvatov.model.dto;

import lombok.Data;

/**
 * Defines a basic DTO, which represents a truck.
 *
 * @author Sergey Khvatov
 */
@Data
public class TruckDto {

    /**
     * Unique registration number of the truck.
     */
    private String truckRegistrationNumber;

    /**
     * Truck's capacity in tons.
     */
    private double truckCapacity;

    /**
     * Driver shift size in hours.
     */
    private int driverShiftSize;

    /**
     * Truck status.
     * Bit value: 0 - OK, 1 - NOT OK.
     */
    private byte truckCondition;

    /**
     * Current location of the truck.
     */
    private String truckCurrentCity;
}
