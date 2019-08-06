package com.ishvatov.service.inner.truck;

import com.ishvatov.exception.DAOException;
import com.ishvatov.mapper.Mapper;
import com.ishvatov.model.dao.city.CityDao;
import com.ishvatov.model.dao.order.OrderDao;
import com.ishvatov.model.dao.truck.TruckDao;
import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.model.entity.buisness.CityEntity;
import com.ishvatov.model.entity.buisness.OrderEntity;
import com.ishvatov.model.entity.buisness.TruckEntity;
import com.ishvatov.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic {@link TruckService} interface implementation.
 *
 * @author Sergey Khvatov
 */
@Service("truckService")
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
     * Default class constructor, that is used
     * to inject DAO interface implementation and
     * initialize the super class.
     *
     * @param mapper    {@link Mapper} implementation.
     * @param truckDao  autowired {@link TruckDao} impl.
     * @param cityDao   autowired {@link CityDao} impl.
     * @param orderDao  autowired {@link OrderDao} impl.
     */
    @Autowired
    public TruckServiceImpl(TruckDao truckDao, CityDao cityDao,
                            OrderDao orderDao, Mapper<TruckEntity, TruckDto> mapper) {
        super(truckDao, mapper);
        this.cityDao = cityDao;
        this.orderDao = orderDao;
        this.truckDao = truckDao;
    }

    /**
     * Adds entity to the DB. Check if entity already exists.
     *
     * @param dtoObj new entity to add.
     * @throws DAOException if entity with this UID already exists
     */
    @Transactional
    @Override
    public void save(TruckDto dtoObj) {
        if (truckDao.exists(dtoObj.getUniqueIdentificator())) {
            throw new DAOException(getClass(), "save", "Entity with such UID already exists");
        } else {
            // create new instance
            TruckEntity entity = new TruckEntity();

            // set UID
            entity.setUniqueIdentificator(dtoObj.getUniqueIdentificator());

            // static fields are required
            updateRegularFieldsImpl(dtoObj, entity);

            // city is not required
            if (dtoObj.getTruckCityUID() != null) {
                updateCityImpl(dtoObj.getTruckCityUID(), entity);
            }

            // order is not required
            if (dtoObj.getTruckOrderUID() != null) {
                updateOrderImpl(dtoObj.getTruckOrderUID(), entity);
            }

            // save entity
            truckDao.save(entity);
        }
    }

    /**
     * Updates static field of the object.
     *
     * @param dtoObj   {@link TruckDto} instance.
     * @param truckUID UID of the modified truck.
     */
    @Transactional
    @Override
    public void updateRegularFields(TruckDto dtoObj, String truckUID) {
        TruckEntity entity = truckDao.findByUniqueKey(dtoObj.getUniqueIdentificator());
        if (entity == null) {
            throw new DAOException(getClass(), "updateRegularFields", "Entity with such UID does not exist");
        } else {
            updateRegularFieldsImpl(dtoObj, entity);
        }
    }

    /**
     * Updates city.
     *
     * @param cityUID      uid of the city.
     * @param truckUID UID of the modified truck.
     */
    @Transactional
    @Override
    public void updateCity(String cityUID, String truckUID) {
        TruckEntity entity = truckDao.findByUniqueKey(truckUID);
        if (entity == null) {
            throw new DAOException(getClass(), "updateCity", "Entity with such UID does not exist");
        } else {
            updateCityImpl(cityUID, entity);
        }
    }

    /**
     * Updates order.
     *
     * @param orderUID      uid of the order.
     * @param truckUID UID of the modified truck.
     */
    @Transactional
    @Override
    public void updateOrder(String orderUID, String truckUID) {
        TruckEntity entity = truckDao.findByUniqueKey(truckUID);
        if (entity == null) {
            throw new DAOException(getClass(), "updateOrder", "Entity with such UID does not exist");
        } else {
            updateOrderImpl(orderUID, entity);
        }
    }

    /**
     * Removes order from the truck.
     *
     * @param truckUID uid of the truck.
     */
    @Override
    public void removeOrder(String truckUID) {
        TruckEntity truckEntity = truckDao.findByUniqueKey(truckUID);
        if (truckEntity == null) {
            throw new DAOException(getClass(), "updateDriver", "Entity with such UID does not exist");
        }
        truckEntity.setTruckOrder(null);
    }

    /**
     * Removes city from the truck and sets it to the default value.
     *
     * @param truckUID uid of the truck.
     */
    @Override
    public void removeCity(String truckUID) {
        TruckEntity truckEntity = truckDao.findByUniqueKey(truckUID);
        if (truckEntity == null) {
            throw new DAOException(getClass(), "updateDriver", "Entity with such UID does not exist");
        }
        truckEntity.setTruckCity(null);
    }

    /**
     * Updates static field of the object.
     *
     * @param dtoObj {@link TruckDto} instance.
     * @param truckEntity {@link TruckEntity} instance.
     */
    private void updateRegularFieldsImpl(TruckDto dtoObj, TruckEntity truckEntity) {
        if (dtoObj.getTruckDriverShiftSize() != null) truckEntity.setTruckDriverShiftSize(dtoObj.getTruckDriverShiftSize());
        if (dtoObj.getTruckCondition() != null) truckEntity.setTruckCondition(dtoObj.getTruckCondition());
        if (dtoObj.getTruckCapacity() != null) truckEntity.setTruckCapacity(dtoObj.getTruckCapacity());
    }

    /**
     * Updates city.
     *
     * @param uid         uid of the city.
     * @param truckEntity {@link TruckEntity} instance.
     */
    private void updateCityImpl(String uid, TruckEntity truckEntity) {
        CityEntity cityEntity = cityDao.findByUniqueKey(uid);
        if (cityEntity == null) {
            throw new DAOException(getClass(), "updateCityImpl", "Entity with such UID does not exist");
        }
        truckEntity.setTruckCity(cityEntity);
    }

    /**
     * Updates order.
     *
     * @param uid         uid of the order.
     * @param truckEntity {@link TruckEntity} instance.
     */
    private void updateOrderImpl(String uid, TruckEntity truckEntity) {
        OrderEntity orderEntity = orderDao.findByUniqueKey(uid);
        if (orderEntity == null) {
            throw new DAOException(getClass(), "updateOrderImpl", "Entity with such UID does not exist");
        }
        truckEntity.setTruckOrder(orderEntity);
    }
}
