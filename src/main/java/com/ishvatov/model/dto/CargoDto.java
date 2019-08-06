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
public class CargoDto implements BaseDtoInterface<String> {

    /**
     * Unique identificator of the object.
     */
    private String uniqueIdentificator;

    /**
     * Mass of the cargo.
     */
    private Double cargoMass;

    /**
     * Status of the cargo.
     */
    private CargoStatusType cargoStatus;
}
