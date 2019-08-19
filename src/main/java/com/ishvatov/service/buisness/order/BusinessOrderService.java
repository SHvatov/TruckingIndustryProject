package com.ishvatov.service.buisness.order;

import com.ishvatov.model.dto.DriverDto;
import com.ishvatov.model.dto.OrderDto;
import com.ishvatov.model.dto.OrderWaypointDto;
import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.service.buisness.response.ServerResponse;
import com.ishvatov.service.buisness.response.ServerResponseObject;

import java.util.List;

/**
 * Defines basic methods, that are used in the business
 * logic.
 *
 * @author Sergey Khvatov
 */
public interface BusinessOrderService {

    /**
     * Finds all the orders in the system.
     *
     * @return list of orders.
     */
    List<OrderDto> loadAllOrders();

    /**
     * Fetches the list of trucks, that are suitable for this order.
     *
     * @param orderDto dto object.
     * @return list of trucks, that are suitable for this order, or error message.
     */
    ServerResponseObject<List<TruckDto>> fetchTruckList(OrderWaypointDto orderDto);

    /**
     * Fetches the list of drivers, that are suitable for this order.
     *
     * @param orderDto dto object.
     * @return list of trucks, that are suitable for this order, or error message.
     */
    ServerResponseObject<List<DriverDto>> fetchDriverList(OrderWaypointDto orderDto);

    /**
     * Creates the order.
     *
     * @param orderDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    ServerResponse createOrder(OrderWaypointDto orderDto);
}
