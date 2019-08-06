package com.ishvatov.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Basic country update DTO implementation.
 *
 * @author Sergey Khvatov
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityMapDto implements BaseDtoInterface<Integer> {

    /**
     * Id of the entity in the database.
     */
    private Integer id;

    /**
     * Name of the A point on the update.
     */
    private String sourceCityName;

    /**
     * Name of the B point on the update.
     */
    private String destinationCityName;

    /**
     * Distance between.
     */
    private Double distance;

    /**
     * Get the unique identificator of the entity method.
     *
     * @return UID of the entity in the database.
     */
    @Override
    public Integer getUniqueIdentificator() {
        return id;
    }
}
