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
    private String password;

    /**
     * Name of the driver.
     */
    private String name;

    /**
     * Surname of the driver.
     */
    private String surname;

    /**
     * Number of hours driver has worked
     * in this month.
     */
    private Integer workedHours;

    /**
     * Date - last time order was updated.
     */
    private Timestamp lastUpdated;

    /**
     * Status of the driver.
     */
    private DriverStatusType status;

    /**
     * UID of the truck this driver is assigned to.
     */
    private String truckId;

    /**
     * UID of the current city.
     */
    private String cityId;

    /**
     * UID of the order this driver is assigned to.
     */
    private String orderId;
}
