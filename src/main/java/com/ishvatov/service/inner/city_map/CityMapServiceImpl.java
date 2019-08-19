package com.ishvatov.service.inner.city_map;

import com.ishvatov.exception.DAOException;
import com.ishvatov.model.dao.city.CityDao;
import com.ishvatov.model.dao.city_map.CityMapDao;
import com.ishvatov.model.dto.WayPointDto;
import com.ishvatov.model.entity.buisness.CityEntity;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Basic {@link CityMapService} interface implementation.
 *
 * @author Sergey Khvatov
 */
@Service("cityMapService")
@Transactional
public class CityMapServiceImpl implements CityMapService {

    /**
     * Autowired DAO field.
     */
    @Autowired
    private CityMapDao cityMapDao;

    /**
     * Autowired DAO field.
     */
    @Autowired
    private CityDao cityDao;

    /**
     * Builds a graph which represents the map of the country.
     *
     * @return SimpleGraph object.
     */
    @Override
    public Graph<Integer, DefaultEdge> buildMap() {
        return cityMapDao.buildMap();
    }

    /**
     * Builds a graph which represents the map of the country.
     *
     * @param startCityId UID of the city, where truck is located.
     * @param path         path to be checked.
     * @return SimpleGraph object.
     */
    @Override
    public boolean checkIfPathExists(String startCityId, List<WayPointDto> path) {
        // build graph
        Graph<Integer, DefaultEdge> map = cityMapDao.buildMap();
        DijkstraShortestPath<Integer, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<>(map);

        CityEntity currentCity = Optional.ofNullable(cityDao.findByUniqueKey(startCityId)).
            orElseThrow(() -> new DAOException(getClass(), "checkIfPathExists", "Entity with such UID does not exist"));

        for (WayPointDto wayPointDto : path) {
            CityEntity nextCity = Optional.ofNullable(cityDao.findByUniqueKey(wayPointDto.getCityId()))
                .orElseThrow(() -> new DAOException(getClass(), "checkIfPathExists", "Entity with such UID does not exist"));

            // find shortest path
            GraphPath<Integer, DefaultEdge> shortestPath =
                dijkstraShortestPath.getPath(currentCity.getId(), nextCity.getId());

            // check if it is null
            if (shortestPath == null) {
                return false;
            }

            // move to the next city
            currentCity = nextCity;
        }
        return true;
    }

    /**
     * Builds a graph which represents the map of the country.
     *
     * @param path to be checked.
     * @return SimpleGraph object.
     */
    @Override
    public boolean checkIfPathExists(List<WayPointDto> path) {
        // no path from itself
        if (path.size() <= 1) {
            return false;
        }

        Graph<Integer, DefaultEdge> map = cityMapDao.buildMap();
        DijkstraShortestPath<Integer, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<>(map);

        CityEntity currentCity = Optional.ofNullable(cityDao.findByUniqueKey(path.get(0).getCityId()))
            .orElseThrow(() -> new DAOException(getClass(), "checkIfPathExists", "Entity with such UID does not exist"));

        for (int i = 1; i < path.size(); i++) {
            CityEntity nextCity = Optional.ofNullable(cityDao.findByUniqueKey(path.get(i).getCityId()))
                .orElseThrow(() -> new DAOException(getClass(), "checkIfPathExists", "Entity with such UID does not exist"));

            // find shortest path
            GraphPath<Integer, DefaultEdge> shortestPath =
                dijkstraShortestPath.getPath(currentCity.getId(), nextCity.getId());

            // check if it is null
            if (shortestPath == null) {
                return false;
            }

            // move to the next city
            currentCity = nextCity;
        }
        return true;
    }
}
