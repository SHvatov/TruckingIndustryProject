package com.ishvatov.model.dao.city_map;

import com.ishvatov.model.dao.AbstractDao;
import com.ishvatov.model.entity.buisness.CityMapEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * {@link CityMapDao} implementation.
 *
 * @author Sergey Khvatov
 */
@Repository("cityMapDao")
public class CityMapDaoImpl extends AbstractDao<Integer, CityMapEntity> implements CityMapDao {

    /**
     * Finds entity by it's unique id.
     *
     * @param key unique key of the id.
     * @return Unique entity with this id.
     */
    @Override
    public CityMapEntity findByUniqueKey(Integer key) {
        return findById(key);
    }

    /**
     * Finds the distance between two cities.
     *
     * @param from UID of the source city.
     * @param to   UID of the destination city.
     * @return Distance between two cities, if
     * both of them exist in the table.
     */
    @Override
    public Double findDistanceBetween(Integer from, Integer to) {
        // generate criteria
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<CityMapEntity> criteriaQuery = criteriaBuilder.createQuery(CityMapEntity.class);
        Root<CityMapEntity> root = criteriaQuery.from(CityMapEntity.class);

        // form predicate and retrieve entities from data base
        Predicate predicate = criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CityMapEntity.FROM_CITY), from),
            criteriaBuilder.equal(root.get(CityMapEntity.TO_CITY), to));

        // create request
        criteriaQuery.select(root).where(predicate);

        List<CityMapEntity> resultList = getSession().createQuery(criteriaQuery).getResultList();
        // return the result if found
        if (!resultList.isEmpty()) {
            return resultList.get(0).getDistance();
        } else {
            // else try to find the way from TO to FROM
            Predicate reversedPredicate = criteriaBuilder.and(
                criteriaBuilder.equal(root.get(CityMapEntity.FROM_CITY), to),
                criteriaBuilder.equal(root.get(CityMapEntity.TO_CITY), from));

            // create request
            criteriaQuery.select(root).where(reversedPredicate);

            // return result
            resultList = getSession().createQuery(criteriaQuery).getResultList();
            return resultList.isEmpty() ? null : resultList.get(0).getDistance();
        }
    }
}
