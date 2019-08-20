package com.ishvatov.controller.driver;

import com.ishvatov.model.dto.DriverDto;
import com.ishvatov.service.buisness.driver.BusinessDriverService;
import com.ishvatov.service.buisness.response.ServerResponse;
import com.ishvatov.service.buisness.response.ServerResponseObject;
import lombok.extern.log4j.Log4j;
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
@RequestMapping(value = "/employee/driver")
@Log4j
public class RestDriverController {

    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private BusinessDriverService driverService;

    /**
     * Gets all the drivers from the database.
     *
     * @return list of drivers from database.
     */
    @GetMapping(value = "/list")
    @ResponseBody
    public List<DriverDto> loadAllDrivers() {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return driverService.loadAllDrivers();
    }

    /**
     * Fetches data about driver from the database.
     *
     * @return {@link ServerResponseObject} object.
     */
    @GetMapping(value = "/{uid}/load")
    @ResponseBody
    public ServerResponseObject<DriverDto> loadDriver(@PathVariable(name = "uid") String driverId) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return driverService.loadDriver(driverId);
    }


    /**
     * Gets all the drivers from the database.
     *
     * @return {@link ServerResponse} object.
     */
    @ResponseBody
    @PostMapping(value = "/{uid}/delete")
    public ServerResponse deleteDriver(@PathVariable(name = "uid") String driverId) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return driverService.deleteDriver(driverId);
    }

    /**
     * Gets all the drivers from the database.
     *
     * @return {@link ServerResponse} object.
     */
    @ResponseBody
    @PostMapping(value = "/create")
    public ServerResponse addDriver(@RequestBody DriverDto driverDto) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return driverService.addDriver(driverDto);
    }

    /**
     * Updates the name of the driver.
     *
     * @return {@link ServerResponseObject} object.
     */
    @ResponseBody
    @PostMapping(value = "/{uid}/update_name")
    public ServerResponse updateDriverName(@RequestBody DriverDto driverDto,
                                           @PathVariable(name = "uid") String driverId) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return driverService.updateDriverName(driverDto);
    }

    /**
     * Updates the surname of the driver.
     *
     * @return {@link ServerResponseObject} object.
     */
    @ResponseBody
    @PostMapping(value = "/{uid}/update_surname")
    public ServerResponse updateDriverSurname(@RequestBody DriverDto driverDto,
                                              @PathVariable(name = "uid") String driverId) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return driverService.updateDriverSurname(driverDto);
    }

    /**
     * Updates the surname of the driver.
     *
     * @return {@link ServerResponseObject} object.
     */
    @ResponseBody
    @PostMapping(value = "/{uid}/update_city")
    public ServerResponse updateDriverCity(@RequestBody DriverDto driverDto,
                                           @PathVariable(name = "uid") String driverId) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());
        
        return driverService.updateDriverCity(driverDto);
    }
}
