package com.ishvatov.service.inner.order;

import com.ishvatov.model.dto.OrderDto;
import com.ishvatov.model.entity.enum_types.OrderStatusType;
import com.ishvatov.service.BaseService;

/**
 * Defines a basic interface to interact with
 * order DAO layer.
 *
 * @author Sergey Khvatov
 */
public interface OrderService extends BaseService<String, OrderDto> {

    /**
     * Updates the status of the order.
     *
     * @param status   new status.
     * @param orderUID UID of the order.
     */
    void updateOrderStatus(OrderStatusType status, String orderUID);

}
