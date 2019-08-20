package com.ishvatov.service.inner.driver;

import com.ishvatov.exception.DAOException;
import com.ishvatov.exception.ValidationException;
import com.ishvatov.mapper.Mapper;
import com.ishvatov.model.dao.city.CityDao;
import com.ishvatov.model.dao.driver.DriverDao;
import com.ishvatov.model.dao.order.OrderDao;
import com.ishvatov.model.dao.truck.TruckDao;
import com.ishvatov.model.dto.DriverDto;
import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.buisness.CityEntity;
import com.ishvatov.model.entity.buisness.DriverEntity;
import com.ishvatov.model.entity.buisness.OrderEntity;
import com.ishvatov.model.entity.buisness.TruckEntity;
import com.ishvatov.service.inner.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Basic {@link DriverService} interface implementation.
 *
 * @author Sergey Khvatov
 */
@Service("driverService")
@Transactional
public class DriverServiceImpl extends AbstractService<String, DriverEntity, DriverDto> implements DriverService {

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
     * @param mapper    {@link Mapper} implementation.
     * @param truckDao  autowired {@link TruckDao} impl.
     * @param driverDao autowired {@link DriverDao} impl.
     * @param cityDao   autowired {@link CityDao} impl.
     * @param orderDao  autowired {@link OrderDao} impl.
     */
    @Autowired
    public DriverServiceImpl(DriverDao driverDao, TruckDao truckDao,
                             CityDao cityDao, OrderDao orderDao,
                             Mapper<DriverEntity, DriverDto> mapper) {
        super(driverDao, mapper);
        this.cityDao = cityDao;
        this.orderDao = orderDao;
        this.truckDao = truckDao;
        this.driverDao = driverDao;
    }

    /**
     * Adds entity to the DB. Check if entity already exists.
     *
     * @param dtoObj new entity to add.
     * @throws DAOException         if entity with this UID already exists
     * @throws NullPointerException if DTO field, which is corresponding to
     *                              the not nullable field in the Entity object is null.
     */
    @Override
    public void save(DriverDto dtoObj) {
        validateRequiredFields(dtoObj);
        if (exists(dtoObj.getUniqueIdentificator())) {
            throw new DAOException(getClass(), "save", "Entity with such UID already exists");
        } else {
            DriverEntity entity = new DriverEntity();
            updateImpl(dtoObj, entity);
            driverDao.save(entity);
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
     * @throws DAOException         if entity with this UID already exists
     * @throws NullPointerException if DTO field, which is corresponding to
     *                              the not nullable field in the Entity object is null.
     */
    @Override
    public void update(DriverDto dtoObj) {
        validateRequiredFields(dtoObj);
        DriverEntity entity = Optional
            .ofNullable(driverDao.findByUniqueKey(dtoObj.getUniqueIdentificator()))
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
        Optional<DriverEntity> driverEntity = Optional.ofNullable(
            driverDao.findByUniqueKey(Optional.ofNullable(key)
                    .orElseThrow(() -> new ValidationException(getClass(), "find", "Key is null"))));

        driverEntity.ifPresent(entity -> {
            removeCity(entity);
            removeOrder(entity);
            removeTruck(entity);
            driverDao.delete(entity);
        });
    }

    /**
     * Get all list of UID of all drivers in the DB.
     *
     * @return list with UID of all drivers in the DB.
     */
    @Override
    public List<String> getAllDrivers() {
        return driverDao.findAll()
            .stream()
            .filter(Objects::nonNull)
            .map(AbstractEntity::getUniqueIdentificator)
            .collect(Collectors.toList());
    }

    /**
     * Update method implementation.
     *
     * @param dto    DTO object.
     * @param entity Entity object.
     */
    private void updateImpl(DriverDto dto, DriverEntity entity) {
        if (!dto.getUniqueIdentificator().equals(entity.getUniqueIdentificator())) {
            entity.setUniqueIdentificator(dto.getUniqueIdentificator());
        }

        if (!dto.getWorkedHours().equals(entity.getWorkedHours())) {
            entity.setWorkedHours(dto.getWorkedHours());
        }

        if (!dto.getLastUpdated().equals(entity.getLastUpdated())) {
            entity.setLastUpdated(dto.getLastUpdated());
        }

        if (!dto.getStatus().equals(entity.getStatus())) {
            entity.setStatus(dto.getStatus());
        }

        if (!dto.getSurname().equals(entity.getSurname())) {
            entity.setSurname(dto.getSurname());
        }

        if (!dto.getName().equals(entity.getName())) {
            entity.setName(dto.getName());
        }

        if (dto.getCityId() != null) {
            updateCity(dto.getCityId(), entity);
        } else {
            removeCity(entity);
        }

        if (dto.getTruckId() != null) {
            updateTruck(dto.getTruckId(), entity);
        } else {
            removeTruck(entity);
        }

        if (dto.getOrderId() != null) {
            updateOrder(dto.getOrderId(), entity);
        } else {
            removeOrder(entity);
        }
    }

    /**
     * Updates the city of the entity.
     *
     * @param cityId UID of the city.
     * @param entity  Entity object.
     */
    private void updateCity(String cityId, DriverEntity entity) {
        String currentCityUID = Optional
            .ofNullable(entity.getCity())
            .map(AbstractEntity::getUniqueIdentificator)
            .orElse("");
        if (!currentCityUID.equals(cityId)) {
            CityEntity cityEntity = Optional
                .ofNullable(cityDao.findByUniqueKey(cityId))
                .orElseThrow(() -> new DAOException(getClass(), "updateCity", "Entity with such UID does not exist"));
            Optional.ofNullable(entity.getCity()).ifPresent(e -> e.removeDriver(entity));
            cityEntity.addDriver(entity);
        }
    }

    /**
     * Deletes the city of the entity.
     *
     * @param entity  Entity object.
     */
    private void removeCity(DriverEntity entity) {
        Optional.ofNullable(entity.getCity()).ifPresent(e -> e.removeDriver(entity));
    }

    /**
     * Updates the order of the entity.
     *
     * @param orderId UID of the order.
     * @param entity   Entity object.
     */
    private void updateOrder(String orderId, DriverEntity entity) {
        String currentOrderUID = Optional
            .ofNullable(entity.getOrder())
            .map(AbstractEntity::getUniqueIdentificator)
            .orElse("");
        if (!currentOrderUID.equals(orderId)) {
            OrderEntity orderEntity = Optional
                .ofNullable(orderDao.findByUniqueKey(orderId))
                .orElseThrow(() -> new DAOException(getClass(), "updateOrder", "Entity with such UID does not exist"));
            Optional.ofNullable(entity.getOrder()).ifPresent(e -> e.removeDriver(entity));
            orderEntity.addDriver(entity);
        }
    }

    /**
     * Deletes the order of the entity.
     *
     * @param entity   Entity object.
     */
    private void removeOrder(DriverEntity entity) {
       Optional.ofNullable(entity.getOrder()).ifPresent(e -> e.removeDriver(entity));
    }

    /**
     * Updates the truck of the entity.
     *
     * @param truckId UID of the truck.
     * @param entity   Entity object.
     */
    private void updateTruck(String truckId, DriverEntity entity) {
        String currentTruckUID = Optional
            .ofNullable(entity.getTruck())
            .map(AbstractEntity::getUniqueIdentificator)
            .orElse("");
        if (!currentTruckUID.equals(truckId)) {
            TruckEntity truckEntity = Optional
                .ofNullable(truckDao.findByUniqueKey(truckId))
                .orElseThrow(() -> new DAOException(getClass(), "updateTruck", "Entity with such UID does not exist"));
            Optional.ofNullable(entity.getTruck()).ifPresent(e -> e.removeDriver(entity));
            truckEntity.addDriver(entity);
        }
    }

    /**
     * Deletes the truck of the entity.
     *
     * @param entity Entity object.
     */
    private void removeTruck(DriverEntity entity) {
        Optional.ofNullable(entity.getTruck()).ifPresent(e -> e.removeDriver(entity));
    }


    /**
     * Validates the input DTO object and throws NPE,
     * if object or one of required fields is null.
     *
     * @param dto DTO object.
     */
    private void validateRequiredFields(DriverDto dto) {
        if (dto == null || dto.getUniqueIdentificator() == null
            || dto.getWorkedHours() == null || dto.getStatus() == null
            || dto.getName() == null || dto.getSurname() == null
            || dto.getLastUpdated() == null) {
            throw new NullPointerException();
        }
    }
}
