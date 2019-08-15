package com.ishvatov.model.dao.waypoint;

import com.ishvatov.model.dao.BaseDaoInterface;
import com.ishvatov.model.entity.buisness.WayPointEntity;

/**
 * Defines basic interface to work with truck entities in the database.
 *
 * @author Sergey Khvatov.
 */
public interface WayPointDao extends BaseDaoInterface<Integer, WayPointEntity> {

    /**
     * Checks if cargo with such id has an assigned waypoint.
     *
     * @param cargoId id of the cargo.
     * @return true or false.
     */
    boolean isAssigned(int cargoId);
}

