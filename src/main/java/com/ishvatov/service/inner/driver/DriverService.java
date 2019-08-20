package com.ishvatov.service.inner.driver;

import com.ishvatov.model.dto.DriverDto;
import com.ishvatov.service.inner.BaseService;

import java.util.List;

/**
 * Defines a basic interface to interact with
 * truck DAO layer.
 *
 * @author Sergey Khvatov
 */
public interface DriverService extends BaseService<String, DriverDto> {

    /**
     * Get all list of UID of all drivers in the DB.
     *
     * @return list with UID of all drivers in the DB.
     */
    List<String> getAllDrivers();
}
