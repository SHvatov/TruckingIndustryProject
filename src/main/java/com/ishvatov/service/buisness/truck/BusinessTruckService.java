package com.ishvatov.service.buisness.truck;

import com.ishvatov.service.buisness.response.ServerResponse;
import com.ishvatov.service.buisness.response.ServerResponseObject;
import com.ishvatov.model.dto.TruckDto;

import java.util.List;

/**
 * Defines basic methods, that are used in the business
 * logic.
 *
 * @author Sergey Khvatov
 */
public interface BusinessTruckService {

    /**
     * Gets all the trucks from the database.
     *
     * @return list of trucks from database.
     */
    List<TruckDto> loadAllTrucks();

    /**
     * Fetches data about truck from the database.
     *
     * @param truckId UID of the truck.
     * @return requested truck object or error.
     */
    ServerResponseObject<TruckDto> loadTruck(String truckId);

    /**
     * Deletes truck from database.
     *
     * @param truckId UID of the truck.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    ServerResponse deleteTruck(String truckId);

    /**
     * Adds truck to the database.
     *
     * @param truckDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    ServerResponse addTruck(TruckDto truckDto);

    /**
     * Updates the capacity of the truck.
     *
     * @param truckDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    ServerResponse updateTruckCapacity(TruckDto truckDto);

    /**
     * Updates the shift size of the truck.
     *
     * @param truckDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    ServerResponse updateTruckShiftSize(TruckDto truckDto);

    /**
     * Updates the status of the truck.
     *
     * @param truckDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    ServerResponse updateTruckStatus(TruckDto truckDto);
}
