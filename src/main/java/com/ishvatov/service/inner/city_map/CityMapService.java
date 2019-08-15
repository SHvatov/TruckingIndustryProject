package com.ishvatov.service.inner.city_map;

import com.ishvatov.model.dto.WayPointDto;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.List;

/**
 * Defines a basic interface to interact with
 * truck DAO layer.
 *
 * @author Sergey Khvatov
 */
public interface CityMapService {

    /**
     * Builds a graph which represents the map of the country.
     *
     * @return SimpleGraph object.
     */
    Graph<Integer, DefaultEdge> buildCityMap();

    /**
     * Checks if path exists.
     *
     * @param startCityUID UID of the city, where truck is located.
     * @param path         path to check.
     * @return true or false.
     */
    boolean checkIfPathExists(String startCityUID, List<WayPointDto> path);

    /**
     * Builds a graph which represents the map of the country.
     *
     * @param path
     * @return SimpleGraph object.
     */
    boolean checkIfPathExists(List<WayPointDto> path);
}
