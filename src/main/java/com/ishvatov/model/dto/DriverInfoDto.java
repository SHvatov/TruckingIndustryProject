package com.ishvatov.model.dto;

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
    private DriverDto driverInformation;

    /**
     * UID of the second driver.
     */
    private String secondDriverUID;

    /**
     * Array of waypoint elements.
     */
    private List<WayPointDto> wayPointDtoArray;
}
