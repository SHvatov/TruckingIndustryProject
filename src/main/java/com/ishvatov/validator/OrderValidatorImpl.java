package com.ishvatov.validator;

import com.ishvatov.controller.response.ServerResponse;
import com.ishvatov.model.dto.*;
import com.ishvatov.model.entity.enum_types.CargoStatusType;
import com.ishvatov.service.inner.cargo.CargoService;
import com.ishvatov.service.inner.city.CityService;
import com.ishvatov.service.inner.city_map.CityMapService;
import com.ishvatov.service.inner.driver.DriverService;
import com.ishvatov.service.inner.order.OrderService;
import com.ishvatov.service.inner.truck.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * {@link OrderValidator} implementation class.
 *
 * @author Sergey Khvatov
 */
@Component("orderValidator")
public class OrderValidatorImpl implements OrderValidator {

    /**
     * Min length of the UID.
     */
    private static final int MIN_UID_LEN = 5;

    /**
     * Max length of the UID.
     */
    private static final int MAX_UID_LEN = 20;

    /**
     * Autowired message source.
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * Autowired service.
     */
    @Autowired
    private OrderService orderService;

    /**
     * Autowired service.
     */
    @Autowired
    private CityService cityService;

    /**
     * Autowired service.
     */
    @Autowired
    private CityMapService cityMapService;

    /**
     * Autowired service.
     */
    @Autowired
    private TruckService truckService;

    /**
     * Autowired service.
     */
    @Autowired
    private DriverService driverService;

    /**
     * Autowired service.
     */
    @Autowired
    private CargoService cargoService;

    /**
     * Validates the list of the waypoints, submitted by the user.
     *
     * @param wayPointDtoList list of waypoints ordered by the user.
     * @param response        Server's response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateWayPoints(List<WayPointDto> wayPointDtoList, ServerResponse response) {
        if (wayPointDtoList == null || wayPointDtoList.isEmpty()) {
            response.addError("wayPointDtoArray",
                messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
            return false;
        }
        return checkWaypointsPairs(wayPointDtoList, response)
            && checkWaypointsCities(wayPointDtoList, response)
            && checkWaypointsPath(wayPointDtoList, response);
    }

    /**
     * Validates user input data before fetching data from server.
     *
     * @param dto      DTO object.
     * @param response Server's response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeTruckFetch(OrderWaypointDto dto, ServerResponse response) {
        return validateUID(dto.getUniqueIdentificator(), response)
            && validateWayPoints(dto.getWayPointDtoArray(), response);
    }

    /**
     * Validates user input data before fetching data from server.
     *
     * @param dto      DTO object.
     * @param response Server's response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeDriverFetch(OrderWaypointDto dto, ServerResponse response) {
        return validateBeforeTruckFetch(dto, response)
            && validateTruck(dto.getTruckUID(), response);
    }

    /**
     * Validates entity before loading it.
     *
     * @param entityUID UID of the entity.
     * @param response  Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeLoad(String entityUID, ServerResponse response) {
        return false;
    }

    /**
     * Validates entity before updating its capacity.
     *
     * @param entityDto DTO object.
     * @param response  Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeUpdate(OrderWaypointDto entityDto, ServerResponse response) {
        return false;
    }

    /**
     * Validates entity before deleting it.
     *
     * @param entityUID UID of the entity.
     * @param response  Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeDelete(String entityUID, ServerResponse response) {
        return false;
    }

    /**
     * Validates user's input before saving new entity.
     *
     * @param entityDto DTO object.
     * @param response  Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeSave(OrderWaypointDto entityDto, ServerResponse response) {
        return validateBeforeTruckFetch(entityDto, response)
            && validateTruck(entityDto.getTruckUID(), response)
            && validateDrivers(entityDto.getDriversUIDSet(), response);
    }

    /**
     * Validates UID before saving.
     *
     * @param uid      new UID.
     * @param response Object, which stores server response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean validateUID(String uid, ServerResponse response) {
        if (uid == null || uid.isEmpty()) {
            response.addError("uniqueIdentificator",
                messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
            return false;
        } else if (uid.length() < MIN_UID_LEN || uid.length() > MAX_UID_LEN) {
            response.addError("uniqueIdentificator",
                messageSource.getMessage("Incorrect.order.uid", null, Locale.ENGLISH));
            return false;
        } else if (orderService.exists(uid)) {
            response.addError("uniqueIdentificator",
                messageSource.getMessage("NotUnique.order", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }

    /**
     * Validates input waypoint entity.
     *
     * @param wayPointDto waypoint entity.
     * @param response    Object, which stores server response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean validateWayPoint(WayPointDto wayPointDto, ServerResponse response) {
        if (wayPointDto == null) {
            response.addError("wayPointDtoArray",
                messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
            return false;
        }
        return validateCity(wayPointDto.getWaypointCityUID(), response)
            && validateCargo(wayPointDto.getWaypointCargoUID(), response);
    }

    /**
     * Validates input city entity.
     *
     * @param cityUID  UID of the city.
     * @param response Object, which stores server response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean validateCity(String cityUID, ServerResponse response) {
        if (cityUID == null) {
            response.addError("wayPointDtoArray",
                messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
            return false;
        } else if (!cityService.exists(cityUID)) {
            response.addError("wayPointDtoArray",
                messageSource.getMessage("NotExist.city", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }

    /**
     * Validates input cargo entity.
     *
     * @param cargoUID UID of the cargo.
     * @param response Object, which stores server response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean validateCargo(Integer cargoUID, ServerResponse response) {
        if (cargoUID == null) {
            response.addError("wayPointDtoArray",
                messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
            return false;
        } else if (!cargoService.exists(cargoUID)) {
            response.addError("wayPointDtoArray",
                messageSource.getMessage("NotExist.cargo", null, Locale.ENGLISH));
            return false;
        } else {
            CargoDto cargoDto = cargoService.find(cargoUID);
            if (cargoDto.getCargoStatus() != CargoStatusType.READY) {
                response.addError("wayPointDtoArray",
                    messageSource.getMessage("Incorrect.cargo.order", null, Locale.ENGLISH));
                return false;
            }
        }
        return true;
    }

    /**
     * Validates input truck entity.
     *
     * @param truckUID UID of the truck.
     * @param response Object, which stores server response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean validateTruck(String truckUID, ServerResponse response) {
        if (truckUID == null) {
            response.addError("truckUID",
                messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
            return false;
        } else if (!truckService.exists(truckUID)) {
            response.addError("truckUID",
                messageSource.getMessage("NotExist.truck", null, Locale.ENGLISH));
            return false;
        } else {
            TruckDto truckDto = truckService.find(truckUID);
            if (truckDto.getTruckOrderUID() != null) {
                response.addError("truckUID",
                    messageSource.getMessage("Business.truck.has_order", null, Locale.ENGLISH));
                return false;
            }
        }
        return true;
    }

    /**
     * Validates input truck entity.
     *
     * @param driversUIDSet UID of the truck.
     * @param response Object, which stores server response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean validateDrivers(Set<String> driversUIDSet, ServerResponse response) {
        if (driversUIDSet == null || driversUIDSet.isEmpty()) {
            response.addError("driversUIDSet",
                messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
            return false;
        } else if (driversUIDSet.size() > 2) {
            response.addError("driversUIDSet",
                messageSource.getMessage("Incorrect.order", null, Locale.ENGLISH));
            return false;
        } else {
            for (String driverUID : driversUIDSet) {
                if (!driverService.exists(driverUID)) {
                    response.addError("driversUIDSet",
                        messageSource.getMessage("NotExist.truck", null, Locale.ENGLISH));
                    return false;
                } else {
                    DriverDto driverDto = driverService.find(driverUID);
                    if (driverDto.getDriverOrderUID() != null) {
                        response.addError("driversUIDSet",
                            messageSource.getMessage("Business.truck.has_order", null, Locale.ENGLISH));
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Checks if all waypoints has their own pairs.
     *
     * @param wayPointDtoList list of waypoints.
     * @param response server's response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean checkWaypointsPairs(List<WayPointDto> wayPointDtoList, ServerResponse response) {
        // fill map with waypoints
        Map<Integer, Integer> waypointsAction = new HashMap<>();
        for (WayPointDto wayPointDto : wayPointDtoList) {
            // validate waypoint
            if (!validateWayPoint(wayPointDto, response)) {
                return false;
            }

            // check if waypoints with this cargo id already exists
            switch (wayPointDto.getCargoAction()) {
                case LOADING:
                    if (waypointsAction.containsKey(wayPointDto.getWaypointCargoUID())) {
                        response.addError("wayPointDtoArray",
                            messageSource.getMessage("Incorrect.order.waypoints", null, Locale.ENGLISH));
                        return false;
                    } else {
                        waypointsAction.put(wayPointDto.getWaypointCargoUID(), -1);
                    }
                    break;
                case UNLOADING:
                    if (!waypointsAction.containsKey(wayPointDto.getWaypointCargoUID())) {
                        response.addError("wayPointDtoArray",
                            messageSource.getMessage("Incorrect.order.waypoints", null, Locale.ENGLISH));
                        return false;
                    } else {
                        int value = waypointsAction.get(wayPointDto.getWaypointCargoUID()) + 1;
                        waypointsAction.put(wayPointDto.getWaypointCargoUID(), value);
                    }
                    break;
            }
        }

        // check the map
        for (Map.Entry<Integer, Integer> entry : waypointsAction.entrySet()) {
            if (entry.getValue() != 0) {
                response.addError("wayPointDtoArray",
                    messageSource.getMessage("Incorrect.order.waypoints", null, Locale.ENGLISH));
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if all waypoints has their own pairs.
     *
     * @param wayPointDtoList list of waypoints.
     * @param response server's response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean checkWaypointsCities(List<WayPointDto> wayPointDtoList, ServerResponse response) {
        // fill map with waypoints
        Map<Integer, String> waypointsCities = new HashMap<>();
        for (WayPointDto wayPointDto : wayPointDtoList) {
            switch (wayPointDto.getCargoAction()) {
                case LOADING:
                    waypointsCities.put(wayPointDto.getWaypointCargoUID(), wayPointDto.getWaypointCityUID());
                    break;
                case UNLOADING:
                    if (waypointsCities.containsKey(wayPointDto.getWaypointCargoUID())) {
                        String cityName = waypointsCities.get(wayPointDto.getWaypointCargoUID());
                        if (cityName.equals(wayPointDto.getWaypointCityUID())) {
                            response.addError("wayPointDtoArray",
                                messageSource.getMessage("Incorrect.order.waypoints", null, Locale.ENGLISH));
                            return false;
                        }
                    }
                    break;
            }
        }
        return true;
    }

    /**
     * Checks if all path exists.
     *
     * @param wayPointDtoList list of waypoints.
     * @param response server's response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean checkWaypointsPath(List<WayPointDto> wayPointDtoList, ServerResponse response) {
        if (!cityMapService.checkIfPathExists(wayPointDtoList)) {
            response.addError("wayPointDtoArray",
                messageSource.getMessage("Incorrect.order.waypoints", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }

    /**
     * Checks if all path exists.
     *
     * @param truckUID UID of the truck.
     * @param wayPointDtoList list of waypoints.
     * @param response server's response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean checkWaypointsPath(String truckUID, List<WayPointDto> wayPointDtoList, ServerResponse response) {
        TruckDto truckDto = truckService.find(truckUID);
        if (!cityMapService.checkIfPathExists(truckDto.getTruckCityUID(), wayPointDtoList)) {
            response.addError("truckUID",
                messageSource.getMessage("Incorrect.order.waypoints", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }
}
