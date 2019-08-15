package com.ishvatov.model.dao.waypoint;

import com.ishvatov.model.dao.AbstractDao;
import com.ishvatov.model.dao.truck.TruckDao;
import com.ishvatov.model.entity.buisness.CityEntity;
import com.ishvatov.model.entity.buisness.TruckEntity;
import com.ishvatov.model.entity.buisness.WayPointEntity;
import com.ishvatov.model.entity.enum_types.CargoActionType;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

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
    public boolean isAssigned(int cargoId) {
        // generate criteria
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<WayPointEntity> criteriaQuery = criteriaBuilder.createQuery(WayPointEntity.class);
        Root<WayPointEntity> root = criteriaQuery.from(WayPointEntity.class);

        // form predicate and retrieve entities from data base
        Predicate predicate = criteriaBuilder.equal(root.get(WayPointEntity.ORDER_ID), cargoId);

        // create request
        return !findEntities(predicate, criteriaQuery, root).isEmpty();
    }
}
