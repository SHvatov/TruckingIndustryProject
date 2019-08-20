package com.ishvatov.controller.driver;

import com.ishvatov.model.dto.DriverInfoDto;
import com.ishvatov.model.entity.enum_types.DriverStatusType;
import com.ishvatov.service.buisness.driver_user.BusinessDriverUserService;
import com.ishvatov.service.buisness.response.ServerResponse;
import com.ishvatov.service.buisness.response.ServerResponseObject;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * This controller is designed to process the requests made by the admin.
 *
 * @author Sergey Khvatov
 */
@RestController
@RequestMapping("/driver")
@Log4j
public class DriverUserController {

    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private BusinessDriverUserService orderService;

    /**
     * Loads info about driver.
     *
     * @param uid Id of the driver.
     * @return driver data.
     */
    @GetMapping("/{uid}/load")
    @ResponseBody
    public ServerResponseObject<DriverInfoDto> loadDriverInformation(@PathVariable(name = "uid") String uid) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return orderService.loadDriverInformation(uid);
    }

    /**
     * Complete waypoint request.
     *
     * @param wayPointId Id of the waypoint.
     * @param uid Id of the driver.
     * @return ServerResponse object.
     */
    @PostMapping("/{driverId}/waypoint/{wayPointId}")
    @ResponseBody
    public ServerResponse completeWaypoint(@PathVariable(name = "wayPointId") Integer wayPointId,
                                           @PathVariable(name = "driverId") String uid) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return orderService.completeWaypoint(wayPointId, uid);
    }

    /**
     * Update driver's session status method.
     *
     * @param uid Id of the driver.
     * @return ServerResponse object.
     */
    @GetMapping("/{uid}/session")
    @ResponseBody
    public ServerResponse changeSession(@PathVariable(name = "uid") String uid) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return orderService.changeSession(uid);
    }

    /**
     * Update driver's session status method.
     *
     * @param uid Id of the driver.
     * @return ServerResponse object.
     */
    @PostMapping("/{uid}/status/{status}")
    @ResponseBody
    public ServerResponse changeStatus(@PathVariable(name = "status") DriverStatusType status,
                                        @PathVariable(name = "uid") String uid) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

       return orderService.changeStatus(status, uid);
    }

    /**
     * Update driver's session status method.
     *
     * @param uid Id of the driver.
     * @return ServerResponse object.
     */
    @GetMapping("/{uid}/complete")
    @ResponseBody
    public ServerResponse completeOrder(@PathVariable(name = "uid") String uid) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return orderService.completeOrder(uid);
    }

    /**
     * Update driver's session status method.
     *
     * @param uid Id of the driver.
     * @return ServerResponse object.
     */
    @GetMapping("/{uid}/start")
    @ResponseBody
    public ServerResponse startOrder(@PathVariable(name = "uid") String uid) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return orderService.startOrder(uid);
    }
}
