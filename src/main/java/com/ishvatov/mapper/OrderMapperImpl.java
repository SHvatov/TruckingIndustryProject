package com.ishvatov.mapper;

import com.ishvatov.model.dto.OrderDto;
import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.buisness.OrderEntity;
import com.ishvatov.model.entity.buisness.WayPointEntity;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
     * Autowired {@link DozerBeanMapper} instance.
     */
    @Autowired
    private DozerBeanMapper mapper;

    /**
     * Maps existing entity object from DB to DTO.
     *
     * @param src entity object.
     * @return new DTO initialized with values from entity.
     */
    @Override
    public OrderDto map(OrderEntity src) {
        OrderDto orderDto = mapper.map(src, OrderDto.class);

        orderDto.setDriverUIDSet(src.getAssignedDrivers()
            .stream()
            .filter(Objects::nonNull)
            .map(AbstractEntity::getUniqueIdentificator)
            .collect(Collectors.toSet()));

        Optional.ofNullable(src.getAssignedTruck())
            .ifPresent(e -> orderDto.setTruckUID(e.getUniqueIdentificator()));

        orderDto.setWaypointsIDList(src.getAssignedWaypoints()
            .stream()
            .filter(Objects::nonNull)
            .map(WayPointEntity::getId)
            .collect(Collectors.toList()));

        return null;
    }
}
