package com.ishvatov.service.buisness;

import com.ishvatov.service.buisness.response.ServerResponse;

/**
 * Defines basic interface of the validation classes.
 *
 * @author Sergey Khvatov
 */
public interface CustomValidator<T, U> {
    
    /**
     * Validates entity before loading it.
     *
     * @param entityUID UID of the entity.
     * @param response Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    boolean validateBeforeLoad(U entityUID, ServerResponse response);

    /**
     * Validates entity before updating its capacity.
     *
     * @param entityDto DTO object.
     * @param response Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    boolean validateBeforeUpdate(T entityDto, ServerResponse response);

    /**
     * Validates entity before deleting it.
     *
     * @param entityUID UID of the entity.
     * @param response Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    boolean validateBeforeDelete(U entityUID, ServerResponse response);

    /**
     * Validates user's input before saving new entity.
     *
     * @param entityDto DTO object.
     * @param response Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    boolean validateBeforeSave(T entityDto, ServerResponse response);
}
