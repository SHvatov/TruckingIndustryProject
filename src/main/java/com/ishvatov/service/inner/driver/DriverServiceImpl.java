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
            driverDao.findByUniqueKey(
                Optional.ofNullable(key).orElseThrow(() -> new ValidationException(getClass(), "find", "Key is null"))
            )
        );

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
    public List<String> getAllDriversUID() {
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

        if (!dto.getDriverWorkedHours().equals(entity.getDriverWorkedHours())) {
            entity.setDriverWorkedHours(dto.getDriverWorkedHours());
        }

        if (!dto.getLastUpdated().equals(entity.getLastUpdated())) {
            entity.setLastUpdated(dto.getLastUpdated());
        }

        if (!dto.getDriverStatus().equals(entity.getDriverStatus())) {
            entity.setDriverStatus(dto.getDriverStatus());
        }

        if (!dto.getDriverSurname().equals(entity.getDriverSurname())) {
            entity.setDriverSurname(dto.getDriverSurname());
        }

        if (!dto.getDriverName().equals(entity.getDriverName())) {
            entity.setDriverName(dto.getDriverName());
        }

        if (dto.getCurrentCityUID() != null) {
            updateCity(dto.getCurrentCityUID(), entity);
        } else {
            removeCity(entity);
        }

        if (dto.getDriverTruckUID() != null) {
            updateTruck(dto.getDriverTruckUID(), entity);
        } else {
            removeTruck(entity);
        }

        if (dto.getDriverOrderUID() != null) {
            updateOrder(dto.getDriverOrderUID(), entity);
        } else {
            removeOrder(entity);
        }
    }

    /**
     * Updates the city of the entity.
     *
     * @param cityUID UID of the city.
     * @param entity  Entity object.
     */
    private void updateCity(String cityUID, DriverEntity entity) {
        String currentCityUID = Optional
            .ofNullable(entity.getDriverCity())
            .map(AbstractEntity::getUniqueIdentificator)
            .orElse("");
        if (!currentCityUID.equals(cityUID)) {
            CityEntity cityEntity = Optional
                .ofNullable(cityDao.findByUniqueKey(cityUID))
                .orElseThrow(() -> new DAOException(getClass(), "updateCity", "Entity with such UID does not exist"));
            Optional.ofNullable(entity.getDriverCity()).ifPresent(e -> e.removeDriver(entity));
            cityEntity.addDriver(entity);
        }
    }

    /**
     * Deletes the city of the entity.
     *
     * @param entity  Entity object.
     */
    private void removeCity(DriverEntity entity) {
        Optional.ofNullable(entity.getDriverCity()).ifPresent(e -> e.removeDriver(entity));
    }

    /**
     * Updates the order of the entity.
     *
     * @param orderUID UID of the order.
     * @param entity   Entity object.
     */
    private void updateOrder(String orderUID, DriverEntity entity) {
        String currentOrderUID = Optional
            .ofNullable(entity.getDriverOrder())
            .map(AbstractEntity::getUniqueIdentificator)
            .orElse("");
        if (!currentOrderUID.equals(orderUID)) {
            OrderEntity orderEntity = Optional
                .ofNullable(orderDao.findByUniqueKey(orderUID))
                .orElseThrow(() -> new DAOException(getClass(), "updateOrder", "Entity with such UID does not exist"));
            Optional.ofNullable(entity.getDriverOrder()).ifPresent(e -> e.removeDriver(entity));
            orderEntity.addDriver(entity);
        }
    }

    /**
     * Deletes the order of the entity.
     *
     * @param entity   Entity object.
     */
    private void removeOrder(DriverEntity entity) {
       Optional.ofNullable(entity.getDriverOrder()).ifPresent(e -> e.removeDriver(entity));
    }

    /**
     * Updates the truck of the entity.
     *
     * @param truckUID UID of the truck.
     * @param entity   Entity object.
     */
    private void updateTruck(String truckUID, DriverEntity entity) {
        String currentTruckUID = Optional
            .ofNullable(entity.getDriverTruck())
            .map(AbstractEntity::getUniqueIdentificator)
            .orElse("");
        if (!currentTruckUID.equals(truckUID)) {
            TruckEntity truckEntity = Optional
                .ofNullable(truckDao.findByUniqueKey(truckUID))
                .orElseThrow(() -> new DAOException(getClass(), "updateTruck", "Entity with such UID does not exist"));
            Optional.ofNullable(entity.getDriverTruck()).ifPresent(e -> e.removeDriver(entity));
            truckEntity.addDriver(entity);
        }
    }

    /**
     * Deletes the truck of the entity.
     *
     * @param entity Entity object.
     */
    private void removeTruck(DriverEntity entity) {
        Optional.ofNullable(entity.getDriverTruck()).ifPresent(e -> e.removeDriver(entity));
    }


    /**
     * Validates the input DTO object and throws NPE,
     * if object or one of required fields is null.
     *
     * @param dto DTO object.
     */
    private void validateRequiredFields(DriverDto dto) {
        if (dto == null || dto.getUniqueIdentificator() == null
            || dto.getDriverWorkedHours() == null || dto.getDriverStatus() == null
            || dto.getDriverName() == null || dto.getDriverSurname() == null
            || dto.getLastUpdated() == null) {
            throw new NullPointerException();
        }
    }
}
