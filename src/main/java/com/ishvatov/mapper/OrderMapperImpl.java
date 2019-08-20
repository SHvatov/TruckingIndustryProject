package com.ishvatov.mapper;

import com.ishvatov.model.dto.OrderDto;
import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.buisness.OrderEntity;
import com.ishvatov.model.entity.buisness.WayPointEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        OrderDto orderDto = new OrderDto(src.getUniqueIdentificator(),
            src.getStatus(), null,
            src.getLastUpdated(),
            new HashSet<>(),
            new ArrayList<>());

        orderDto.setAssignedDrivers(src.getAssignedDrivers()
            .stream()
            .filter(Objects::nonNull)
            .map(AbstractEntity::getUniqueIdentificator)
            .collect(Collectors.toSet()));

        Optional.ofNullable(src.getAssignedTruck())
            .ifPresent(e -> orderDto.setTruckId(e.getUniqueIdentificator()));

        orderDto.setAssignedWaypoints(src.getAssignedWaypoints()
            .stream()
            .filter(Objects::nonNull)
            .map(WayPointEntity::getId)
            .collect(Collectors.toList()));

        return orderDto;
    }
}
