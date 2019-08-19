package com.ishvatov.service.buisness.truck;

import com.ishvatov.service.buisness.response.ServerResponse;
import com.ishvatov.service.buisness.response.ServerResponseObject;
import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.service.inner.truck.TruckService;
import com.ishvatov.service.buisness.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * {@link BusinessTruckService} implementation.
 *
 * @author Sergey Khvatov
 */
@Service
public class BusinessTruckServiceImpl implements BusinessTruckService {

    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private TruckService truckService;

    /**
     * Autowired validator.
     */
    @Autowired
    private CustomValidator<TruckDto, String> truckValidator;

    /**
     * Gets all the trucks from the database.
     *
     * @return list of trucks from database.
     */
    @Override
    public List<TruckDto> loadAllTrucks() {
        return truckService.findAll();
    }

    /**
     * Fetches data about truck from the database.
     *
     * @param truckId UID of the truck.
     * @return requested truck object or error.
     */
    @Override
    public ServerResponseObject<TruckDto> loadTruck(String truckId) {
        ServerResponseObject<TruckDto> responseObject = new ServerResponseObject<>();
        if (truckValidator.validateBeforeLoad(truckId, responseObject)) {
            responseObject.setObject(truckService.find(truckId));
        }
        return responseObject;
    }

    /**
     * Deletes truck from database.
     *
     * @param truckId UID of the truck.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    @Override
    public ServerResponse deleteTruck(String truckId) {
        ServerResponse response = new ServerResponse();
        if (truckValidator.validateBeforeDelete(truckId, response)) {
            truckService.delete(truckId);
        }
        return response;
    }

    /**
     * Adds truck to the database.
     *
     * @param truckDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    @Override
    public ServerResponse addTruck(TruckDto truckDto) {
        ServerResponse response = new ServerResponse();
        if (truckValidator.validateBeforeSave(truckDto, response)) {
            truckService.save(truckDto);
        }
        return response;
    }

    /**
     * Updates the capacity of the truck.
     *
     * @param truckDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    @Override
    public ServerResponse updateTruckCapacity(TruckDto truckDto) {
        ServerResponse responseObject = new ServerResponseObject<>();
        if (!truckValidator.validateBeforeLoad(truckDto.getUniqueIdentificator(), responseObject)) {
            return responseObject;
        } else {
            TruckDto innerTruckDto = truckService.find(truckDto.getUniqueIdentificator());
            innerTruckDto.setCapacity(truckDto.getCapacity());
            if (truckValidator.validateBeforeUpdate(innerTruckDto, responseObject)) {
                truckService.update(innerTruckDto);
            }
        }
        return responseObject;
    }

    /**
     * Updates the shift size of the truck.
     *
     * @param truckDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    @Override
    public ServerResponse updateTruckShiftSize(TruckDto truckDto) {
        ServerResponse responseObject = new ServerResponseObject<>();
        if (!truckValidator.validateBeforeLoad(truckDto.getUniqueIdentificator(), responseObject)) {
            return responseObject;
        } else {
            TruckDto innerTruckDto = truckService.find(truckDto.getUniqueIdentificator());
            innerTruckDto.setShiftSize(truckDto.getShiftSize());
            if (truckValidator.validateBeforeUpdate(innerTruckDto, responseObject)) {
                truckService.update(innerTruckDto);
            }
        }
        return responseObject;
    }

    /**
     * Updates the status of the truck.
     *
     * @param truckDto dto object.
     * @return ServerResponse object, which stores information about the process
     * of the request.
     */
    @Override
    public ServerResponse updateTruckStatus(TruckDto truckDto) {
        ServerResponse responseObject = new ServerResponseObject<>();
        if (!truckValidator.validateBeforeLoad(truckDto.getUniqueIdentificator(), responseObject)) {
            return responseObject;
        } else {
            TruckDto innerTruckDto = truckService.find(truckDto.getUniqueIdentificator());
            innerTruckDto.setStatus(truckDto.getStatus());
            if (truckValidator.validateBeforeUpdate(innerTruckDto, responseObject)) {
                truckService.update(innerTruckDto);
            }
        }
        return responseObject;
    }
}
