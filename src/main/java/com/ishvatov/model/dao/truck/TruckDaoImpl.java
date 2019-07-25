package com.ishvatov.model.dao.truck;

import com.ishvatov.model.dao.AbstractDao;
import com.ishvatov.model.entity.TruckEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.List;

@Repository("truckDao")
public class TruckDaoImpl extends AbstractDao<String, TruckEntity> implements TruckDao {

    /**
     * Finds the entity with this unique registration number.
     *
     * @param registrationNumber registration number.
     * @return null, if no instances were found, {@link TruckEntity} object otherwise.
     */
    public TruckEntity findByUniqueKey(String registrationNumber) {
        // generate criteria
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<TruckEntity> criteriaQuery = criteriaBuilder.createQuery(TruckEntity.class);
        Root<TruckEntity> root = criteriaQuery.from(TruckEntity.class);

        // form predicate and retrieve entities from data base
        Predicate predicate = criteriaBuilder.equal(root.get(TruckEntity.REGISTRATION_NUMBER), registrationNumber);
        List<TruckEntity> entities = super.findEntities(predicate, criteriaQuery, root);

        // return result
        return entities.isEmpty() ? null : entities.get(0);
    }

    /**
     * Finds the entities with this shift size.
     *
     * @param shiftSize driver's shift size.
     * @return empty collection, if no instances were found,
     * List of {@link TruckEntity} objects otherwise.
     */
    public List<TruckEntity> findByDriverShiftSize(int shiftSize) {
        // generate criteria
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<TruckEntity> criteriaQuery = criteriaBuilder.createQuery(TruckEntity.class);
        Root<TruckEntity> root = criteriaQuery.from(TruckEntity.class);

        // form predicate and retrieve entities from data base
        Predicate predicate = criteriaBuilder.equal(root.get(TruckEntity.SHIFT), shiftSize);
        return super.findEntities(predicate, criteriaQuery, root);
    }

    /**
     * Finds the entities with capacity in range(min, max).
     *
     * @param min minimal capacity.
     * @param max maximal capacity.
     * @return empty collection, if no instances were found,
     * List of {@link TruckEntity} objects otherwise.
     */
    public List<TruckEntity> findByCapacity(double min, double max) {
        // generate criteria
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<TruckEntity> criteriaQuery = criteriaBuilder.createQuery(TruckEntity.class);
        Root<TruckEntity> root = criteriaQuery.from(TruckEntity.class);

        // form predicate and retrieve entities from data base
        Predicate predicate = criteriaBuilder.and(criteriaBuilder.ge(root.<Double>get(TruckEntity.CAPACITY), min), criteriaBuilder.le(root.<Double>get(TruckEntity.CAPACITY), max));

        // get the result
        return super.findEntities(predicate, criteriaQuery, root);
    }

    /**
     * Finds the entities with this condition.
     *
     * @param status condition of the truck
     * @return empty collection, if no instances were found,
     * List of {@link TruckEntity} objects otherwise.
     */
    public List<TruckEntity> findByCondition(byte status) {
        // generate criteria
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<TruckEntity> criteriaQuery = criteriaBuilder.createQuery(TruckEntity.class);
        Root<TruckEntity> root = criteriaQuery.from(TruckEntity.class);

        // form predicate and retrieve entities from data base
        Predicate predicate = criteriaBuilder.equal(root.get(TruckEntity.CONDITION), status);

        // get the result
        return super.findEntities(predicate, criteriaQuery, root);
    }

    /**
     * Finds the entities with this city.
     *
     * @param city truck's location.
     * @return empty collection, if no instances were found,
     * List of {@link TruckEntity} objects otherwise.
     */
    public List<TruckEntity> findByCity(String city) {
        // generate criteria
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<TruckEntity> criteriaQuery = criteriaBuilder.createQuery(TruckEntity.class);
        Root<TruckEntity> root = criteriaQuery.from(TruckEntity.class);

        // form predicate and retrieve entities from data base
        Predicate predicate = criteriaBuilder.equal(root.get(TruckEntity.CITY), city);

        // get the result
        return super.findEntities(predicate, criteriaQuery, root);
    }
}
