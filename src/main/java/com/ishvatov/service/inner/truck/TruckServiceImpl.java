package com.ishvatov.service.inner.truck;

import com.ishvatov.exception.DAOException;
import com.ishvatov.exception.ValidationException;
import com.ishvatov.mapper.Mapper;
import com.ishvatov.model.dao.city.CityDao;
import com.ishvatov.model.dao.driver.DriverDao;
import com.ishvatov.model.dao.order.OrderDao;
import com.ishvatov.model.dao.truck.TruckDao;
import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.buisness.CityEntity;
import com.ishvatov.model.entity.buisness.DriverEntity;
import com.ishvatov.model.entity.buisness.OrderEntity;
import com.ishvatov.model.entity.buisness.TruckEntity;
import com.ishvatov.service.inner.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Basic {@link TruckService} interface implementation.
 *
 * @author Sergey Khvatov
 */
@Service("truckService")
@Transactional
public class TruckServiceImpl extends AbstractService<String, TruckEntity, TruckDto> implements TruckService {

    /**
     * Autowired DAO field.
     */
    private TruckDao truckDao;

    /**
     * Autowired DAO field.
     */
    private CityDao cityDao;

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
     * @param cityDao  autowired {@link CityDao} impl.
     * @param orderDao autowired {@link OrderDao} impl.
     */
    @Autowired
    public TruckServiceImpl(TruckDao truckDao, CityDao cityDao,
                            DriverDao driverDao, OrderDao orderDao,
                            Mapper<TruckEntity, TruckDto> mapper) {
        super(truckDao, mapper);
        this.cityDao = cityDao;
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
    public void save(TruckDto dtoObj) {
        validateRequiredFields(dtoObj);
        if (exists(dtoObj.getUniqueIdentificator())) {
            throw new DAOException(getClass(), "save", "Entity with such UID already exists");
        } else {
            TruckEntity entity = new TruckEntity();
            updateImpl(dtoObj, entity);
            truckDao.save(entity);
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
    public void update(TruckDto dtoObj) {
        validateRequiredFields(dtoObj);
        TruckEntity entity = Optional
            .ofNullable(truckDao.findByUniqueKey(dtoObj.getUniqueIdentificator()))
            .orElseThrow(() -> new DAOException(getClass(), "update", "Entity with such UID does not exist"));
        updateImpl(dtoObj, entity);
    }

    /**
     * Deletes entity from the DB if it exists.
     *
     * @param key UID of the entity.
     * @throws ValidationException if key is null.
     */
    @Override
    public void delete(String key) {
        Optional<TruckEntity> truckEntity = Optional.ofNullable(
            truckDao.findByUniqueKey(Optional.ofNullable(key)
                .orElseThrow(() -> new ValidationException(getClass(), "find", "Key is null")))
        );

        truckEntity.ifPresent(entity -> {
            removeCity(entity);
            removeOrder(entity);
            clearDriversSet(entity);
            truckDao.delete(entity);
        });
    }

    /**
     * Update method implementation.
     *
     * @param dto    DTO object.
     * @param entity Entity object.
     */
    private void updateImpl(TruckDto dto, TruckEntity entity) {
        if (!dto.getUniqueIdentificator().equals(entity.getUniqueIdentificator())) {
            entity.setUniqueIdentificator(dto.getUniqueIdentificator());
        }

        if (!dto.getCapacity().equals(entity.getCapacity())) {
            entity.setCapacity(dto.getCapacity());
        }

        if (!dto.getStatus().equals(entity.getStatus())) {
            entity.setStatus(dto.getStatus());
        }

        if (!dto.getShiftSize().equals(entity.getShiftSize())) {
            entity.setShiftSize(dto.getShiftSize());
        }

        if (dto.getCityId() != null) {
            updateCity(dto.getCityId(), entity);
        } else {
            removeCity(entity);
        }

        if (dto.getOrderId() != null) {
            updateOrder(dto.getOrderId(), entity);
        } else {
            removeOrder(entity);
        }

        Optional.ofNullable(dto.getAssignedDrivers()).ifPresent(driversSet -> {
            if (!driversSet.isEmpty()) {
                updateAssignedDrivers(driversSet
                    .stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()), entity);
            } else {
                clearDriversSet(entity);
            }
        });
    }

    /**
     * Updates the city of the entity.
     *
     * @param cityId UID of the city.
     * @param entity  Entity object.
     */
    private void updateCity(String cityId, TruckEntity entity) {
        String currentCityUID = Optional
            .ofNullable(entity.getCity())
            .map(AbstractEntity::getUniqueIdentificator)
            .orElse("");
        if (!currentCityUID.equals(cityId)) {
            CityEntity cityEntity = Optional.ofNullable(cityDao.findByUniqueKey(cityId))
                .orElseThrow(() -> new DAOException(getClass(), "updateCity", "Entity with such UID does not exist"));
            Optional.ofNullable(entity.getCity()).ifPresent(e -> e.removeTruck(entity));
            cityEntity.addTruck(entity);
        }
    }

    /**
     * Deletes the city of the entity.
     *
     * @param entity  Entity object.
     */
    private void removeCity(TruckEntity entity) {
        Optional.ofNullable(entity.getCity()).ifPresent(e -> e.removeTruck(entity));
    }

    /**
     * Updates the order of the entity.
     *
     * @param orderId UID of the order.
     * @param entity   Entity object.
     */
    private void updateOrder(String orderId, TruckEntity entity) {
        String currentOrderUID = Optional
            .ofNullable(entity.getOrder())
            .map(AbstractEntity::getUniqueIdentificator)
            .orElse("");

        if (!currentOrderUID.equals(orderId)) {
            OrderEntity orderEntity = Optional
                .ofNullable(orderDao.findByUniqueKey(orderId))
                .orElseThrow(() -> new DAOException(getClass(), "updateOrder", "Entity with such UID does not exist"));

            Optional.ofNullable(entity.getOrder()).ifPresent(e -> {
                e.setAssignedTruck(null);
                entity.setOrder(null);
            });

            orderEntity.setAssignedTruck(entity);
            entity.setOrder(orderEntity);
        }
    }

    /**
     * Deletes the order of the entity.
     *
     * @param entity   Entity object.
     */
    private void removeOrder(TruckEntity entity) {
        Optional.ofNullable(entity.getOrder()).ifPresent(e -> {
            e.setAssignedTruck(null);
            entity.setOrder(null);
        });
    }

    /**
     * Updates the set of drivers of the entity.
     *
     * @param assignedDrivers UID of the order.
     * @param entity   Entity object.
     */
    private void updateAssignedDrivers(Set<String> assignedDrivers, TruckEntity entity) {
        Set<String> currentDriversUIDSet = entity.getAssignedDrivers()
            .stream()
            .filter(Objects::nonNull)
            .map(AbstractEntity::getUniqueIdentificator)
            .collect(Collectors.toSet());

        if (!currentDriversUIDSet.equals(assignedDrivers)) {
            // clear the driver set
            clearDriversSet(entity);
            // for each uid in the set
            assignedDrivers.forEach(uid -> {
                // try get the driver entity from DB
                DriverEntity driverEntity = Optional.ofNullable(driverDao.findByUniqueKey(uid))
                    .orElseThrow(
                        () -> new DAOException(getClass(), "updateAssignedDrivers", "Entity with such UID does not exist")
                    );
                // if driver has already been assigned to a truck
                // then remove him
                Optional.ofNullable(driverEntity.getTruck())
                    .ifPresent(e -> e.removeDriver(driverEntity));
                // add driver to this truck
                entity.addDriver(driverEntity);
            });
        }
    }

    /**
     * Deletes the order of the entity.
     *
     * @param entity   Entity object.
     */
    private void clearDriversSet(TruckEntity entity) {
        Set<DriverEntity> driverEntitySet = entity.getAssignedDrivers()
            .stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        driverEntitySet.forEach(entity::removeDriver);
    }

    /**
     * Validates the input DTO object and throws NPE,
     * if object or one of required fields is null.
     *
     * @param dto DTO object.
     */
    private void validateRequiredFields(TruckDto dto) {
        if (dto == null || dto.getUniqueIdentificator() == null
            || dto.getStatus() == null || dto.getCapacity() == null
            || dto.getShiftSize() == null) {
            throw new ValidationException();
        }
    }
}
