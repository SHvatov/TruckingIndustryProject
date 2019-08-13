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
    private Integer cityFromId;

    /**
     * Name of the B point on the update.
     */
    private Integer cityToId;

    /**
     * Distance between.
     */
    private Double distance;

    /**
     * Average speed.
     */
    private Double averageSpeed;

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
