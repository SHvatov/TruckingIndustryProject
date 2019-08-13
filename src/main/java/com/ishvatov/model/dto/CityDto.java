package com.ishvatov.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Basic city DTO implementation.
 *
 * @author Sergey Khvatov
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDto implements BaseDtoInterface<String> {

    /**
     * ID of the city in the database.
     */
    private Integer id;

    /**
     * Unique id of the city <=> name of the city.
     */
    private String uniqueIdentificator;

    /**
     * Set of UIDs of the drivers in the city.
     */
    private Set<String> locatedDrivers = new HashSet<>();

    /**
     * Set of UIDs of the trucks in the city.
     */
    private Set<String> locatedTrucks = new HashSet<>();
}
