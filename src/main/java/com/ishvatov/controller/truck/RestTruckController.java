package com.ishvatov.controller.truck;

import com.ishvatov.service.buisness.response.ServerResponse;
import com.ishvatov.service.buisness.response.ServerResponseObject;
import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.service.buisness.truck.BusinessTruckService;
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
@RequestMapping(value = "/employee/truck")
@Log4j
public class RestTruckController {

    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private BusinessTruckService truckService;

    /**
     * Gets all the trucks from the database.
     *
     * @return list of trucks from database.
     */
    @GetMapping(value = "/list")
    @ResponseBody
    public List<TruckDto> loadAllTrucks() {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return truckService.loadAllTrucks();
    }

    /**
     * Fetches data about truck from the database.
     *
     * @return {@link ServerResponseObject} object.
     */
    @GetMapping(value = "/{uid}/load")
    @ResponseBody
    public ServerResponseObject<TruckDto> loadTruck(@PathVariable(name = "uid") String truckId) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return truckService.loadTruck(truckId);
    }

    /**
     * Gets all the trucks from the database.
     *
     * @return {@link ServerResponse} object.
     */
    @PostMapping(value = "/{uid}/delete")
    @ResponseBody
    public ServerResponse deleteTruck(@PathVariable(name = "uid") String truckId) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return truckService.deleteTruck(truckId);
    }

    /**
     * Gets all the trucks from the database.
     *
     * @return {@link ServerResponse} object.
     */
    @PostMapping(value = "/create")
    @ResponseBody
    public ServerResponse addTruck(@RequestBody TruckDto truckDto) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return truckService.addTruck(truckDto);
    }

    /**
     * Update the capacity of the truck.
     *
     * @return {@link ServerResponseObject} object.
     */
    @ResponseBody
    @PostMapping(value = "/{uid}/update_capacity")
    public ServerResponse updateTruckCapacity(@RequestBody TruckDto truckDto,
                                              @PathVariable(name = "uid") String truckId) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return truckService.updateTruckCapacity(truckDto);
    }

    /**
     * Updates the shift size of the truck.
     *
     * @return {@link ServerResponseObject} object.
     */
    @ResponseBody
    @PostMapping(value = "/{uid}/update_shift")
    public ServerResponse updateTruckShiftSize(@RequestBody TruckDto truckDto,
                                               @PathVariable(name = "uid") String truckId) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return truckService.updateTruckShiftSize(truckDto);
    }

    /**
     * Updates the condition status of the truck.
     *
     * @return {@link ServerResponseObject} object.
     */
    @ResponseBody
    @PostMapping(value = "/{uid}/update_condition")
    public ServerResponse updateTruckCondition(@RequestBody TruckDto truckDto,
                                               @PathVariable(name = "uid") String truckId) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return truckService.updateTruckStatus(truckDto);
    }
}
