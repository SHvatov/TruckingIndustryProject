package com.ishvatov.service.buisness.driver;

import com.ishvatov.service.buisness.response.ServerResponse;
import com.ishvatov.service.buisness.response.ServerResponseObject;
import com.ishvatov.model.dto.DriverDto;
import com.ishvatov.model.dto.UserDto;
import com.ishvatov.model.entity.enum_types.UserRoleType;
import com.ishvatov.service.inner.driver.DriverService;
import com.ishvatov.service.inner.user.UserService;
import com.ishvatov.service.buisness.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * {@link BusinessDriverService} implementation.
 *
 * @author Sergey Khvatov
 */
@Service
public class BusinessDriverServiceImpl implements BusinessDriverService {

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
    @Override
    public List<DriverDto> loadAllDrivers() {
        return driverService.findAll();
    }

    /**
     * Load driver from the database.
     *
     * @param driverId UID of the driver.
     * @return requested driver.
     */
    @Override
    public ServerResponseObject<DriverDto> loadDriver(String driverId) {
        ServerResponseObject<DriverDto> responseObject = new ServerResponseObject<>();
        if (driverValidator.validateBeforeLoad(driverId, responseObject)) {
            responseObject.setObject(driverService.find(driverId));
        }
        return responseObject;
    }

    /**
     * Delete driver from the database.
     *
     * @param driverId UID of the driver.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    @Override
    public ServerResponse deleteDriver(String driverId) {
        ServerResponse response = new ServerResponse();
        if (driverValidator.validateBeforeDelete(driverId, response)) {
            driverService.delete(driverId);
        }
        return response;
    }

    /**
     * Add driver to the database.
     *
     * @param driverDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    @Override
    public ServerResponse addDriver(DriverDto driverDto) {
        ServerResponse response = new ServerResponse();
        if (driverValidator.validateBeforeSave(driverDto, response)) {
            // create new truck
            driverService.save(driverDto);

            // create new user
            UserDto userDto = new UserDto(driverDto.getUniqueIdentificator(),
                driverDto.getPassword(),
                UserRoleType.ROLE_DRIVER);
            userService.save(userDto);
        }
        return response;
    }

    /**
     * Updates driver's name.
     *
     * @param driverDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    @Override
    public ServerResponse updateDriverName(DriverDto driverDto) {
        ServerResponse responseObject = new ServerResponseObject<>();
        if (!driverValidator.validateBeforeLoad(driverDto.getUniqueIdentificator(), responseObject)) {
            return responseObject;
        } else {
            DriverDto innerDriverDto = driverService.find(driverDto.getUniqueIdentificator());
            innerDriverDto.setName(driverDto.getName());
            if (driverValidator.validateBeforeUpdate(innerDriverDto, responseObject)) {
                driverService.update(innerDriverDto);
            }
        }
        return responseObject;
    }

    /**
     * Updates driver's surname.
     *
     * @param driverDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    @Override
    public ServerResponse updateDriverSurname(DriverDto driverDto) {
        ServerResponse responseObject = new ServerResponseObject<>();
        if (!driverValidator.validateBeforeLoad(driverDto.getUniqueIdentificator(), responseObject)) {
            return responseObject;
        } else {
            DriverDto innerDriverDto = driverService.find(driverDto.getUniqueIdentificator());
            innerDriverDto.setSurname(driverDto.getSurname());
            if (driverValidator.validateBeforeUpdate(innerDriverDto, responseObject)) {
                driverService.update(innerDriverDto);
            }
        }
        return responseObject;
    }

    /**
     * Updates driver's city.
     *
     * @param driverDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    @Override
    public ServerResponse updateDriverCity(DriverDto driverDto) {
        ServerResponse responseObject = new ServerResponseObject<>();
        if (!driverValidator.validateBeforeLoad(driverDto.getUniqueIdentificator(), responseObject)) {
            return responseObject;
        } else {
            DriverDto innerDriverDto = driverService.find(driverDto.getUniqueIdentificator());
            innerDriverDto.setCityId(driverDto.getCityId());
            if (driverValidator.validateBeforeUpdate(innerDriverDto, responseObject)) {
                driverService.update(innerDriverDto);
            }
        }
        return responseObject;
    }
}
