package com.ishvatov.model.dao.city_map;

import com.ishvatov.model.dao.BaseDaoInterface;
import com.ishvatov.model.entity.buisness.CityMapEntity;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

/**
 * Defines basic interface to work with city_map entities in the database.
 *
 * @author Sergey Khvatov.
 */
public interface CityMapDao extends BaseDaoInterface<Integer, CityMapEntity> {

    /**
     * Finds the distance between two cities.
     *
     * @param from UID of the source city.
     * @param to   UID of the destination city.
     * @return Distance between two cities, if
     * both of them exist in the table.
     */
    CityMapEntity findDistanceBetween(Integer from, Integer to);

    /**
     * Builds a graph which represents the map of the country.
     *
     * @return SimpleGraph object.
     */
    Graph<Integer, DefaultEdge> buildCityMap();
}
