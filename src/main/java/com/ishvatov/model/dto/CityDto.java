package com.ishvatov.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Basic city DTO implementation.
 *
 * @author Sergey Khvatov
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDto implements BaseDtoInterface<String> {

    /**
     * Unique id of the city <=> name of the city.
     */
    private String uniqueIdentificator;
}
