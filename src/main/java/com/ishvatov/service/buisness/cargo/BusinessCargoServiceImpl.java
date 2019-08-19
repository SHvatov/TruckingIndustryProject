package com.ishvatov.service.buisness.cargo;

import com.ishvatov.service.buisness.response.ServerResponse;
import com.ishvatov.service.buisness.response.ServerResponseObject;
import com.ishvatov.model.dto.CargoDto;
import com.ishvatov.model.entity.enum_types.CargoStatusType;
import com.ishvatov.service.inner.cargo.CargoService;
import com.ishvatov.service.buisness.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@link BusinessCargoService} implementation.
 *
 * @author Sergey Khvatov
 */
@Service
public class BusinessCargoServiceImpl implements BusinessCargoService {

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
    @Override
    public List<CargoDto> loadAllCargo() {
        return cargoService.findAll();
    }

    /**
     * Gets all not delivered cargo from the database.
     *
     * @return list of cargo from database.
     */
    @Override
    public List<Integer> listAllCargoId() {
        return cargoService.findAll()
            .stream()
            .filter(Objects::nonNull)
            .filter(e -> e.getStatus() == CargoStatusType.READY)
            .map(CargoDto::getId)
            .collect(Collectors.toList());
    }

    /**
     * Fetches data about cargo from the database.
     *
     * @param cargoId UID of the cargo.
     * @return requested truck object or error.
     */
    @Override
    public ServerResponseObject<CargoDto> loadCargo(Integer cargoId) {
        ServerResponseObject<CargoDto> responseObject = new ServerResponseObject<>();
        if (cargoValidator.validateBeforeLoad(cargoId, responseObject)) {
            responseObject.setObject(cargoService.find(cargoId));
        }
        return responseObject;
    }

    /**
     * Deletes cargo from the database.
     *
     * @param cargoId UID of the cargo
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    @Override
    public ServerResponse deleteCargo(Integer cargoId) {
        ServerResponse response = new ServerResponse();
        if (cargoValidator.validateBeforeDelete(cargoId, response)) {
            cargoService.delete(cargoId);
        }
        return response;
    }

    /**
     * Adds cargo to the database.
     *
     * @param cargoDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    @Override
    public ServerResponse addCargo(CargoDto cargoDto) {
        ServerResponse response = new ServerResponse();
        if (cargoValidator.validateBeforeSave(cargoDto, response)) {
            cargoService.save(cargoDto);
        }
        return response;
    }

    /**
     * Updates the name of the cargo from the database.
     *
     * @param cargoDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    @Override
    public ServerResponse updateCargoName(CargoDto cargoDto) {
        ServerResponse responseObject = new ServerResponseObject<>();
        if (!cargoValidator.validateBeforeLoad(cargoDto.getUniqueIdentificator(), responseObject)) {
            return responseObject;
        } else {
            CargoDto innerCargoDto = cargoService.find(cargoDto.getUniqueIdentificator());
            innerCargoDto.setName(cargoDto.getName());
            if (cargoValidator.validateBeforeUpdate(innerCargoDto, responseObject)) {
                cargoService.update(innerCargoDto);
            }
        }
        return responseObject;
    }

    /**
     * Updates the mass of the cargo from the database.
     *
     * @param cargoDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    @Override
    public ServerResponse updateCargoMass(CargoDto cargoDto) {
        ServerResponse responseObject = new ServerResponseObject<>();
        if (!cargoValidator.validateBeforeLoad(cargoDto.getUniqueIdentificator(), responseObject)) {
            return responseObject;
        } else {
            CargoDto innerCargoDto = cargoService.find(cargoDto.getUniqueIdentificator());
            innerCargoDto.setMass(cargoDto.getMass());
            if (cargoValidator.validateBeforeUpdate(innerCargoDto, responseObject)) {
                cargoService.update(innerCargoDto);
            }
        }
        return responseObject;
    }
}
