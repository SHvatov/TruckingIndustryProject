package com.ishvatov.model.dao.city_map;

import com.ishvatov.model.dao.AbstractDao;
import com.ishvatov.model.entity.buisness.CityMapEntity;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
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
    public CityMapEntity findBetween(Integer from, Integer to) {
        // generate criteria
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<CityMapEntity> criteriaQuery = criteriaBuilder.createQuery(CityMapEntity.class);
        Root<CityMapEntity> root = criteriaQuery.from(CityMapEntity.class);

        // form predicate and retrieve entities from data base
        Predicate predicate = criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CityMapEntity.FROM_FIELD), from),
            criteriaBuilder.equal(root.get(CityMapEntity.TO_FIELD), to));

        // create request
        criteriaQuery.select(root).where(predicate);

        // return the result
        List<CityMapEntity> resultList = getSession().createQuery(criteriaQuery).getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    /**
     * Builds a graph which represents the map of the country.
     *
     * @return SimpleGraph object.
     */
    @Override
    public Graph<Integer, DefaultEdge> buildMap() {
        List<CityMapEntity> cityMapEntityList = findAll();
        Graph<Integer, DefaultEdge> mapGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
        for (CityMapEntity cityMap : cityMapEntityList) {
            if (!mapGraph.containsVertex(cityMap.getFrom())) {
                mapGraph.addVertex(cityMap.getFrom());
            }

            if (!mapGraph.containsVertex(cityMap.getTo())) {
                mapGraph.addVertex(cityMap.getTo());
            }

            if (!mapGraph.containsEdge(cityMap.getFrom(), cityMap.getTo())) {
                mapGraph.addEdge(cityMap.getFrom(), cityMap.getTo());
            }
        }

        return mapGraph;
    }
}
