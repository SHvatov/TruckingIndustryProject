package com.ishvatov.service.inner.truck;

import com.ishvatov.exception.DAOException;
import com.ishvatov.mapper.Mapper;
import com.ishvatov.model.dao.city.CityDao;
import com.ishvatov.model.dao.driver.DriverDao;
import com.ishvatov.model.dao.order.OrderDao;
import com.ishvatov.model.dao.truck.TruckDao;
import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.model.entity.buisness.CityEntity;
import com.ishvatov.model.entity.buisness.DriverEntity;
import com.ishvatov.model.entity.buisness.OrderEntity;
import com.ishvatov.model.entity.buisness.TruckEntity;
import com.ishvatov.service.inner.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

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
    public TruckServiceImpl(TruckDao truckDao, CityDao cityDao, DriverDao driverDao, OrderDao orderDao, Mapper<TruckEntity, TruckDto> mapper) {
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
     * @throws DAOException         if entity with this UID already exists
     * @throws NullPointerException if DTO field, which is corresponding to
     *                              the not nullable field in the Entity object is null.
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
     * @throws DAOException         if entity with this UID already exists
     * @throws NullPointerException if DTO field, which is corresponding to
     *                              the not nullable field in the Entity object is null.
     */
    @Override
    public void update(TruckDto dtoObj) {
        validateRequiredFields(dtoObj);
        TruckEntity entity = truckDao.findByUniqueKey(dtoObj.getUniqueIdentificator());
        if (entity == null) {
            throw new DAOException(getClass(), "update", "Entity with such UID does not exist");
        } else {
            updateImpl(dtoObj, entity);
        }
    }

    /**
     * Deletes entity from the DB if it exists.
     *
     * @param key UID of the entity.
     */
    @Override
    public void delete(String key) {
        if (Objects.isNull(key)) {
            throw new NullPointerException();
        }

        TruckEntity truckEntity = truckDao.findByUniqueKey(key);
        if (truckEntity != null) {
            CityEntity cityEntity = truckEntity.getTruckCity();
            if (cityEntity != null) {
                cityEntity.removeTruck(truckEntity);
            }

            OrderEntity orderEntity = truckEntity.getTruckOrder();
            if (orderEntity != null) {
                orderEntity.setTruckEntity(null);
                truckEntity.setTruckOrder(null);
            }

            Set<DriverEntity> driverEntitySet = truckEntity.getTruckDriversSet();
            for (DriverEntity driverEntity : driverEntitySet) {
                truckEntity.removeDriver(driverEntity);
            }

            truckDao.delete(truckEntity);
        }
    }

    /**
     * Update method implementation.
     *
     * @param dto    DTO object.
     * @param entity Entity object.
     */
    private void updateImpl(TruckDto dto, TruckEntity entity) {
        entity.setUniqueIdentificator(dto.getUniqueIdentificator());
        entity.setTruckCondition(dto.getTruckCondition());
        entity.setTruckCapacity(dto.getTruckCapacity());
        entity.setTruckDriverShiftSize(dto.getTruckDriverShiftSize());

        // add city to the entity
        updateCity(dto.getTruckCityUID(), entity);

        // add / replace order
        updateOrder(dto.getTruckOrderUID(), entity);

        // add / replace drivers
        updateDriverSet(dto.getTruckDriverUIDSet(), entity);
    }

    /**
     * Updates the city of the entity.
     *
     * @param cityUID UID of the city.
     * @param entity  Entity object.
     */
    private void updateCity(String cityUID, TruckEntity entity) {
        if (cityUID != null) {
            CityEntity cityEntity = cityDao.findByUniqueKey(cityUID);
            if (cityEntity == null) {
                throw new DAOException(getClass(), "updateCity", "Entity with such UID does not exist");
            }

            CityEntity previousCity = entity.getTruckCity();
            if (previousCity != null) {
                previousCity.removeTruck(entity);
            }
            cityEntity.addTruck(entity);
        } else {
            CityEntity previousCity = entity.getTruckCity();
            if (previousCity != null) {
                previousCity.removeTruck(entity);
            }
        }
    }

    /**
     * Updates the order of the entity.
     *
     * @param orderUID UID of the order.
     * @param entity   Entity object.
     */
    private void updateOrder(String orderUID, TruckEntity entity) {
        if (orderUID != null) {
            OrderEntity orderEntity = orderDao.findByUniqueKey(orderUID);
            if (orderEntity == null) {
                throw new DAOException(getClass(), "updateOrder", "Entity with such UID does not exist");
            }

            OrderEntity previousOrder = entity.getTruckOrder();
            if (previousOrder != null) {
                previousOrder.setTruckEntity(null);
            }
            orderEntity.setTruckEntity(entity);
            entity.setTruckOrder(orderEntity);
        } else {
            OrderEntity previousOrder = entity.getTruckOrder();
            if (previousOrder != null) {
                previousOrder.setTruckEntity(null);
                entity.setTruckOrder(null);
            }
        }
    }

    /**
     * Updates the set of drivers, who are assigned to this truck.
     *
     * @param driverUIDSet Set of the drivers UID.
     * @param entity       Entity object.
     */
    private void updateDriverSet(Set<String> driverUIDSet, TruckEntity entity) {
        if (driverUIDSet != null) {
            // todo should i check if null
            Set<DriverEntity> previousDrivers = entity.getTruckDriversSet();
            if (previousDrivers != null && !previousDrivers.isEmpty()) {
                for (DriverEntity driverEntity : previousDrivers) {
                    entity.removeDriver(driverEntity);
                }
            }

            // otherwise add all elements from the set
            if (!driverUIDSet.isEmpty()) {
                for (String driverUID : driverUIDSet) {
                    DriverEntity driverEntity = driverDao.findByUniqueKey(driverUID);
                    if (driverEntity == null) {
                        throw new DAOException(getClass(), "updateDriverSet", "Entity with such UID does not exist");
                    } else {
                        entity.addDriver(driverEntity);
                    }
                }
            }
        }
    }

    /**
     * Validates the input DTO object and throws NPE,
     * if object or one of required fields is null.
     *
     * @param dto DTO object.
     */
    private void validateRequiredFields(TruckDto dto) {
        if (dto == null || dto.getUniqueIdentificator() == null
            || dto.getTruckCondition() == null
            || dto.getTruckCapacity() == null || dto.getTruckDriverShiftSize() == null) {
            throw new NullPointerException();
        }

        if (dto.getTruckDriverUIDSet() != null && dto.getTruckDriverUIDSet().contains(null)) {
            throw new NullPointerException();
        }
    }
}
