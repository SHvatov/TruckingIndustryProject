package com.ishvatov.model.dto;

/**
 * Basic DTO interface.
 *
 * @param <U> Type of the key.
 */
public interface BaseDtoInterface<U> {

    /**
     * Get the unique identificator of the entity method.
     *
     * @return UID of the entity in the database.
     */
    U getUniqueIdentificator();
}
