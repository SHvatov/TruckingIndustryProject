package com.ishvatov.service.inner.order;

import com.ishvatov.exception.DAOException;
import com.ishvatov.mapper.Mapper;
import com.ishvatov.model.dao.city.CityDao;
import com.ishvatov.model.dao.driver.DriverDao;
import com.ishvatov.model.dao.order.OrderDao;
import com.ishvatov.model.dao.truck.TruckDao;
import com.ishvatov.model.dto.OrderDto;
import com.ishvatov.model.entity.buisness.OrderEntity;
import com.ishvatov.service.inner.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param cityDao   autowired {@link CityDao} impl.
     * @param orderDao  autowired {@link OrderDao} impl.
     */
    @Autowired
    public OrderServiceImpl(TruckDao truckDao, CityDao cityDao, DriverDao driverDao,
                            OrderDao orderDao, Mapper<OrderEntity, OrderDto> mapper) {
        super(orderDao, mapper);
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
     * @throws DAOException         if entity with this UID already exists
     * @throws NullPointerException if DTO field, which is corresponding to
     *                              the not nullable field in the Entity object is null.
     */
    @Override
    public void update(OrderDto dtoObj) {
        validateRequiredFields(dtoObj);
        OrderEntity entity = orderDao.findByUniqueKey(dtoObj.getUniqueIdentificator());
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
        // todo add delete method for order
        // todo delete all waypoints with order
        throw new UnsupportedOperationException();
    }

    /**
     * Update method implementation.
     *
     * @param dto    DTO object.
     * @param entity Entity object.
     */
    private void updateImpl(OrderDto dto, OrderEntity entity) {
        entity.setOrderStatus(dto.getOrderStatus());

        // todo add / remove drivers

        // todo add / remove trucks

        // todo add / remove waypoints
    }

    /**
     * Validates the input DTO object and throws NPE,
     * if object or one of required fields is null.
     *
     * @param dto DTO object.
     */
    private void validateRequiredFields(OrderDto dto) {
        if (dto.getOrderStatus() == null) {
            throw new NullPointerException();
        }
    }
}
