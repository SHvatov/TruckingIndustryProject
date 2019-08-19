package com.ishvatov.service.buisness.order;

import com.ishvatov.service.buisness.response.ServerResponse;
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
            response.addError("waypoints",
                messageSource.getMessage("order.waypoints.not.empty", null, Locale.ENGLISH));
            return false;
        }
        return checkWaypointsPairs(wayPointDtoList, response)
            && checkWaypointsCities(wayPointDtoList, response)
            && checkWaypointsCargo(wayPointDtoList, response)
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
            && validateWayPoints(dto.getWaypoints(), response);
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
            && validateTruck(dto.getTruckId(), response)
            && checkWaypointsPath(dto.getTruckId(), dto.getWaypoints(), response);
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
            && validateTruck(entityDto.getTruckId(), response)
            && validateDrivers(entityDto.getAssignedDrivers(), response);
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
                messageSource.getMessage("not.empty.order.uid", null, Locale.ENGLISH));
            return false;
        } else if (uid.length() < MIN_UID_LEN || uid.length() > MAX_UID_LEN) {
            response.addError("uniqueIdentificator",
                messageSource.getMessage("incorrect.order.uid", null, Locale.ENGLISH));
            return false;
        } else if (orderService.exists(uid)) {
            response.addError("uniqueIdentificator",
                messageSource.getMessage("exist.order", null, Locale.ENGLISH));
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
            response.addError("waypoints",
                messageSource.getMessage("order.waypoints.not.empty", null, Locale.ENGLISH));
            return false;
        }
        return validateCity(wayPointDto.getCityId(), response)
            && validateCargo(wayPointDto.getCargoId(), response);
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
            response.addError("waypoints",
                messageSource.getMessage("not.empty.order.waypoint.city", null, Locale.ENGLISH));
            return false;
        } else if (!cityService.exists(cityUID)) {
            response.addError("waypoints",
                messageSource.getMessage("not.exist.city", null, Locale.ENGLISH));
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
            response.addError("waypoints",
                messageSource.getMessage("not.empty.order.waypoint.cargo", null, Locale.ENGLISH));
            return false;
        } else if (!cargoService.exists(cargoUID)) {
            response.addError("waypoints",
                messageSource.getMessage("not.exist.cargo", null, Locale.ENGLISH));
            return false;
        } else {
            CargoDto cargoDto = cargoService.find(cargoUID);
            if (cargoDto.getStatus() != CargoStatusType.READY) {
                response.addError("waypoints",
                    messageSource.getMessage("incorrect.cargo.order", null, Locale.ENGLISH));
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
            response.addError("truckId",
                messageSource.getMessage("not.empty.order.truck", null, Locale.ENGLISH));
            return false;
        } else if (!truckService.exists(truckUID)) {
            response.addError("truckId",
                messageSource.getMessage("not.exist.truck", null, Locale.ENGLISH));
            return false;
        } else {
            TruckDto truckDto = truckService.find(truckUID);
            if (truckDto.getOrderId() != null) {
                response.addError("truckId",
                    messageSource.getMessage("incorrect.truck.order", null, Locale.ENGLISH));
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
            response.addError("assignedDrivers",
                messageSource.getMessage("not.empty.order.drivers", null, Locale.ENGLISH));
            return false;
        } else if (driversUIDSet.size() > 2) {
            response.addError("assignedDrivers",
                messageSource.getMessage("incorrect.order.driver.num", null, Locale.ENGLISH));
            return false;
        } else {
            for (String driverUID : driversUIDSet) {
                if (!driverService.exists(driverUID)) {
                    response.addError("assignedDrivers",
                        messageSource.getMessage("not.exist.driver", null, Locale.ENGLISH));
                    return false;
                } else {
                    DriverDto driverDto = driverService.find(driverUID);
                    if (driverDto.getOrderId() != null) {
                        response.addError("assignedDrivers",
                            messageSource.getMessage("incorrect.driver.order", null, Locale.ENGLISH));
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
            switch (wayPointDto.getAction()) {
                case LOADING:
                    if (waypointsAction.containsKey(wayPointDto.getCargoId())) {
                        response.addError("waypoints",
                            messageSource.getMessage("incorrect.order.waypoints", null, Locale.ENGLISH));
                        return false;
                    } else {
                        waypointsAction.put(wayPointDto.getCargoId(), -1);
                    }
                    break;
                case UNLOADING:
                    if (!waypointsAction.containsKey(wayPointDto.getCargoId())) {
                        response.addError("waypoints",
                            messageSource.getMessage("incorrect.order.waypoints", null, Locale.ENGLISH));
                        return false;
                    } else {
                        int value = waypointsAction.get(wayPointDto.getCargoId()) + 1;
                        waypointsAction.put(wayPointDto.getCargoId(), value);
                    }
                    break;
            }
        }

        // check the map
        for (Map.Entry<Integer, Integer> entry : waypointsAction.entrySet()) {
            if (entry.getValue() != 0) {
                response.addError("waypoints",
                    messageSource.getMessage("incorrect.order.waypoints", null, Locale.ENGLISH));
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
            switch (wayPointDto.getAction()) {
                case LOADING:
                    waypointsCities.put(wayPointDto.getCargoId(), wayPointDto.getCityId());
                    break;
                case UNLOADING:
                    if (waypointsCities.containsKey(wayPointDto.getCargoId())) {
                        String cityName = waypointsCities.get(wayPointDto.getCargoId());
                        if (cityName.equals(wayPointDto.getCityId())) {
                            response.addError("waypoints",
                                messageSource.getMessage("incorrect.order.waypoints", null, Locale.ENGLISH));
                            return false;
                        }
                    }
                    break;
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
    private boolean checkWaypointsCargo(List<WayPointDto> wayPointDtoList, ServerResponse response) {
        // fill map with waypoints
        Map<Integer, Integer> waypointsCargo = new HashMap<>();
        for (WayPointDto wayPointDto : wayPointDtoList) {
            if (!waypointsCargo.containsKey(wayPointDto.getCargoId())) {
                waypointsCargo.put(wayPointDto.getCargoId(), 1);
            } else {
                int current = waypointsCargo.get(wayPointDto.getCargoId());
                waypointsCargo.put(wayPointDto.getCargoId(), current + 1);
            }
        }

        // check the map
        for (Map.Entry<Integer, Integer> entry : waypointsCargo.entrySet()) {
            if (entry.getValue() != 2) {
                response.addError("waypoints",
                    messageSource.getMessage("incorrect.order.waypoint.cargo", null, Locale.ENGLISH));
                return false;
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
            response.addError("waypoints",
                messageSource.getMessage("incorrect.order.waypoints.path", null, Locale.ENGLISH));
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
        if (!cityMapService.checkIfPathExists(truckDto.getCityId(), wayPointDtoList)) {
            response.addError("truckId",
                messageSource.getMessage("incorrect.order.waypoints.path.truck", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }
}
