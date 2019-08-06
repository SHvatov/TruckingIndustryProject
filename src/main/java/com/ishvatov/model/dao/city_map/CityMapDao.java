package com.ishvatov.model.dao.city_map;

import com.ishvatov.model.dao.BaseDaoInterface;
import com.ishvatov.model.entity.buisness.CityMapEntity;

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
    Double findDistanceBetween(Integer from, Integer to);
}
