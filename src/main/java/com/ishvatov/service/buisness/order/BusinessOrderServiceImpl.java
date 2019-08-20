package com.ishvatov.service.buisness.order;

import com.ishvatov.model.dto.*;
import com.ishvatov.model.entity.enum_types.CargoStatusType;
import com.ishvatov.model.entity.enum_types.OrderStatusType;
import com.ishvatov.service.buisness.response.ServerResponse;
import com.ishvatov.service.buisness.response.ServerResponseObject;
import com.ishvatov.service.inner.cargo.CargoService;
import com.ishvatov.service.inner.driver.DriverService;
import com.ishvatov.service.inner.order.OrderService;
import com.ishvatov.service.inner.waypoint.WayPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * {@link BusinessOrderService} implementation.
 *
 * @author Sergey Khvatov
 */
@Service
public class BusinessOrderServiceImpl implements BusinessOrderService {

    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private OrderService orderService;

    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private DriverService driverService;

    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private CargoService cargoService;

    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private WayPointService wayPointService;

    /**
     * Autowired validator.
     */
    @Autowired
    private OrderValidator orderValidator;

    /**
     * Finds all the orders in the system.
     *
     * @return list of orders.
     */
    @Override
    public List<OrderDto> loadAllOrders() {
        return orderService.findAll();
    }

    /**
     * Fetches the list of trucks, that are suitable for this order.
     *
     * @param orderDto dto object.
     * @return list of trucks, that are suitable for this order, or error message.
     */
    @Override
    public ServerResponseObject<List<TruckDto>> fetchTruckList(OrderWaypointDto orderDto) {
        ServerResponseObject<List<TruckDto>> response = new ServerResponseObject<>();
        if (orderValidator.validateBeforeTruckFetch(orderDto, response)) {
            response.setObject(orderService.findSuitableTrucks(orderDto.getWaypoints()));
        }
        return response;
    }

    /**
     * Fetches the list of drivers, that are suitable for this order.
     *
     * @param orderDto dto object.
     * @return list of trucks, that are suitable for this order, or error message.
     */
    @Override
    public ServerResponseObject<List<DriverDto>> fetchDriverList(OrderWaypointDto orderDto) {
        ServerResponseObject<List<DriverDto>> response = new ServerResponseObject<>();
        if (orderValidator.validateBeforeDriverFetch(orderDto, response)) {
            response.setObject(orderService.findSuitableDrivers(orderDto.getTruckId(), orderDto.getWaypoints()));
        }
        return response;
    }

    /**
     * Creates the order.
     *
     * @param orderDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    @Override
    public ServerResponse createOrder(OrderWaypointDto orderDto) {
        ServerResponse response = new ServerResponse();
        if (orderValidator.validateBeforeSave(orderDto, response)) {
            OrderDto innerOrderDto = new OrderDto();

            // save order
            innerOrderDto.setUniqueIdentificator(orderDto.getUniqueIdentificator());
            innerOrderDto.setLastUpdated(new Timestamp(new Date().getTime()));
            innerOrderDto.setStatus(OrderStatusType.READY);
            orderService.save(innerOrderDto);

            // update order
            innerOrderDto.setAssignedDrivers(orderDto.getAssignedDrivers());
            innerOrderDto.setTruckId(orderDto.getTruckId());
            orderService.update(innerOrderDto);

            // save waypoints
            for (WayPointDto wayPointDto : orderDto.getWaypoints()) {
                // update cargo status
                CargoDto cargoDto = cargoService.find(wayPointDto.getCargoId());
                cargoDto.setStatus(CargoStatusType.SHIPPING);
                cargoService.update(cargoDto);

                // update waypoints
                wayPointDto.setOrderId(orderDto.getUniqueIdentificator());
                wayPointService.save(wayPointDto);
            }

            // update drivers
            for (String driverUID : orderDto.getAssignedDrivers()) {
                DriverDto driverDto = driverService.find(driverUID);
                driverDto.setTruckId(orderDto.getTruckId());
                driverService.update(driverDto);
            }
        }
        return response;
    }
}
