package com.ishvatov.service.inner.driver;

import com.ishvatov.model.dto.DriverDto;
import com.ishvatov.service.BaseService;

/**
 * Defines a basic interface to interact with
 * truck DAO layer.
 *
 * @author Sergey Khvatov
 */
public interface DriverService extends BaseService<String, DriverDto> {

    /**
     * Updates regular field of the object.
     *
     * @param dtoObj    {@link DriverDto} instance.
     * @param driverUID UID of the modified driver.
     */
    void updateRegularFields(DriverDto dtoObj, String driverUID);

    /**
     * Updates city.
     *
     * @param cityUID   uid of the city.
     * @param driverUID UID of the driver
     */
    void updateCity(String cityUID, String driverUID);

    /**
     * Updates order.
     *
     * @param orderUID  uid of the order.
     * @param driverUID UID of the driver
     */
    void updateOrder(String orderUID, String driverUID);

    /**
     * Updates truck.
     *
     * @param truckUID  UID of the truck.
     * @param driverUID UID of the driver.
     */
    void updateTruck(String truckUID, String driverUID);


    /**
     * Removes order from the truck.
     *
     * @param driverUID uid of the driver.
     */
    void removeOrder(String driverUID);

    /**
     * Removes city from the driver.
     *
     * @param driverUID uid of the driver.
     */
    void removeCity(String driverUID);

    /**
     * Removes driver from the truck.
     *
     * @param driverUID uid of the driver.
     */
    void removeTruck(String driverUID);
}
