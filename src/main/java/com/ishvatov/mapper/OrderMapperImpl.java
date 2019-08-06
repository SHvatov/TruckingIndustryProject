package com.ishvatov.mapper;

import com.ishvatov.model.dto.OrderDto;
import com.ishvatov.model.entity.buisness.OrderEntity;
import org.springframework.stereotype.Component;

/**
 * {@link Mapper} interface implementation for driver.
 *
 * @author Sergey Khvatov
 */
@Component
public class OrderMapperImpl implements Mapper<OrderEntity, OrderDto> {

    /**
     * Maps existing entity object from DB to DTO.
     *
     * @param src entity object.
     * @return new DTO initialized with values from entity.
     */
    @Override
    public OrderDto map(OrderEntity src) {
        return null;
    }
}
