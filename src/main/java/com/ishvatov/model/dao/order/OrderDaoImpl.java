package com.ishvatov.model.dao.order;

import com.ishvatov.model.dao.AbstractDao;
import com.ishvatov.model.entity.buisness.OrderEntity;
import org.springframework.stereotype.Repository;

/**
 * {@link OrderDao} implementation.
 *
 * @author Sergey Khvatov
 */
@Repository("orderDao")
public class OrderDaoImpl extends AbstractDao<String, OrderEntity> implements OrderDao {
    // empty
}
