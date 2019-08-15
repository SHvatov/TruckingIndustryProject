package com.ishvatov.controller.driver;

import com.ishvatov.controller.response.ServerResponse;
import com.ishvatov.controller.response.ServerResponseObject;
import com.ishvatov.model.dto.DriverDto;
import com.ishvatov.model.dto.UserDto;
import com.ishvatov.model.entity.enum_types.UserRoleType;
import com.ishvatov.service.inner.driver.DriverService;
import com.ishvatov.service.inner.user.UserService;
import com.ishvatov.validator.CustomValidator;
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
public class RestDriverController {

    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private DriverService driverService;

    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private UserService userService;

    /**
     * Autowired validator.
     */
    @Autowired
    private CustomValidator<DriverDto, String> driverValidator;

    /**
     * Gets all the drivers from the database.
     *
     * @return list of drivers from database.
     */
    @GetMapping(value = "/list")
    public @ResponseBody
    List<DriverDto> loadAllDrivers() {
        return driverService.findAll();
    }

    /**
     * Fetches data about driver from the database.
     *
     * @return {@link ServerResponseObject} object.
     */
    @GetMapping(value = "/{uid}/load")
    public @ResponseBody
    ServerResponseObject<DriverDto> loadDriver(@PathVariable(name = "uid") String driverUID) {
        ServerResponseObject<DriverDto> responseObject = new ServerResponseObject<>();
        if (driverValidator.validateBeforeLoad(driverUID, responseObject)) {
            responseObject.setObject(driverService.find(driverUID));
        }
        return responseObject;
    }


    /**
     * Gets all the drivers from the database.
     *
     * @return {@link ServerResponse} object.
     */
    @PostMapping(value = "/{uid}/delete")
    public @ResponseBody
    ServerResponse deleteDriver(@PathVariable(name = "uid") String driverUID) {
        ServerResponse response = new ServerResponse();
        if (driverValidator.validateBeforeDelete(driverUID, response)) {
            driverService.delete(driverUID);
        }
        return response;
    }

    /**
     * Gets all the drivers from the database.
     *
     * @return {@link ServerResponse} object.
     */
    @PostMapping(value = "/create")
    public @ResponseBody
    ServerResponse addDriver(@RequestBody DriverDto driverDto) {
        ServerResponse response = new ServerResponse();
        if (driverValidator.validateBeforeSave(driverDto, response)) {
            // create new truck
            driverService.save(driverDto);

            // create new user
            UserDto userDto = new UserDto(driverDto.getUniqueIdentificator(), driverDto.getDriverPassword(), UserRoleType.ROLE_DRIVER);
            userService.save(userDto);
        }
        return response;
    }

    /**
     * Updates the name of the driver.
     *
     * @return {@link ServerResponseObject} object.
     */
    @PostMapping(value = "/{uid}/update_name")
    public @ResponseBody ServerResponse updateDriverName(@RequestBody DriverDto driverDto, @PathVariable(name = "uid") String truckUID) {
        ServerResponse responseObject = new ServerResponseObject<>();
        if (!driverValidator.validateBeforeLoad(driverDto.getUniqueIdentificator(), responseObject)) {
            return responseObject;
        } else {
            DriverDto innerDriverDto = driverService.find(driverDto.getUniqueIdentificator());
            innerDriverDto.setDriverName(driverDto.getDriverName());
            if (driverValidator.validateBeforeUpdate(innerDriverDto, responseObject)) {
                driverService.update(innerDriverDto);
            }
        }
        return responseObject;
    }

    /**
     * Updates the surname of the driver.
     *
     * @return {@link ServerResponseObject} object.
     */
    @PostMapping(value = "/{uid}/update_surname")
    public @ResponseBody ServerResponse updateDriverSurname(@RequestBody DriverDto driverDto, @PathVariable(name = "uid") String truckUID) {
        ServerResponse responseObject = new ServerResponseObject<>();
        if (!driverValidator.validateBeforeLoad(driverDto.getUniqueIdentificator(), responseObject)) {
            return responseObject;
        } else {
            DriverDto innerDriverDto = driverService.find(driverDto.getUniqueIdentificator());
            innerDriverDto.setDriverSurname(driverDto.getDriverSurname());
            if (driverValidator.validateBeforeUpdate(innerDriverDto, responseObject)) {
                driverService.update(innerDriverDto);
            }
        }
        return responseObject;
    }

    /**
     * Updates the surname of the driver.
     *
     * @return {@link ServerResponseObject} object.
     */
    @PostMapping(value = "/{uid}/update_city")
    public @ResponseBody ServerResponse updateDriverCity(@RequestBody DriverDto driverDto, @PathVariable(name = "uid") String truckUID) {
        ServerResponse responseObject = new ServerResponseObject<>();
        if (!driverValidator.validateBeforeLoad(driverDto.getUniqueIdentificator(), responseObject)) {
            return responseObject;
        } else {
            DriverDto innerDriverDto = driverService.find(driverDto.getUniqueIdentificator());
            innerDriverDto.setCurrentCityUID(driverDto.getCurrentCityUID());
            if (driverValidator.validateBeforeUpdate(innerDriverDto, responseObject)) {
                driverService.update(innerDriverDto);
            }
        }
        return responseObject;
    }
}
