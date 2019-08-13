package com.ishvatov.service.inner.order;

import com.ishvatov.exception.DAOException;
import com.ishvatov.exception.ValidationException;
import com.ishvatov.mapper.Mapper;
import com.ishvatov.model.dao.driver.DriverDao;
import com.ishvatov.model.dao.order.OrderDao;
import com.ishvatov.model.dao.truck.TruckDao;
import com.ishvatov.model.dao.waypoint.WayPointDao;
import com.ishvatov.model.dto.OrderDto;
import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.buisness.DriverEntity;
import com.ishvatov.model.entity.buisness.OrderEntity;
import com.ishvatov.model.entity.buisness.TruckEntity;
import com.ishvatov.model.entity.buisness.WayPointEntity;
import com.ishvatov.service.inner.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Basic {@link OrderService} interface implementation.
 *
 * @author Sergey Khvatov
 */
@Service("orderService")
@Transactional
public class OrderServiceImpl extends AbstractService<String, OrderEntity, OrderDto> implements OrderService {

    /**
     * Autowired DAO field.
     */
    private TruckDao truckDao;

    /**
     * Autowired DAO field.
     */
    private WayPointDao wayPointDao;

    /**
     * Autowired DAO field.
     */
    private OrderDao orderDao;

    /**
     * Autowired DAO field.
     */
    private DriverDao driverDao;

    /**
     * Default class constructor, that is used
     * to inject DAO interface implementation and
     * initialize the super class.
     *
     * @param mapper   {@link Mapper} implementation.
     * @param truckDao autowired {@link TruckDao} impl.
     * @param wayPointDao  autowired {@link WayPointDao} impl.
     * @param orderDao autowired {@link OrderDao} impl.
     */
    @Autowired
    public OrderServiceImpl(TruckDao truckDao, WayPointDao wayPointDao,
                            DriverDao driverDao, OrderDao orderDao,
                            Mapper<OrderEntity, OrderDto> mapper) {
        super(orderDao, mapper);
        this.wayPointDao = wayPointDao;
        this.orderDao = orderDao;
        this.truckDao = truckDao;
        this.driverDao = driverDao;
    }

    /**
     * Adds entity to the DB. Check if entity already exists.
     *
     * @param dtoObj new entity to add.
     * @throws DAOException        if entity with this UID already exists
     * @throws ValidationException if DTO field, which is corresponding to
     *                             the not nullable field in the Entity object is null.
     */
    @Override
    public void save(OrderDto dtoObj) {
        validateRequiredFields(dtoObj);
        if (exists(dtoObj.getUniqueIdentificator())) {
            throw new DAOException(getClass(), "save", "Entity with such UID already exists");
        } else {
            OrderEntity entity = new OrderEntity();
            updateImpl(dtoObj, entity);
            orderDao.save(entity);
        }
    }

    /**
     * Updates data in the database. If fields in teh DTO
     * are not null, then update them. If are null, then
     * if corresponding filed in the Entity is nullable,
     * then set it to null and remove all connections,
     * otherwise throw NPE.
     *
     * @param dtoObj values to update in the entity.
     * @throws DAOException        if entity with this UID already exists
     * @throws ValidationException if DTO field, which is corresponding to
     *                             the not nullable field in the Entity object is null.
     */
    @Override
    public void update(OrderDto dtoObj) {
        validateRequiredFields(dtoObj);
        OrderEntity entity = Optional.ofNullable(
            orderDao.findByUniqueKey(dtoObj.getUniqueIdentificator()))
            .orElseThrow(() -> new DAOException(getClass(), "update", "Entity with such UID does not exist"));
        updateImpl(dtoObj, entity);
    }

    /**
     * Deletes entity from the DB if it exists.
     *
     * @param key UID of the entity.
     */
    @Override
    public void delete(String key) {
        Optional<OrderEntity> orderEntity = Optional.ofNullable(
            orderDao.findByUniqueKey(
                Optional.ofNullable(key).orElseThrow(() -> new ValidationException(getClass(), "find", "Key is null"))
            )
        );

        orderEntity.ifPresent(entity -> {
            clearDriversSet(entity);
            removeTruck(entity);
            clearWaypointsSet(entity);
            orderDao.delete(entity);
        });
    }

    /**
     * Update method implementation.
     *
     * @param dto    DTO object.
     * @param entity Entity object.
     */
    private void updateImpl(OrderDto dto, OrderEntity entity) {
        if (!dto.getUniqueIdentificator().equals(entity.getUniqueIdentificator())) {
            entity.setUniqueIdentificator(dto.getUniqueIdentificator());
        }

        if (!dto.getOrderStatus().equals(entity.getOrderStatus())) {
            entity.setOrderStatus(dto.getOrderStatus());
        }

        if (dto.getTruckUID() != null) {
            updateTruck(dto.getTruckUID(), entity);
        } else {
            removeTruck(entity);
        }

        Optional.ofNullable(dto.getDriverUIDSet()).ifPresent(driversSet -> {
            if (!driversSet.isEmpty()) {
                updateDriversSet(driversSet
                    .stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()), entity);
            } else {
                clearDriversSet(entity);
            }
        });

        Optional.ofNullable(dto.getWaypointsIDSet()).ifPresent(waypointsSet -> {
            if (!waypointsSet.isEmpty()) {
                updateWaypointsSet(waypointsSet
                    .stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()), entity);
            } else {
                clearWaypointsSet(entity);
            }
        });
    }

    /**
     * Updates the drivers set of the entity.
     *
     * @param driversUIDSet set of UIDs of the drivers.
     * @param entity        Entity object.
     */
    private void updateDriversSet(Set<String> driversUIDSet, OrderEntity entity) {
        Set<String> currentDriversUIDSet = entity.getAssignedDrivers()
            .stream()
            .filter(Objects::nonNull)
            .map(AbstractEntity::getUniqueIdentificator)
            .collect(Collectors.toSet());

        if (!currentDriversUIDSet.equals(driversUIDSet)) {
            // clear the driver set
            clearDriversSet(entity);
            // for each uid in the set
            driversUIDSet.forEach(uid -> {
                // try get the driver entity from DB
                DriverEntity driverEntity = Optional.ofNullable(driverDao.findByUniqueKey(uid))
                    .orElseThrow(
                        () -> new DAOException(getClass(), "updateOrder", "Entity with such UID does not exist")
                    );
                // if driver has already been assigned to a truck
                // then remove him
                Optional.ofNullable(driverEntity.getDriverOrder())
                    .ifPresent(e -> e.removeDriver(driverEntity));
                // add driver to this truck
                entity.addDriver(driverEntity);
            });
        }
    }

    /**
     * Clears the drivers set of the entity.
     *
     * @param entity Entity object.
     */
    private void clearDriversSet(OrderEntity entity) {
        Set<DriverEntity> driverEntitySet = entity.getAssignedDrivers()
            .stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        driverEntitySet.forEach(entity::removeDriver);
    }

    /**
     * Updates the trucks set of the entity.
     *
     * @param truckUID    UID of the truck.
     * @param entity        Entity object.
     */
    private void updateTruck(String truckUID, OrderEntity entity) {
        String currentTruckUID = Optional
            .ofNullable(entity.getAssignedTruck())
            .map(AbstractEntity::getUniqueIdentificator)
            .orElse("");
        if (!currentTruckUID.equals(truckUID)) {
            TruckEntity truckEntity = Optional
                .ofNullable(truckDao.findByUniqueKey(truckUID))
                .orElseThrow(() -> new DAOException(getClass(), "updateTruck", "Entity with such UID does not exist"));

            Optional.ofNullable(entity.getAssignedTruck()).ifPresent(e -> {
                e.setTruckOrder(null);
                entity.setAssignedTruck(null);
            });

            entity.setAssignedTruck(truckEntity);
            truckEntity.setTruckOrder(entity);
        }
    }

    /**
     * Clears the trucks set of the entity.
     *
     * @param entity Entity object.
     */
    private void removeTruck(OrderEntity entity) {
        Optional.ofNullable(entity.getAssignedTruck()).ifPresent(e -> {
            e.setTruckOrder(null);
            entity.setAssignedTruck(null);
        });
    }

    /**
     * Updates the waypoints set of the entity.
     *
     * @param waypointsIDSet set of UIDs of the drivers.
     * @param entity        Entity object.
     */
    private void updateWaypointsSet(Set<Integer> waypointsIDSet, OrderEntity entity) {
        Set<Integer> currentWaypointsIDSet = entity.getAssignedWaypoints()
            .stream()
            .filter(Objects::nonNull)
            .map(WayPointEntity::getId)
            .collect(Collectors.toSet());

        if (!currentWaypointsIDSet.equals(waypointsIDSet)) {
            // clear the driver set
            clearWaypointsSet(entity);
            // for each uid in the set
            waypointsIDSet.forEach(uid -> {
                // try get the driver entity from DB
                WayPointEntity wayPointEntity = Optional.ofNullable(wayPointDao.findById(uid))
                    .orElseThrow(
                        () -> new DAOException(getClass(), "updateOrder", "Entity with such UID does not exist")
                    );
                // if driver has already been assigned to a truck
                // then remove him
                Optional.ofNullable(wayPointEntity.getWaypointOrder())
                    .ifPresent(e -> e.removeWayPoint(wayPointEntity));
                // add driver to this truck
                entity.addWayPoint(wayPointEntity);
            });
        }
    }

    /**
     * Clears the waypoints set of the entity.
     *
     * @param entity Entity object.
     */
    private void clearWaypointsSet(OrderEntity entity) {
        Set<WayPointEntity> waypointEntitySet = entity.getAssignedWaypoints()
            .stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        waypointEntitySet.forEach(entity::removeWayPoint);
    }

    /**
     * Validates the input DTO object and throws NPE,
     * if object or one of required fields is null.
     *
     * @param dto DTO object.
     */
    private void validateRequiredFields(OrderDto dto) {
        if (dto == null || dto.getUniqueIdentificator() == null || dto.getOrderStatus() == null
            || dto.getOrderStart() == null || dto.getOrderEnd() == null) {
            throw new ValidationException();
        }
    }
}
