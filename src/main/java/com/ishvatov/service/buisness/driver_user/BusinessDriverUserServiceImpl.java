package com.ishvatov.service.buisness.driver_user;

import com.ishvatov.model.dto.*;
import com.ishvatov.model.entity.enum_types.*;
import com.ishvatov.service.buisness.response.ServerResponse;
import com.ishvatov.service.buisness.response.ServerResponseObject;
import com.ishvatov.service.inner.cargo.CargoService;
import com.ishvatov.service.inner.driver.DriverService;
import com.ishvatov.service.inner.order.OrderService;
import com.ishvatov.service.inner.truck.TruckService;
import com.ishvatov.service.inner.waypoint.WayPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.*;

/**
 * {@link BusinessDriverUserService} implementation.
 *
 * @author Sergey Khvatov
 */
@Service
public class BusinessDriverUserServiceImpl implements BusinessDriverUserService {

    /**
     * Autowired message source.
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private DriverService driverService;

    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private OrderService orderService;

    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private WayPointService wayPointService;

    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private CargoService cargoService;

    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private TruckService truckService;


    /**
     * Loads info about driver.
     *
     * @param uid UID of the driver.
     * @return driver data.
     */
    public ServerResponseObject<DriverInfoDto> loadDriverInformation(String uid) {
        ServerResponseObject<DriverInfoDto> response = new ServerResponseObject<>();
        // if driver exists
        if (driverService.exists(uid)) {
            // create new data object
            DriverInfoDto infoDto = new DriverInfoDto();

            // fetch the driver entity
            DriverDto driverDto = driverService.find(uid);

            // check if new month
            int prevMonth = new Date(driverDto.getLastUpdated().getTime())
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .getMonthValue();
            // get current month
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
            // if it is a new month
            if (currentMonth != prevMonth) {
                // if driver is idle then set his WH to 0
                if (driverDto.getStatus() == DriverStatusType.IDLE) {
                    driverDto.setWorkedHours(0);
                } else {
                    // find the first date of the month - 00:00:00
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.DAY_OF_MONTH, 1);
                    c.set(Calendar.HOUR_OF_DAY, 0);
                    c.set(Calendar.MINUTE, 0);
                    c.set(Calendar.SECOND, 0);
                    // calculate the time, that has passed since new day of the month and add it
                    int diff = (int) (Calendar.getInstance().getTimeInMillis() - c.getTimeInMillis() / 1000);
                    driverDto.setWorkedHours(diff);
                    driverDto.setLastUpdated(new Timestamp(new Date().getTime()));
                }
                driverService.update(driverDto);
            }

            // set driver entity
            infoDto.setDriver(driverDto);

            // set waypoints if there is an order
            if (driverDto.getOrderId() != null
                && orderService.exists(driverDto.getOrderId())) {
                // get the order
                OrderDto orderDto = orderService.find(driverDto.getOrderId());

                // get the ids of the waypoints
                List<Integer> waypointIdList = orderDto.getAssignedWaypoints();
                // add waypoints to the data pack
                List<WayPointDto> wayPointDtoList = new ArrayList<>();
                for (Integer id : waypointIdList) {
                    if (wayPointService.exists(id)) {
                        wayPointDtoList.add(wayPointService.find(id));
                    }
                }

                // set order details
                infoDto.setWaypoints(wayPointDtoList);
                infoDto.setOrderStatus(orderDto.getStatus());

                // set info about second driver
                if (orderDto.getAssignedDrivers().size() > 1) {
                    // ONLY TWO DRIVERS IN A TRUCK!!!
                    orderDto.getAssignedDrivers().remove(driverDto.getUniqueIdentificator());
                    infoDto.setSecondDriverId(orderDto.getAssignedDrivers().toArray(new String[0])[0]);
                } else {
                    infoDto.setSecondDriverId("No second driver");
                }
            }

            response.setObject(infoDto);
        } else {
            // else return error
            response.addError("error",
                messageSource.getMessage("not.exist.driver.user", null, Locale.ENGLISH));
        }
        return response;
    }

    /**
     * Complete waypoint request.
     *
     * @param wayPointUID UID of the waypoint.
     * @param uid         UID of the driver.
     * @return ServerResponse object.
     */
    @PostMapping("/{driverUID}/waypoint/{wayPointUID}")
    @ResponseBody
    public ServerResponse completeWaypoint(@PathVariable(name = "wayPointUID") Integer wayPointUID,
                                           @PathVariable(name = "driverUID") String uid) {
        ServerResponse response = new ServerResponse();
        if (driverService.exists(uid)) {
            DriverDto driverDto = driverService.find(uid);
            // set waypoints if there is an order
            if (driverDto.getOrderId() != null
                && orderService.exists(driverDto.getOrderId())) {
                // get the order
                OrderDto orderDto = orderService.find(driverDto.getOrderId());

                // check the order status
                if (orderDto.getStatus() != OrderStatusType.IN_PROGRESS) {
                    // else return error
                    response.addError("error",
                        messageSource.getMessage("not.empty.driver.user.order", null, Locale.ENGLISH));
                    return response;
                } else {
                    // check if driver's status is not LOADING / UNLOADING
                    if (driverDto.getStatus() != DriverStatusType.LOADING_UNLOADING) {
                        // then return error
                        response.addError("error",
                            messageSource.getMessage("incorrect.driver.user.waypoint.driver.status", null, Locale.ENGLISH));
                        return response;
                    }

                    // get the ids of the waypoints
                    List<Integer> waypointIdList = orderDto.getAssignedWaypoints();

                    // check the waypoints
                    for (Integer id : waypointIdList) {
                        if (wayPointService.exists(id)) {
                            WayPointDto wayPointDto = wayPointService.find(id);

                            // if waypoint is in the order then change status
                            if (wayPointDto.getId().equals(wayPointUID)) {

                                // check if waypoint is already completed
                                if (wayPointDto.getStatus() == WayPointStatusType.COMPLETED) {
                                    // then return error
                                    response.addError("error",
                                        messageSource.getMessage("incorrect.driver.user.waypoint.status", null, Locale.ENGLISH));
                                    return response;
                                }

                                // update waypoint status
                                wayPointDto.setStatus(WayPointStatusType.COMPLETED);
                                wayPointService.update(wayPointDto);

                                // update cargo status
                                if (cargoService.exists(wayPointDto.getCargoId())) {
                                    CargoDto cargoDto = cargoService.find(wayPointDto.getCargoId());
                                    if (wayPointDto.getAction() == CargoActionType.LOADING) {
                                        cargoDto.setStatus(CargoStatusType.SHIPPING);
                                    } else {
                                        cargoDto.setStatus(CargoStatusType.DELIVERED);
                                    }
                                    cargoService.update(cargoDto);
                                }

                                // update cities of the drivers and the last time they have been updated
                                Set<String> driversUIDSet = orderDto.getAssignedDrivers();
                                for (String driverUID : driversUIDSet) {
                                    if (driverService.exists(driverUID)) {
                                        DriverDto dto = driverService.find(driverUID);
                                        // update city
                                        dto.setCityId(wayPointDto.getCityId());
                                        // update worked hours
                                        int workedHours =
                                            (int) (new Date().getTime() - dto.getLastUpdated().getTime()) / 1000;
                                        dto.setWorkedHours(workedHours);
                                        // update last time updated
                                        dto.setLastUpdated(new Timestamp(new Date().getTime()));
                                        driverService.update(dto);
                                    }
                                }

                                // update truck's city
                                if (truckService.exists(orderDto.getTruckId())) {
                                    TruckDto truckDto = truckService.find(orderDto.getTruckId());
                                    truckDto.setCityId(wayPointDto.getCityId());
                                    truckService.update(truckDto);
                                }

                                // update order
                                orderDto.setLastUpdated(new Timestamp(new Date().getTime()));
                                orderService.update(orderDto);
                                return response;
                            } else if (wayPointDto.getStatus() != WayPointStatusType.COMPLETED) {
                                // else return error
                                response.addError("error",
                                    messageSource.getMessage("incorrect.driver.user.waypoint.order", null, Locale.ENGLISH));
                                return response;
                            }
                        }
                    }
                }
            } else {
                // else return error
                response.addError("error",
                    messageSource.getMessage("incorrect.driver.user.order", null, Locale.ENGLISH));
            }
        } else {
            // else return error
            response.addError("error",
                messageSource.getMessage("not.exist.driver.user", null, Locale.ENGLISH));
        }
        return response;
    }

    /**
     * Update driver's session status method.
     *
     * @param uid UID of the driver.
     * @return ServerResponse object.
     */
    @GetMapping("/{uid}/session")
    @ResponseBody
    public ServerResponse changeSession(@PathVariable(name = "uid") String uid) {
        ServerResponse response = new ServerResponse();
        if (driverService.exists(uid)) {
            DriverDto driverDto = driverService.find(uid);

            // check if driver has an order
            if (driverDto.getOrderId() != null
                && orderService.exists(driverDto.getOrderId())) {
                OrderDto orderDto = orderService.find(driverDto.getOrderId());

                // check the order status
                if (orderDto.getStatus() != OrderStatusType.IN_PROGRESS) {
                    // else return error
                    response.addError("error",
                        messageSource.getMessage("not.empty.driver.user.order", null, Locale.ENGLISH));
                    return response;
                } else {
                    // if driver was idle
                    if (driverDto.getStatus() == DriverStatusType.IDLE) {
                        // then update his status and last updated
                        driverDto.setStatus(DriverStatusType.IN_SHIFT);
                        driverDto.setLastUpdated(new Timestamp(new Date().getTime()));
                    } else {
                        // update status
                        driverDto.setStatus(DriverStatusType.IDLE);
                        // update worked hours
                        int workedHours =
                            (int) (new Date().getTime() - driverDto.getLastUpdated().getTime()) / 1000;
                        driverDto.setWorkedHours(workedHours);
                        // update last updated
                        driverDto.setLastUpdated(new Timestamp(new Date().getTime()));
                    }
                    // update driver
                    driverService.update(driverDto);
                }
            } else {
                // else return error
                response.addError("error",
                    messageSource.getMessage("incorrect.driver.user.order", null, Locale.ENGLISH));
            }
        } else {
            // else return error
            response.addError("error",
                messageSource.getMessage("not.exist.driver.user", null, Locale.ENGLISH));
        }
        return response;
    }

    /**
     * Update driver's session status method.
     *
     * @param uid UID of the driver.
     * @return ServerResponse object.
     */
    @PostMapping("/{uid}/status/{status}")
    @ResponseBody
    public ServerResponse changeStatus(@PathVariable(name = "status") DriverStatusType statusType,
                                       @PathVariable(name = "uid") String uid) {
        ServerResponse response = new ServerResponse();
        if (driverService.exists(uid)) {
            DriverDto driverDto = driverService.find(uid);
            // check if driver has an order
            if (driverDto.getOrderId() != null
                && orderService.exists(driverDto.getOrderId())) {
                OrderDto orderDto = orderService.find(driverDto.getOrderId());

                // check the order status
                if (orderDto.getStatus() != OrderStatusType.IN_PROGRESS) {
                    // else return error
                    response.addError("error",
                        messageSource.getMessage("not.empty.driver.user.order", null, Locale.ENGLISH));
                    return response;
                } else {
                    // update status
                    driverDto.setStatus(statusType);
                    driverService.update(driverDto);
                }
            } else {
                // else return error
                response.addError("error",
                    messageSource.getMessage("incorrect.driver.user.order", null, Locale.ENGLISH));
            }
        } else {
            // else return error
            response.addError("error",
                messageSource.getMessage("not.exist.driver.user", null, Locale.ENGLISH));
        }
        return response;
    }

    /**
     * Update driver's session status method.
     *
     * @param uid UID of the driver.
     * @return ServerResponse object.
     */
    @GetMapping("/{uid}/complete")
    @ResponseBody
    public ServerResponse completeOrder(@PathVariable(name = "uid") String uid) {
        ServerResponse response = new ServerResponse();
        if (driverService.exists(uid)) {
            DriverDto driverDto = driverService.find(uid);
            // check if driver has an order
            if (driverDto.getOrderId() != null
                && orderService.exists(driverDto.getOrderId())) {
                OrderDto orderDto = orderService.find(driverDto.getOrderId());

                // check the order status
                if (orderDto.getStatus() != OrderStatusType.IN_PROGRESS) {
                    // else return error
                    response.addError("error",
                        messageSource.getMessage("not.empty.driver.user.order", null, Locale.ENGLISH));
                    return response;
                } else {
                    // get the ids of the waypoints
                    List<Integer> waypointIdList = orderDto.getAssignedWaypoints();

                    // check if all waypoints are completed
                    for (Integer id : waypointIdList) {
                        if (wayPointService.exists(id)) {
                            WayPointDto wayPointDto = wayPointService.find(id);
                            if (wayPointDto.getStatus() != WayPointStatusType.COMPLETED) {
                                // else return error
                                response.addError("error",
                                    messageSource.getMessage("incorrect.driver.user.waypoints.completed", null, Locale.ENGLISH));
                                return response;
                            }
                        }
                    }

                    // update truck
                    if (truckService.exists(orderDto.getTruckId())) {
                        TruckDto truckDto = truckService.find(orderDto.getTruckId());
                        truckDto.setAssignedDrivers(new HashSet<>());
                        truckService.update(truckDto);
                    }

                    // update drivers status
                    Set<String> driversUIDSet = orderDto.getAssignedDrivers();
                    for (String driverUID : driversUIDSet) {
                        if (driverService.exists(driverUID)) {
                            DriverDto dto = driverService.find(driverUID);
                            dto.setStatus(DriverStatusType.IDLE);
                            dto.setLastUpdated(new Timestamp(new Date().getTime()));
                            driverService.update(dto);
                        }
                    }

                    // update order
                    orderDto.setStatus(OrderStatusType.COMPLETED);
                    orderDto.setTruckId(null);
                    orderDto.setLastUpdated(new Timestamp(new Date().getTime()));
                    orderDto.setAssignedDrivers(new HashSet<>());
                    orderService.update(orderDto);
                }
            } else {
                // else return error
                response.addError("error",
                    messageSource.getMessage("incorrect.driver.user.order", null, Locale.ENGLISH));
            }
        } else {
            // else return error
            response.addError("error",
                messageSource.getMessage("not.exist.driver.user", null, Locale.ENGLISH));
        }
        return response;
    }

    /**
     * Update driver's session status method.
     *
     * @param uid UID of the driver.
     * @return ServerResponse object.
     */
    @GetMapping("/{uid}/start")
    @ResponseBody
    public ServerResponse startOrder(@PathVariable(name = "uid") String uid) {
        ServerResponse response = new ServerResponse();
        if (driverService.exists(uid)) {
            DriverDto driverDto = driverService.find(uid);

            // check if driver has an order
            if (driverDto.getOrderId() != null
                && orderService.exists(driverDto.getOrderId())) {
                OrderDto orderDto = orderService.find(driverDto.getOrderId());

                // check the order status
                if (orderDto.getStatus() != OrderStatusType.READY) {
                    // else return error
                    response.addError("error",
                        messageSource.getMessage("incorrect.driver.user.order.start", null, Locale.ENGLISH));
                    return response;
                } else {
                    // update order
                    orderDto.setStatus(OrderStatusType.IN_PROGRESS);
                    orderDto.setLastUpdated(new Timestamp(new Date().getTime()));
                    orderService.update(orderDto);

                    // update drivers
                    Set<String> driversUIDSet = orderDto.getAssignedDrivers();
                    for (String driverUID : driversUIDSet) {
                        if (driverService.exists(driverUID)) {
                            DriverDto dto = driverService.find(driverUID);
                            dto.setStatus(DriverStatusType.IN_SHIFT);
                            dto.setLastUpdated(new Timestamp(new Date().getTime()));
                            driverService.update(dto);
                        }
                    }
                }
            } else {
                // else return error
                response.addError("error",
                    messageSource.getMessage("incorrect.driver.user.order", null, Locale.ENGLISH));
            }
        } else {
            // else return error
            response.addError("error",
                messageSource.getMessage("not.exist.driver.user", null, Locale.ENGLISH));
        }
        return response;
    }
}
