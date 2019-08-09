package com.ishvatov.model.dao.waypoint;

import com.ishvatov.model.dao.BaseDaoInterface;
import com.ishvatov.model.entity.buisness.WayPointEntity;
import com.ishvatov.model.entity.enum_types.CargoActionType;

/**
 * Defines basic interface to work with truck entities in the database.
 *
 * @author Sergey Khvatov.
 */
public interface WayPointDao extends BaseDaoInterface<Integer, WayPointEntity> {

    /**
     * Checks if waypoint entity with such unique parameters already
     * exists in the database.
     *
     * @param cityID  ID of the city.
     * @param orderID ID of the order.
     * @param cargoID ID of the cargo.
     * @param action  assigned type of action.
     * @return true, if entity with such parameters already exists in the database.
     */
    boolean exists(int cityID, int orderID, int cargoID, CargoActionType action);
}

