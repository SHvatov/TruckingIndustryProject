package com.ishvatov.model.dao.truck;

import com.ishvatov.model.dao.BaseDaoInterface;
import com.ishvatov.model.entity.TruckEntity;

import java.util.List;

/**
 * Truck DAO interface.
 *
 * @author Sergey Khvatov
 */
public interface TruckDao extends BaseDaoInterface<String, TruckEntity> {

    /**
     * Finds the entities with this shift size.
     *
     * @param shiftSize driver's shift size.
     * @return empty collection, if no instances were found,
     * List of {@link TruckEntity} objects otherwise.
     */
    List<TruckEntity> findByDriverShiftSize(int shiftSize);

    /**
     * Finds the entities with capacity in range(min, max).
     *
     * @param min minimal capacity.
     * @param max maximal capacity.
     * @return empty collection, if no instances were found,
     * List of {@link TruckEntity} objects otherwise.
     */
    List<TruckEntity> findByCapacity(double min, double max);

    /**
     * Finds the entities with this condition.
     *
     * @param status condition of the truck
     * @return empty collection, if no instances were found,
     * List of {@link TruckEntity} objects otherwise.
     */
    List<TruckEntity> findByCondition(byte status);

    /**
     * Finds the entities with this city.
     *
     * @param city truck's location.
     * @return empty collection, if no instances were found,
     * List of {@link TruckEntity} objects otherwise.
     */
    List<TruckEntity> findByCity(String city);
}
