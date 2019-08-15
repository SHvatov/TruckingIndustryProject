package com.ishvatov.service.inner.order;

import com.ishvatov.model.dto.DriverDto;
import com.ishvatov.model.dto.OrderDto;
import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.model.dto.WayPointDto;
import com.ishvatov.service.inner.BaseService;

import java.util.List;

/**
 * Defines a basic interface to interact with
 * order DAO layer.
 *
 * @author Sergey Khvatov
 */
public interface OrderService extends BaseService<String, OrderDto> {

    /**
     * Finds all suitable for this order trucks.
     *
     * @param wayPointDtoList list of the waypoints.
     * @return list of suitable for this order trucks.
     */
    List<TruckDto> findSuitableTrucks(List<WayPointDto> wayPointDtoList);


    /**
     * Finds all suitable for this order drivers.
     *
     * @param truckUID        UID of the truck.
     * @param wayPointDtoList list of the waypoints.
     * @return list of suitable for this order trucks.
     */
    List<DriverDto> findSuitableDrivers(String truckUID, List<WayPointDto> wayPointDtoList);
}
