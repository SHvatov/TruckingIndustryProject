package com.ishvatov.model.dao.waypoint;

import com.ishvatov.model.dao.AbstractDao;
import com.ishvatov.model.dao.truck.TruckDao;
import com.ishvatov.model.entity.buisness.WayPointEntity;
import com.ishvatov.model.entity.enum_types.CargoActionType;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
     * Checks if waypoint entity with such unique parameters already
     * exists in the database.
     *
     * @param cityID  ID of the city.
     * @param orderID ID of the order.
     * @param cargoID ID of the cargo.
     * @param action  assigned type of action.
     * @return true, if entity with such parameters already exists in the database.
     */
    @Override
    public boolean exists(int cityID, int orderID, int cargoID, CargoActionType action) {
        // generate criteria
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<WayPointEntity> criteriaQuery = criteriaBuilder.createQuery(WayPointEntity.class);
        Root<WayPointEntity> root = criteriaQuery.from(WayPointEntity.class);

        // form predicate and retrieve entities from data base
        Predicate predicate = criteriaBuilder.and(
            criteriaBuilder.equal(root.get(WayPointEntity.ORDER_ID), orderID),
            criteriaBuilder.equal(root.get(WayPointEntity.CITY_ID), cityID),
            criteriaBuilder.equal(root.get(WayPointEntity.CARGO_ID), cargoID),
            criteriaBuilder.equal(root.get(WayPointEntity.ACTION), action));

        // create request
        return !findEntities(predicate, criteriaQuery, root).isEmpty();
    }
}
