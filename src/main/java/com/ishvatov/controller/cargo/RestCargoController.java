package com.ishvatov.controller.cargo;

import com.ishvatov.controller.response.ServerResponse;
import com.ishvatov.controller.response.ServerResponseObject;
import com.ishvatov.model.dto.CargoDto;
import com.ishvatov.model.entity.enum_types.CargoStatusType;
import com.ishvatov.service.inner.cargo.CargoService;
import com.ishvatov.validator.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Basic rest controller.
 *
 * @author Sergey Khvatov
 */
@Controller
@RequestMapping(value = "/employee/cargo")
public class RestCargoController {

    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private CargoService cargoService;

    /**
     * Autowired validator.
     */
    @Autowired
    private CustomValidator<CargoDto, Integer> cargoValidator;

    /**
     * Gets all the cargo from the database.
     *
     * @return list of cargo from database.
     */
    @GetMapping(value = "/list")
    public @ResponseBody List<CargoDto> loadAllCargo() {
        return cargoService.findAll();
    }

    /**
     * Gets all not delivered cargo from the database.
     *
     * @return list of cargo from database.
     */
    @GetMapping(value = "/list_id")
    public @ResponseBody List<Integer> listAllCargoId() {
        return cargoService.findAll()
            .stream()
            .filter(Objects::nonNull)
            .filter(e -> e.getCargoStatus() == CargoStatusType.READY)
            .map(CargoDto::getId)
            .collect(Collectors.toList());
    }

    /**
     * Fetches data about cargo from the database.
     *
     * @return {@link ServerResponseObject} object.
     */
    @GetMapping(value = "/{uid}/load")
    public @ResponseBody
    ServerResponseObject<CargoDto> loadCargo(@PathVariable(name = "uid") Integer cargoUID) {
        ServerResponseObject<CargoDto> responseObject = new ServerResponseObject<>();
        if (cargoValidator.validateBeforeLoad(cargoUID, responseObject)) {
            responseObject.setObject(cargoService.find(cargoUID));
        }
        return responseObject;
    }


    /**
     * Gets all the cargo from the database.
     *
     * @return {@link ServerResponse} object.
     */
    @PostMapping(value = "/{uid}/delete")
    public @ResponseBody
    ServerResponse deleteCargo(@PathVariable(name = "uid") Integer cargoUID) {
        ServerResponse response = new ServerResponse();
        if (cargoValidator.validateBeforeDelete(cargoUID, response)) {
            cargoService.delete(cargoUID);
        }
        return response;
    }

    /**
     * Gets all the cargo from the database.
     *
     * @return {@link ServerResponse} object.
     */
    @PostMapping(value = "/create")
    public @ResponseBody
    ServerResponse addCargo(@RequestBody CargoDto cargoDto) {
        ServerResponse response = new ServerResponse();
        if (cargoValidator.validateBeforeSave(cargoDto, response)) {
            cargoService.save(cargoDto);
        }
        return response;
    }

    /**
     * Updates the name of the cargo.
     *
     * @return {@link ServerResponseObject} object.
     */
    @PostMapping(value = "/{uid}/update_name")
    public @ResponseBody ServerResponse updateCargoName(@RequestBody CargoDto cargoDto, @PathVariable(name = "uid") Integer truckUID) {
        ServerResponse responseObject = new ServerResponseObject<>();
        if (!cargoValidator.validateBeforeLoad(cargoDto.getUniqueIdentificator(), responseObject)) {
            return responseObject;
        } else {
            CargoDto innerCargoDto = cargoService.find(cargoDto.getUniqueIdentificator());
            innerCargoDto.setCargoName(cargoDto.getCargoName());
            if (cargoValidator.validateBeforeUpdate(innerCargoDto, responseObject)) {
                cargoService.update(innerCargoDto);
            }
        }
        return responseObject;
    }

    /**
     * Updates the surname of the cargo.
     *
     * @return {@link ServerResponseObject} object.
     */
    @PostMapping(value = "/{uid}/update_mass")
    public @ResponseBody ServerResponse updateCargoSurname(@RequestBody CargoDto cargoDto, @PathVariable(name = "uid") Integer truckUID) {
        ServerResponse responseObject = new ServerResponseObject<>();
        if (!cargoValidator.validateBeforeLoad(cargoDto.getUniqueIdentificator(), responseObject)) {
            return responseObject;
        } else {
            CargoDto innerCargoDto = cargoService.find(cargoDto.getUniqueIdentificator());
            innerCargoDto.setCargoMass(cargoDto.getCargoMass());
            if (cargoValidator.validateBeforeUpdate(innerCargoDto, responseObject)) {
                cargoService.update(innerCargoDto);
            }
        }
        return responseObject;
    }
}
