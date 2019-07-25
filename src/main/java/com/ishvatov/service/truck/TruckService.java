package com.ishvatov.service.truck;

import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.service.BaseService;

import java.util.List;

/**
 * Base truck service interface.
 *
 * @author Sergey Khvatov
 */
public interface TruckService extends BaseService<String, TruckDto> {

    /**
     * Finds the entities with this shift size.
     *
     * @param shiftSize driver's shift size.
     * @return empty collection, if no instances were found,
     * List of {@link TruckDto} objects otherwise.
     */
    List<TruckDto> findByDriverShiftSize(int shiftSize);

    /**
     * Finds the entities with capacity in range(min, max).
     *
     * @param min minimal capacity.
     * @param max maximal capacity.
     * @return empty collection, if no instances were found,
     * List of {@link TruckDto} objects otherwise.
     */
    List<TruckDto> findByCapacity(double min, double max);

    /**
     * Finds the entities with this condition.
     *
     * @param status condition of the truck
     * @return empty collection, if no instances were found,
     * List of {@link TruckDto} objects otherwise.
     */
    List<TruckDto> findByCondition(byte status);

    /**
     * Finds the entities with this city.
     *
     * @param city truck's location.
     * @return empty collection, if no instances were found,
     * List of {@link TruckDto} objects otherwise.
     */
    List<TruckDto> findByCity(String city);
}
