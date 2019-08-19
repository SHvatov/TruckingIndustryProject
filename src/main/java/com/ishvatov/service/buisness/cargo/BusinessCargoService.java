package com.ishvatov.service.buisness.cargo;

import com.ishvatov.service.buisness.response.ServerResponse;
import com.ishvatov.service.buisness.response.ServerResponseObject;
import com.ishvatov.model.dto.CargoDto;

import java.util.List;

/**
 * Defines basic methods, that are used in the business
 * logic.
 *
 * @author Sergey Khvatov
 */
public interface BusinessCargoService {

    /**
     * Gets all the cargo from the database.
     *
     * @return list of cargo from database.
     */
    List<CargoDto> loadAllCargo();

    /**
     * Gets all not delivered cargo from the database.
     *
     * @return list of cargo from database.
     */
    List<Integer> listAllCargoId();

    /**
     * Fetches data about cargo from the database.
     *
     * @param cargoId UID of the cargo.
     * @return requested truck object or error.
     */
    ServerResponseObject<CargoDto> loadCargo(Integer cargoId);

    /**
     * Deletes cargo from the database.
     *
     * @param cargoId UID of the cargo
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    ServerResponse deleteCargo(Integer cargoId);

    /**
     * Adds cargo to the database.
     *
     * @param cargoDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    ServerResponse addCargo(CargoDto cargoDto);

    /**
     * Updates the name of the cargo from the database.
     *
     * @param cargoDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    ServerResponse updateCargoName(CargoDto cargoDto);

    /**
     * Updates the mass of the cargo from the database.
     *
     * @param cargoDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    ServerResponse updateCargoMass(CargoDto cargoDto);
}
