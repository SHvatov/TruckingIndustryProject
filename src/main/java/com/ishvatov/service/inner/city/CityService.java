package com.ishvatov.service.inner.city;

import com.ishvatov.model.dto.CityDto;
import com.ishvatov.service.inner.BaseService;

import java.util.List;

/**
 * Defines a basic interface to interact with
 * truck DAO layer.
 *
 * @author Sergey Khvatov
 */
public interface CityService extends BaseService<String, CityDto> {

    /**
     * Get the names of all cities in the DB.
     *
     * @return list with names of all cities in the DB.
     */
    List<String> getAllCities();
}
