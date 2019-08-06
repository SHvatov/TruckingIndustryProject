package com.ishvatov.mapper;

/**
 * Defines a basic interface that is used to convert data
 * from Entity to DTO.
 *
 * @param <T1> entity type.
 * @param <T2> dto type.
 */
public interface Mapper<T1, T2> {

    /**
     * Maps existing entity object from DB to DTO.
     *
     * @param src entity object.
     * @return new DTO initialized with values from entity.
     */
    T2 map(T1 src);
}
