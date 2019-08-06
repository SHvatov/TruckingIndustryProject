package com.ishvatov.model.dto;

import com.ishvatov.model.entity.enum_types.DriverStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Basic country update DTO implementation.
 *
 * @author Sergey Khvatov
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDto implements BaseDtoInterface<String> {

    /**
     * Unique id of the driver.
     */
    private String uniqueIdentificator;

    /**
     * Name of the driver.
     */
    private String driverName;

    /**
     * Surname of the driver.
     */
    private String driverSurname;

    /**
     * Number of hours driver has worked
     * in this month.
     */
    private String driverWorkedHours;

    /**
     * Status of the driver.
     */
    private DriverStatusType driverStatus;

    /**
     * UID of the truck this driver is assigned to.
     */
    private String driverTruckUID;

    /**
     * UID of the current city.
     */
    private String currentCityUID;

    /**
     * UID of the order this driver is assigned to.
     */
    private String driverOrderUID;
}