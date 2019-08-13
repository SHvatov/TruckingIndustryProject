package com.ishvatov.validator.truck;

import com.ishvatov.controller.response.ServerResponse;
import com.ishvatov.model.dto.TruckDto;

/**
 * Basic interface, which defines methods, that are
 * used to validate input data before saving, modifying
 * or deleting truck entities.
 *
 * @author Sergey Khvatov
 */
public interface TruckValidator {

    /**
     * Validates truck entity before loading it.
     *
     * @param truckUID UID of the truck entity.
     * @param response Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    boolean validateBeforeLoad(String truckUID, ServerResponse response);

    /**
     * Validates truck entity before updating its capacity.
     *
     * @param truckDto DTO object.
     * @param response Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    boolean validateBeforeUpdate(TruckDto truckDto, ServerResponse response);

    /**
     * Validates truck entity before deleting it.
     *
     * @param truckUID UID of the truck entity.
     * @param response Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    boolean validateBeforeDelete(String truckUID, ServerResponse response);

    /**
     * Validates user's input before saving new truck entity.
     *
     * @param truckDto DTO object.
     * @param response Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    boolean validateBeforeSave(TruckDto truckDto, ServerResponse response);
}
