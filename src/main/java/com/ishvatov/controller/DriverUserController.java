package com.ishvatov.controller;

import com.ishvatov.controller.response.ServerResponse;
import com.ishvatov.controller.response.ServerResponseObject;
import com.ishvatov.model.dto.*;
import com.ishvatov.model.entity.enum_types.*;
import com.ishvatov.service.inner.cargo.CargoService;
import com.ishvatov.service.inner.driver.DriverService;
import com.ishvatov.service.inner.order.OrderService;
import com.ishvatov.service.inner.truck.TruckService;
import com.ishvatov.service.inner.waypoint.WayPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.*;

/**
 * This controller is designed to process the requests made by the admin.
 *
 * @author Sergey Khvatov
 */
@RestController
@RequestMapping("/driver")
public class DriverUserController {

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
    @GetMapping("/{uid}/load")
    @ResponseBody
    public ServerResponseObject<DriverInfoDto> loadDriverInformation(@PathVariable(name = "uid") String uid) {
        ServerResponseObject<DriverInfoDto> response = new ServerResponseObject<>();
        // if driver exists
        if (driverService.exists(uid)) {
            // create new data object
            DriverInfoDto infoDto = new DriverInfoDto();

            // fetch the driver entity
            DriverDto driverDto = driverService.find(uid);

            // check if new month
            int prevMonth = new Date(driverDto.getLastUpdated()
                .getTime())
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .getMonthValue();
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
            // if it is a new month
            if (currentMonth != prevMonth) {
                // if driver is idle then set his WH to 0
                if (driverDto.getDriverStatus() == DriverStatusType.IDLE) {
                    driverDto.setDriverWorkedHours(0);
                } else {
                    // find the first date of the month - 00:00:00
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.DAY_OF_MONTH, 1);
                    c.set(Calendar.HOUR_OF_DAY, 0);
                    c.set(Calendar.MINUTE, 0);
                    c.set(Calendar.SECOND, 0);
                    // else calculate the time, that has passed since last time and add it
                    int diff =
                        (int) (Calendar.getInstance().getTimeInMillis() - c.getTimeInMillis() / 1000);
                    driverDto.setDriverWorkedHours(diff);
                    driverDto.setLastUpdated(new Timestamp(new Date().getTime()));
                }
                driverService.update(driverDto);
            }

            // set driver entity
            infoDto.setDriverInformation(driverDto);

            // set waypoints if there is an order
            if (driverDto.getDriverOrderUID() != null
                && orderService.exists(driverDto.getDriverOrderUID())) {
                // get the order
                OrderDto orderDto = orderService.find(driverDto.getDriverOrderUID());

                // get the ids of the waypoints
                List<Integer> waypointIdList = orderDto.getWaypointsIDList();
                // add waypoints to the data pack
                List<WayPointDto> wayPointDtoList = new ArrayList<>();
                for (Integer id : waypointIdList) {
                    if (wayPointService.exists(id)) {
                        wayPointDtoList.add(wayPointService.find(id));
                    }
                }
                infoDto.setWayPointDtoArray(wayPointDtoList);
                infoDto.setOrderStatus(orderDto.getOrderStatus());
            }

            // set second driver info
            if (driverDto.getDriverOrderUID() != null
                && orderService.exists(driverDto.getDriverOrderUID())) {
                // get the order
                OrderDto orderDto = orderService.find(driverDto.getDriverOrderUID());
                // set info
                if (orderDto.getDriversUIDSet().size() > 1) {
                    // ONLY TWO DRIVERS IN A TRUCK!!!
                    orderDto.getDriversUIDSet().remove(driverDto.getUniqueIdentificator());
                    infoDto.setSecondDriverUID(orderDto.getDriversUIDSet().toArray(new String[0])[0]);
                }
            } else {
                infoDto.setSecondDriverUID("None");
            }
            response.setObject(infoDto);
        } else {
            // else return error
            response.addError("uniqueIdentificator",
                messageSource.getMessage("NotExist.driver", null, Locale.ENGLISH));
        }
        return response;
    }

    /**
     * Complete waypoint request.
     *
     * @param wayPointUID UID of the waypoint.
     * @param uid UID of the driver.
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
            if (driverDto.getDriverOrderUID() != null
                && orderService.exists(driverDto.getDriverOrderUID())) {
                // get the order
                OrderDto orderDto = orderService.find(driverDto.getDriverOrderUID());

                // check the order status
                if (orderDto.getOrderStatus() != OrderStatusType.IN_PROGRESS) {
                    // else return error
                    response.addError("error",
                        messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
                    return response;
                } else {
                    // check if driver's status is not LOADING / UNLOADING
                    if (driverDto.getDriverStatus() != DriverStatusType.LOADING_UNLOADING) {
                        // then return error
                        response.addError("error",
                            messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
                        return response;
                    }

                    // get the ids of the waypoints
                    List<Integer> waypointIdList = orderDto.getWaypointsIDList();

                    // check the waypoints
                    for (Integer id : waypointIdList) {
                        if (wayPointService.exists(id)) {
                            WayPointDto wayPointDto = wayPointService.find(id);

                            // if waypoint is in the order then change status
                            if (wayPointDto.getId().equals(wayPointUID)) {

                                // check if waypoint is already completed
                                if (wayPointDto.getWayPointStatus() == WayPointStatus.COMPLETED) {
                                    // then return error
                                    response.addError("error",
                                        messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
                                    return response;
                                }

                                // update waypoint status
                                wayPointDto.setWayPointStatus(WayPointStatus.COMPLETED);
                                wayPointService.update(wayPointDto);

                                // update cargo status
                                if (cargoService.exists(wayPointDto.getWaypointCargoUID())) {
                                    CargoDto cargoDto = cargoService.find(wayPointDto.getWaypointCargoUID());
                                    if (wayPointDto.getCargoAction() == CargoActionType.LOADING) {
                                        cargoDto.setCargoStatus(CargoStatusType.SHIPPING);
                                    } else {
                                        cargoDto.setCargoStatus(CargoStatusType.DELIVERED);
                                    }
                                    cargoService.update(cargoDto);
                                }

                                // update cities of the drivers and the last time they have been updated
                                Set<String> driversUIDSet = orderDto.getDriversUIDSet();
                                for (String driverUID : driversUIDSet) {
                                    if (driverService.exists(driverUID)) {
                                        DriverDto dto = driverService.find(driverUID);
                                        // update city
                                        dto.setCurrentCityUID(wayPointDto.getWaypointCityUID());
                                        // update worked hours
                                        int workedHours =
                                            (int) (new Date().getTime() - dto.getLastUpdated().getTime()) / 1000;
                                        dto.setDriverWorkedHours(workedHours);
                                        // update last time updated
                                        dto.setLastUpdated(new Timestamp(new Date().getTime()));
                                        driverService.update(dto);
                                    }
                                }

                                // update truck's city
                                if (truckService.exists(orderDto.getTruckUID())) {
                                    TruckDto truckDto = truckService.find(orderDto.getTruckUID());
                                    truckDto.setTruckCityUID(wayPointDto.getWaypointCityUID());
                                    truckService.update(truckDto);
                                }

                                // update order
                                orderDto.setLastUpdated(new Timestamp(new Date().getTime()));
                                orderService.update(orderDto);
                                return response;
                            } else if (wayPointDto.getWayPointStatus() != WayPointStatus.COMPLETED) {
                                // else return error
                                response.addError("error",
                                    messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
                                return response;
                            }
                        }
                    }
                }
            } else {
                // else return error
                response.addError("error",
                    messageSource.getMessage("NotExist.driver", null, Locale.ENGLISH));
            }
        } else {
            // else return error
            response.addError("error",
                messageSource.getMessage("NotExist.driver", null, Locale.ENGLISH));
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
            if (driverDto.getDriverOrderUID() != null
                && orderService.exists(driverDto.getDriverOrderUID())) {
                OrderDto orderDto = orderService.find(driverDto.getDriverOrderUID());

                // check the order status
                if (orderDto.getOrderStatus() != OrderStatusType.IN_PROGRESS) {
                    // else return error
                    response.addError("error",
                        messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
                    return response;
                } else {
                    // if driver was idle
                    if (driverDto.getDriverStatus() == DriverStatusType.IDLE) {
                        // then update his status and last updated
                        driverDto.setDriverStatus(DriverStatusType.IN_SHIFT);
                        driverDto.setLastUpdated(new Timestamp(new Date().getTime()));
                    } else {
                        // update status
                        driverDto.setDriverStatus(DriverStatusType.IDLE);
                        // update worked hours
                        int workedHours =
                            (int) (new Date().getTime() - driverDto.getLastUpdated().getTime()) / 1000;
                        driverDto.setDriverWorkedHours(workedHours);
                        // update last updated
                        driverDto.setLastUpdated(new Timestamp(new Date().getTime()));
                    }
                    // update driver
                    driverService.update(driverDto);
                }
            } else {
                // else return error
                response.addError("error",
                    messageSource.getMessage("NotExist.driver", null, Locale.ENGLISH));
            }
        } else {
            // else return error
            response.addError("error",
                messageSource.getMessage("NotExist.driver", null, Locale.ENGLISH));
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
            if (driverDto.getDriverOrderUID() != null
                && orderService.exists(driverDto.getDriverOrderUID())) {
                OrderDto orderDto = orderService.find(driverDto.getDriverOrderUID());

                // check the order status
                if (orderDto.getOrderStatus() != OrderStatusType.IN_PROGRESS) {
                    // else return error
                    response.addError("error",
                        messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
                    return response;
                } else {
                    // update status
                    driverDto.setDriverStatus(statusType);
                    driverService.update(driverDto);
                }
            } else {
                // else return error
                response.addError("error",
                    messageSource.getMessage("NotExist.driver", null, Locale.ENGLISH));
            }
        } else {
            // else return error
            response.addError("error",
                messageSource.getMessage("NotExist.driver", null, Locale.ENGLISH));
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
            if (driverDto.getDriverOrderUID() != null
                && orderService.exists(driverDto.getDriverOrderUID())) {
                OrderDto orderDto = orderService.find(driverDto.getDriverOrderUID());

                // check the order status
                if (orderDto.getOrderStatus() != OrderStatusType.IN_PROGRESS) {
                    // else return error
                    response.addError("error",
                        messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
                    return response;
                } else {
                    // get the ids of the waypoints
                    List<Integer> waypointIdList = orderDto.getWaypointsIDList();

                    // check if all waypoints are completed
                    for (Integer id : waypointIdList) {
                        if (wayPointService.exists(id)) {
                            WayPointDto wayPointDto = wayPointService.find(id);
                            if (wayPointDto.getWayPointStatus() != WayPointStatus.COMPLETED) {
                                // else return error
                                response.addError("error",
                                    messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
                                return response;
                            }
                        }
                    }

                    // update truck
                    if (truckService.exists(orderDto.getTruckUID())) {
                        TruckDto truckDto = truckService.find(orderDto.getTruckUID());
                        truckDto.setTruckDriversUIDSet(new HashSet<>());
                        truckService.update(truckDto);
                    }

                    // update drivers status
                    Set<String> driversUIDSet = orderDto.getDriversUIDSet();
                    for (String driverUID : driversUIDSet) {
                        if (driverService.exists(driverUID)) {
                            DriverDto dto = driverService.find(driverUID);
                            dto.setDriverStatus(DriverStatusType.IDLE);
                            dto.setLastUpdated(new Timestamp(new Date().getTime()));
                            driverService.update(dto);
                        }
                    }

                    // update order
                    orderDto.setOrderStatus(OrderStatusType.COMPLETED);
                    orderDto.setTruckUID(null);
                    orderDto.setLastUpdated(new Timestamp(new Date().getTime()));
                    orderDto.setDriversUIDSet(new HashSet<>());
                    orderService.update(orderDto);
                }
            } else {
                // else return error
                response.addError("error",
                    messageSource.getMessage("NotExist.driver", null, Locale.ENGLISH));
            }
        } else {
            // else return error
            response.addError("error",
                messageSource.getMessage("NotExist.driver", null, Locale.ENGLISH));
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
            if (driverDto.getDriverOrderUID() != null
                && orderService.exists(driverDto.getDriverOrderUID())) {
                OrderDto orderDto = orderService.find(driverDto.getDriverOrderUID());

                // check the order status
                if (orderDto.getOrderStatus() != OrderStatusType.READY) {
                    // else return error
                    response.addError("error",
                        messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
                    return response;
                } else {
                    // update order
                    orderDto.setOrderStatus(OrderStatusType.IN_PROGRESS);
                    orderDto.setLastUpdated(new Timestamp(new Date().getTime()));
                    orderService.update(orderDto);

                    // update drivers
                    Set<String> driversUIDSet = orderDto.getDriversUIDSet();
                    for (String driverUID : driversUIDSet) {
                        if (driverService.exists(driverUID)) {
                            DriverDto dto = driverService.find(driverUID);
                            dto.setDriverStatus(DriverStatusType.IN_SHIFT);
                            dto.setLastUpdated(new Timestamp(new Date().getTime()));
                            driverService.update(driverDto);
                        }
                    }
                }
            } else {
                // else return error
                response.addError("error",
                    messageSource.getMessage("NotExist.driver", null, Locale.ENGLISH));
            }
        } else {
            // else return error
            response.addError("error",
                messageSource.getMessage("NotExist.driver", null, Locale.ENGLISH));
        }
        return response;
    }
}
