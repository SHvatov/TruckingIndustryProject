package com.ishvatov.service.inner.truck;

import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.service.BaseService;

/**
 * Defines a basic interface to interact with
 * truck DAO layer.
 *
 * @author Sergey Khvatov
 */
public interface TruckService extends BaseService<String, TruckDto> {

    /**
     * Updates static field of the object.
     *
     * @param dtoObj   {@link TruckDto} instance.
     * @param truckUID UID of the modified truck.
     */
    void updateRegularFields(TruckDto dtoObj, String truckUID);

    /**
     * Updates city.
     *
     * @param cityUID  uid of the city.
     * @param truckUID UID of the modified truck.
     */
    void updateCity(String cityUID, String truckUID);

    /**
     * Updates order.
     *
     * @param orderUID uid of the order.
     * @param truckUID UID of the modified truck.
     */
    void updateOrder(String orderUID, String truckUID);

    /**
     * Removes order from the truck.
     *
     * @param truckUID uid of the truck.
     */
    void removeOrder(String truckUID);

    /**
     * Removes city from the truck and sets it to the default value.
     *
     * @param truckUID uid of the truck.
     */
    void removeCity(String truckUID);
}
