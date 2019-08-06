package com.ishvatov.service.inner.driver;

import com.ishvatov.exception.DAOException;
import com.ishvatov.mapper.Mapper;
import com.ishvatov.model.dao.city.CityDao;
import com.ishvatov.model.dao.driver.DriverDao;
import com.ishvatov.model.dao.order.OrderDao;
import com.ishvatov.model.dao.truck.TruckDao;
import com.ishvatov.model.dto.DriverDto;
import com.ishvatov.model.entity.buisness.CityEntity;
import com.ishvatov.model.entity.buisness.DriverEntity;
import com.ishvatov.model.entity.buisness.OrderEntity;
import com.ishvatov.model.entity.buisness.TruckEntity;
import com.ishvatov.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic {@link DriverService} interface implementation.
 *
 * @author Sergey Khvatov
 */
@Service("driverService")
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
     * @param mapper   {@link Mapper} implementation.
     * @param truckDao autowired {@link TruckDao} impl.
     * @param driverDao autowired {@link DriverDao} impl.
     * @param cityDao  autowired {@link CityDao} impl.
     * @param orderDao autowired {@link OrderDao} impl.
     */
    @Autowired
    public DriverServiceImpl(DriverDao driverDao,
                             TruckDao truckDao, CityDao cityDao,
                             OrderDao orderDao, Mapper<DriverEntity, DriverDto> mapper) {
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
     * @throws DAOException if entity with this UID already exists
     */
    @Transactional
    @Override
    public void save(DriverDto dtoObj) {
        if (driverDao.exists(dtoObj.getUniqueIdentificator())) {
            throw new DAOException(getClass(), "save", "Entity with such UID already exists");
        } else {
            // create new instance
            DriverEntity entity = new DriverEntity();

            // set UID
            entity.setUniqueIdentificator(dtoObj.getUniqueIdentificator());

            // static fields are required
            updateRegularFieldsImpl(dtoObj, entity);

            // city is not required
            if (dtoObj.getCurrentCityUID() != null) {
                updateCityImpl(dtoObj.getCurrentCityUID(), entity);
            }

            // order is not required
            if (dtoObj.getDriverOrderUID() != null) {
                updateOrderImpl(dtoObj.getDriverOrderUID(), entity);
            }

            // truck is not required
            if (dtoObj.getDriverOrderUID() != null) {
                updateTruckImpl(dtoObj.getDriverTruckUID(), entity);
            }

            // save entity
            driverDao.save(entity);
        }
    }

    /**
     * Updates regular field of the object.
     *
     * @param dtoObj    {@link DriverDto} instance.
     * @param driverUID UID of the modified driver.
     */
    @Transactional
    @Override
    public void updateRegularFields(DriverDto dtoObj, String driverUID) {
        DriverEntity entity = driverDao.findByUniqueKey(driverUID);
        if (entity == null) {
            throw new DAOException(getClass(), "updateCity", "Entity with such UID does not exist");
        } else {
            updateRegularFieldsImpl(dtoObj, entity);
        }
    }

    /**
     * Updates regular of the object.
     *
     * @param dtoObj {@link DriverDto} instance.
     * @param driverEntity {@link DriverEntity} instance.
     */
    private void updateRegularFieldsImpl(DriverDto dtoObj, DriverEntity driverEntity) {
        if (dtoObj.getDriverName() != null) driverEntity.setDriverName(dtoObj.getDriverName());
        if (dtoObj.getDriverSurname() != null) driverEntity.setDriverSurname(dtoObj.getDriverSurname());
        if (dtoObj.getDriverStatus() != null) driverEntity.setDriverStatus(dtoObj.getDriverStatus());
        if (dtoObj.getDriverWorkedHours() != null) driverEntity.setDriverWorkedHours(dtoObj.getDriverWorkedHours());
    }


    /**
     * Updates city.
     *
     * @param cityUID   uid of the city.
     * @param driverUID UID of the driver
     */
    @Transactional
    @Override
    public void updateCity(String cityUID, String driverUID) {
        DriverEntity entity = driverDao.findByUniqueKey(driverUID);
        if (entity == null) {
            throw new DAOException(getClass(), "updateCity", "Entity with such UID does not exist");
        } else {
            updateCityImpl(cityUID, entity);
        }
    }

    /**
     * Updates order.
     *
     * @param orderUID  uid of the order.
     * @param driverUID UID of the driver
     */
    @Transactional
    @Override
    public void updateOrder(String orderUID, String driverUID) {
        DriverEntity entity = driverDao.findByUniqueKey(driverUID);
        if (entity == null) {
            throw new DAOException(getClass(), "updateOrder", "Entity with such UID does not exist");
        } else {
            updateOrderImpl(orderUID, entity);
        }
    }

    /**
     * Updates truck.
     *
     * @param truckUID  UID of the truck.
     * @param driverUID UID of the driver.
     */
    @Transactional
    @Override
    public void updateTruck(String truckUID, String driverUID) {
        DriverEntity entity = driverDao.findByUniqueKey(driverUID);
        if (entity == null) {
            throw new DAOException(getClass(), "updateTruck", "Entity with such UID does not exist");
        } else {
            updateTruckImpl(truckUID, entity);
        }
    }

    /**
     * Removes order from the truck.
     *
     * @param driverUID uid of the driver.
     */
    @Transactional
    @Override
    public void removeOrder(String driverUID) {
        DriverEntity driverEntity = driverDao.findByUniqueKey(driverUID);
        if (driverEntity == null) {
            throw new DAOException(getClass(), "removeOrder", "Entity with such UID does not exist");
        }
        driverEntity.setDriverOrder(null);
    }

    /**
     * Removes city from the driver.
     *
     * @param driverUID uid of the driver.
     */
    @Transactional
    @Override
    public void removeCity(String driverUID) {
        DriverEntity driverEntity = driverDao.findByUniqueKey(driverUID);
        if (driverEntity == null) {
            throw new DAOException(getClass(), "removeCity", "Entity with such UID does not exist");
        }
        driverEntity.setDriverCurrentCity(null);
    }

    /**
     * Removes driver from the truck.
     *
     * @param driverUID uid of the driver.
     */
    @Transactional
    @Override
    public void removeTruck(String driverUID) {
        DriverEntity driverEntity = driverDao.findByUniqueKey(driverUID);
        if (driverEntity == null) {
            throw new DAOException(getClass(), "removeTruck", "Entity with such UID does not exist");
        }
        driverEntity.setDriverTruckEntity(null);
    }

    /**
     * Updates city.
     *
     * @param uid         uid of the city.
     * @param driverEntity {@link DriverEntity} instance.
     */
    private void updateCityImpl(String uid, DriverEntity driverEntity) {
        CityEntity cityEntity = cityDao.findByUniqueKey(uid);
        if (cityEntity == null) {
            throw new DAOException(getClass(), "updateCityImpl", "Entity with such UID does not exist");
        }
        driverEntity.setDriverCurrentCity(cityEntity);
    }

    /**
     * Updates order.
     *
     * @param uid         uid of the order.
     * @param driverEntity {@link DriverEntity} instance.
     */
    private void updateOrderImpl(String uid, DriverEntity driverEntity) {
        OrderEntity orderEntity = orderDao.findByUniqueKey(uid);
        if (orderEntity == null) {
            throw new DAOException(getClass(), "updateOrderImpl", "Entity with such UID does not exist");
        }
        driverEntity.setDriverOrder(orderEntity);
    }

    /**
     * Updates truck.
     *
     * @param uid         uid of the order.
     * @param driverEntity {@link DriverEntity} instance.
     */
    private void updateTruckImpl(String uid, DriverEntity driverEntity) {
        TruckEntity truckEntity = truckDao.findByUniqueKey(uid);
        if (truckEntity == null) {
            throw new DAOException(getClass(), "updateTruckImpl", "Entity with such UID does not exist");
        }
        driverEntity.setDriverTruckEntity(truckEntity);
    }
}
