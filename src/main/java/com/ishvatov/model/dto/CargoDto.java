package com.ishvatov.model.dto;

import com.ishvatov.model.entity.enum_types.CargoStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Basic cargo DTO implementation.
 *
 * @author Sergey Khvatov
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CargoDto implements BaseDtoInterface<Integer> {

    /**
     * ID of the cargo in the database.
     */
    private Integer id;

    /**
     * Name of the cargo.
     */
    private String name;

    /**
     * Mass of the cargo.
     */
    private Double mass;

    /**
     * Status of the cargo.
     */
    private CargoStatusType status;

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
