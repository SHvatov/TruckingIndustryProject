package com.ishvatov.service.inner.order;

import com.ishvatov.exception.DAOException;
import com.ishvatov.mapper.Mapper;
import com.ishvatov.model.dao.order.OrderDao;
import com.ishvatov.model.dto.OrderDto;
import com.ishvatov.model.entity.buisness.OrderEntity;
import com.ishvatov.model.entity.enum_types.OrderStatusType;
import com.ishvatov.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Basic {@link OrderService} interface implementation.
 *
 * @author Sergey Khvatov
 */
@Service("orderService")
public class OrderServiceImpl extends AbstractService<String, OrderEntity, OrderDto> implements OrderService {

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
     * @param orderDao  autowired {@link OrderDao} impl.
     */
    @Autowired
    public OrderServiceImpl(OrderDao orderDao, Mapper<OrderEntity, OrderDto> mapper) {
        super(orderDao, mapper);
        this.orderDao = orderDao;
    }

    /**
     * Updates the status of the order.
     *
     * @param status   new status.
     * @param orderUID UID of the order.
     */
    @Override
    public void updateOrderStatus(OrderStatusType status, String orderUID) {
        OrderEntity entity = orderDao.findByUniqueKey(orderUID);
        if (entity == null) {
            throw new DAOException(getClass(), "updateOrderStatus", "Entity with such UID does not exist");
        } else {
            entity.setOrderStatus(status);
        }
    }

    /**
     * Adds entity to the DB. Check if entity already exists.
     *
     * @param dtoObj new entity to add.
     * @throws DAOException if entity with this UID already exists
     */
    @Override
    public void save(OrderDto dtoObj) {
        if (orderDao.exists(dtoObj.getUniqueIdentificator())) {
            throw new DAOException(getClass(), "save", "Entity with such UID already exists");
        } else {
            // create new instance
            OrderEntity entity = new OrderEntity();

            // set UID
            entity.setUniqueIdentificator(dtoObj.getUniqueIdentificator());

            // set status
            entity.setOrderStatus(dtoObj.getOrderStatus());

            // save entity
            orderDao.save(entity);
        }
    }
}
