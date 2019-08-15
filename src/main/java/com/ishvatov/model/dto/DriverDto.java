package com.ishvatov.model.dto;

import com.ishvatov.model.entity.enum_types.DriverStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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
     * Password of the driver. Used only
     * while adding new user.
     */
    private String driverPassword;

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
    private Integer driverWorkedHours;

    /**
     * Date - last time order was updated.
     */
    private Timestamp lastUpdated;

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
