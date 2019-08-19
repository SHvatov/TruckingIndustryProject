package com.ishvatov.controller.cargo;

import com.ishvatov.service.buisness.response.ServerResponse;
import com.ishvatov.service.buisness.response.ServerResponseObject;
import com.ishvatov.model.dto.CargoDto;
import com.ishvatov.service.buisness.cargo.BusinessCargoService;
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
@RequestMapping(value = "/employee/cargo")
@Log4j
public class RestCargoController {

    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private BusinessCargoService cargoService;

    /**
     * Gets all the cargo from the database.
     *
     * @return list of cargo from database.
     */
    @GetMapping(value = "/list")
    @ResponseBody
    public List<CargoDto> loadAllCargo() {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return cargoService.loadAllCargo();
    }

    /**
     * Gets all not delivered cargo from the database.
     *
     * @return list of cargo from database.
     */
    @GetMapping(value = "/list_id")
    @ResponseBody
    public List<Integer> listAllCargoId() {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return cargoService.listAllCargoId();
    }

    /**
     * Fetches data about cargo from the database.
     *
     * @return {@link ServerResponseObject} object.
     */
    @GetMapping(value = "/{uid}/load")
    @ResponseBody
    public ServerResponseObject<CargoDto> loadCargo(@PathVariable(name = "uid") Integer cargoId) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return cargoService.loadCargo(cargoId);
    }


    /**
     * Gets all the cargo from the database.
     *
     * @return {@link ServerResponse} object.
     */
    @PostMapping(value = "/{uid}/delete")
    @ResponseBody
    public ServerResponse deleteCargo(@PathVariable(name = "uid") Integer cargoId) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return cargoService.deleteCargo(cargoId);
    }

    /**
     * Gets all the cargo from the database.
     *
     * @return {@link ServerResponse} object.
     */
    @PostMapping(value = "/create")
    @ResponseBody
    public ServerResponse addCargo(@RequestBody CargoDto cargoDto) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return cargoService.addCargo(cargoDto);
    }

    /**
     * Updates the name of the cargo.
     *
     * @return {@link ServerResponseObject} object.
     */
    @PostMapping(value = "/{uid}/update_name")
    @ResponseBody
    public ServerResponse updateCargoName(@RequestBody CargoDto cargoDto,
                                          @PathVariable(name = "uid") Integer cargoId) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return cargoService.updateCargoName(cargoDto);
    }

    /**
     * Updates the surname of the cargo.
     *
     * @return {@link ServerResponseObject} object.
     */
    @PostMapping(value = "/{uid}/update_mass")
    @ResponseBody
    public ServerResponse updateCargoSurname(@RequestBody CargoDto cargoDto,
                                             @PathVariable(name = "uid") Integer cargoId) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return cargoService.updateCargoMass(cargoDto);
    }
}
