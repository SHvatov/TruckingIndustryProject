package com.ishvatov.controller.order;

import com.ishvatov.model.dto.DriverDto;
import com.ishvatov.model.dto.OrderDto;
import com.ishvatov.model.dto.OrderWaypointDto;
import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.service.buisness.order.BusinessOrderService;
import com.ishvatov.service.buisness.response.ServerResponse;
import com.ishvatov.service.buisness.response.ServerResponseObject;
import lombok.extern.log4j.Log4j;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Basic rest controller.
 *
 * @author Sergey Khvatov
 */
@Controller
@RequestMapping("employee/order")
@Log4j
public class RestOrderController {

    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private BusinessOrderService orderService;

    /**
     * Gets all the orders from the database.
     *
     * @return list of orders from database.
     */
    @GetMapping(value = "/list")
    public @ResponseBody List<OrderDto> loadAllOrders() {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return orderService.loadAllOrders();
    }

    /**
     * Gets all the orders from the database.
     *
     * @return {@link ServerResponse} object.
     */
    @PostMapping(value = "/trucks")
    public @ResponseBody ServerResponseObject<List<TruckDto>> fetchTruckList(@RequestBody OrderWaypointDto orderDto) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return orderService.fetchTruckList(orderDto);
    }

    /**
     * Gets all the orders from the database.
     *
     * @return {@link ServerResponse} object.
     */
    @PostMapping(value = "/drivers")
    public @ResponseBody ServerResponseObject<List<DriverDto>> fetchDriverList(@RequestBody OrderWaypointDto orderDto) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return orderService.fetchDriverList(orderDto);
    }

    /**
     * Gets all the orders from the database.
     *
     * @return {@link ServerResponse} object.
     */
    @PostMapping(value = "/create")
    public @ResponseBody ServerResponse createOrder(@RequestBody OrderWaypointDto orderDto) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return orderService.createOrder(orderDto);
    }
}
