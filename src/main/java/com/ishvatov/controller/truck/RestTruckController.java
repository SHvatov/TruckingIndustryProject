package com.ishvatov.controller.truck;

import com.ishvatov.controller.response.ServerResponseObject;
import com.ishvatov.controller.response.ServerResponse;
import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.service.inner.truck.TruckService;
import com.ishvatov.validator.truck.TruckValidator;
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
public class RestTruckController {

    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private TruckService truckService;

    /**
     * Autowired validator.
     */
    @Autowired
    private TruckValidator truckValidator;

    /**
     * Gets all the trucks from the database.
     *
     * @return list of trucks from database.
     */
    @GetMapping(value = "/list")
    public @ResponseBody List<TruckDto> loadAllTrucks() {
        return truckService.findAll();
    }

    /**
     * Fetches data about truck from the database.
     *
     * @return {@link ServerResponseObject} object.
     */
    @GetMapping(value = "/{uid}/load")
    public @ResponseBody ServerResponseObject<TruckDto> loadTruck(@PathVariable(name = "uid") String truckUID) {
        ServerResponseObject<TruckDto> responseObject = new ServerResponseObject<>();
        if (truckValidator.validateBeforeLoad(truckUID, responseObject)) {
            responseObject.setObject(truckService.find(truckUID));
        }
        return responseObject;
    }

    /**
     * Update the capacity of the truck.
     *
     * @return {@link ServerResponseObject} object.
     */
    @PostMapping(value = "/{uid}/update_capacity")
    public @ResponseBody ServerResponse updateTruckCapacity(@RequestBody TruckDto truckDto,
                                                            @PathVariable(name = "uid") String truckUID) {
        ServerResponse responseObject = new ServerResponseObject<>();
        if (!truckValidator.validateBeforeLoad(truckDto.getUniqueIdentificator(), responseObject)) {
            return responseObject;
        } else {
            TruckDto innerTruckDto = truckService.find(truckDto.getUniqueIdentificator());
            innerTruckDto.setTruckCapacity(truckDto.getTruckCapacity());
            if (truckValidator.validateBeforeUpdate(innerTruckDto, responseObject)) {
                truckService.update(innerTruckDto);
            }
        }
        return responseObject;
    }

    /**
     * Updates the shift size of the truck.
     *
     * @return {@link ServerResponseObject} object.
     */
    @PostMapping(value = "/{uid}/update_shift")
    public @ResponseBody ServerResponse updateTruckShiftSize(@RequestBody TruckDto truckDto,
                                                             @PathVariable(name = "uid") String truckUID) {
        ServerResponse responseObject = new ServerResponseObject<>();
        if (!truckValidator.validateBeforeLoad(truckDto.getUniqueIdentificator(), responseObject)) {
            return responseObject;
        } else {
            TruckDto innerTruckDto = truckService.find(truckDto.getUniqueIdentificator());
            innerTruckDto.setTruckDriverShiftSize(truckDto.getTruckDriverShiftSize());
            if (truckValidator.validateBeforeUpdate(innerTruckDto, responseObject)) {
                truckService.update(innerTruckDto);
            }
        }
        return responseObject;
    }

    /**
     * Gets all the trucks from the database.
     *
     * @return {@link ServerResponse} object.
     */
    @PostMapping(value = "/{uid}/delete")
    public @ResponseBody ServerResponse deleteTruck(@PathVariable(name = "uid") String truckUID) {
        ServerResponse response = new ServerResponse();
        if (truckValidator.validateBeforeDelete(truckUID, response)) {
            truckService.delete(truckUID);
        }
        return response;
    }

    /**
     * Gets all the trucks from the database.
     *
     * @return {@link ServerResponse} object.
     */
    @PostMapping(value = "/create")
    public @ResponseBody ServerResponse addTruck(@RequestBody TruckDto truckDto) {
        ServerResponse response = new ServerResponse();
        if (truckValidator.validateBeforeSave(truckDto, response)) {
            truckService.save(truckDto);
        }
        return response;
    }
}
