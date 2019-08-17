package com.ishvatov.model.dao.waypoint;

import com.ishvatov.model.dao.AbstractDao;
import com.ishvatov.model.dao.truck.TruckDao;
import com.ishvatov.model.entity.buisness.WayPointEntity;
import org.springframework.stereotype.Repository;

/**
 * {@link TruckDao} implementation.
 *
 * @author Sergey Khvatov
 */
@Repository("wayPointDao")
public class WayPointDaoImpl extends AbstractDao<Integer, WayPointEntity> implements WayPointDao {

    /**
     * Finds entity by it's unique id.
     *
     * @param key unique key of the id.
     * @return Unique entity with this id.
     */
    @Override
    public WayPointEntity findByUniqueKey(Integer key) {
        return findById(key);
    }

    /**
     * Checks if cargo with such id has an assigned waypoint.
     *
     * @param cargoId id of the cargo.
     * @return true or false.
     */
    @Override
    public boolean isAssigned(Integer cargoId) {
        return findAll()
            .stream()
            .anyMatch(wayPointEntity -> cargoId.equals(wayPointEntity.getWaypointCargo().getId()));
    }
}
