package com.ishvatov.service.buisness.driver;

import com.ishvatov.service.buisness.response.ServerResponse;
import com.ishvatov.service.buisness.response.ServerResponseObject;
import com.ishvatov.model.dto.DriverDto;

import java.util.List;

/**
 * Defines basic methods, that are used in the business
 * logic.
 *
 * @author Sergey Khvatov
 */
public interface BusinessDriverService {

    /**
     * Gets all the drivers from the database.
     *
     * @return list of drivers from database.
     */
    List<DriverDto> loadAllDrivers();

    /**
     * Load driver from the database.
     *
     * @param driverId UID of the driver.
     * @return requested driver.
     */
    ServerResponseObject<DriverDto> loadDriver(String driverId);

    /**
     * Delete driver from the database.
     *
     * @param driverId UID of the driver.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    ServerResponse deleteDriver(String driverId);

    /**
     * Add driver to the database.
     *
     * @param driverDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    ServerResponse addDriver(DriverDto driverDto);

    /**
     * Updates driver's name.
     *
     * @param driverDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    ServerResponse updateDriverName(DriverDto driverDto);

    /**
     * Updates driver's surname.
     *
     * @param driverDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    ServerResponse updateDriverSurname(DriverDto driverDto);

    /**
     * Updates driver's city.
     *
     * @param driverDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    ServerResponse updateDriverCity(DriverDto driverDto);
}
