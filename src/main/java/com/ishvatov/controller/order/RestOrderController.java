package com.ishvatov.controller.order;

import com.ishvatov.controller.response.ServerResponse;
import com.ishvatov.controller.response.ServerResponseObject;
import com.ishvatov.model.dto.*;
import com.ishvatov.model.entity.enum_types.CargoStatusType;
import com.ishvatov.model.entity.enum_types.OrderStatusType;
import com.ishvatov.service.inner.cargo.CargoService;
import com.ishvatov.service.inner.driver.DriverService;
import com.ishvatov.service.inner.order.OrderService;
import com.ishvatov.service.inner.waypoint.WayPointService;
import com.ishvatov.validator.OrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Basic rest controller.
 *
 * @author Sergey Khvatov
 */
@Controller
@RequestMapping("employee/order")
public class RestOrderController {

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
     * Gets all the orders from the database.
     *
     * @return list of orders from database.
     */
    @GetMapping(value = "/list")
    public @ResponseBody List<OrderDto> loadAllOrders() {
        return orderService.findAll();
    }

    /**
     * Gets all the orders from the database.
     *
     * @return {@link ServerResponse} object.
     */
    @PostMapping(value = "/trucks")
    public @ResponseBody ServerResponseObject<List<TruckDto>> fetchTruckList(@RequestBody OrderWaypointDto orderDto) {
        ServerResponseObject<List<TruckDto>> response = new ServerResponseObject<>();
        if (orderValidator.validateBeforeTruckFetch(orderDto, response)) {
            response.setObject(orderService.findSuitableTrucks(orderDto.getWayPointDtoArray()));
        }
        return response;
    }

    /**
     * Gets all the orders from the database.
     *
     * @return {@link ServerResponse} object.
     */
    @PostMapping(value = "/drivers")
    public @ResponseBody ServerResponseObject<List<DriverDto>> fetchDriverList(@RequestBody OrderWaypointDto orderDto) {
        ServerResponseObject<List<DriverDto>> response = new ServerResponseObject<>();
        if (orderValidator.validateBeforeDriverFetch(orderDto, response)) {
            response.setObject(
                orderService.findSuitableDrivers(orderDto.getTruckUID(), orderDto.getWayPointDtoArray())
            );
        }
        return response;
    }

    /**
     * Gets all the orders from the database.
     *
     * @return {@link ServerResponse} object.
     */
    @PostMapping(value = "/create")
    public @ResponseBody ServerResponse createOrder(@RequestBody OrderWaypointDto orderDto) {
        ServerResponse response = new ServerResponse();
        if (orderValidator.validateBeforeSave(orderDto, response)) {
            OrderDto innerOrderDto = new OrderDto();

            // save order
            innerOrderDto.setUniqueIdentificator(orderDto.getUniqueIdentificator());
            innerOrderDto.setLastUpdated(new Timestamp(new Date().getTime()));
            innerOrderDto.setOrderStatus(OrderStatusType.READY);
            orderService.save(innerOrderDto);

            // update order
            innerOrderDto.setDriversUIDSet(orderDto.getDriversUIDSet());
            innerOrderDto.setTruckUID(orderDto.getTruckUID());
            orderService.update(innerOrderDto);

            // save waypoints
            for (WayPointDto wayPointDto : orderDto.getWayPointDtoArray()) {
                // update cargo status
                CargoDto cargoDto = cargoService.find(wayPointDto.getWaypointCargoUID());
                cargoDto.setCargoStatus(CargoStatusType.SHIPPING);
                cargoService.update(cargoDto);
                // update waypoints
                wayPointDto.setWaypointOrderUID(orderDto.getUniqueIdentificator());
                wayPointService.save(wayPointDto);
            }

            // update drivers
            for (String driverUID : orderDto.getDriversUIDSet()) {
                DriverDto driverDto = driverService.find(driverUID);
                driverDto.setDriverTruckUID(orderDto.getTruckUID());
                driverService.update(driverDto);
            }
        }
        return response;
    }
}
