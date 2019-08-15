package com.ishvatov.service.inner.cargo;

import com.ishvatov.model.dto.CargoDto;
import com.ishvatov.service.inner.BaseService;

/**
 * Defines a basic interface to interact with
 * cargo DAO layer.
 *
 * @author Sergey Khvatov
 */
public interface CargoService extends BaseService<Integer, CargoDto> {

    /**
     * Checks if cargo with this id has been assigned.
     *
     * @param id id of the cargo.
     * @return true, it it was, false otherwise.
     */
    boolean hasOrder(Integer id);
}
