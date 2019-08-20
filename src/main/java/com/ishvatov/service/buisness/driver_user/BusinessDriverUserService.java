package com.ishvatov.service.buisness.driver_user;

import com.ishvatov.model.dto.DriverInfoDto;
import com.ishvatov.model.entity.enum_types.DriverStatusType;
import com.ishvatov.service.buisness.response.ServerResponse;
import com.ishvatov.service.buisness.response.ServerResponseObject;

/**
 * Defines basic methods, that are used in the business
 * logic.
 *
 * @author Sergey Khvatov
 */
public interface BusinessDriverUserService {

    /**
     * Loads info about driver.
     *
     * @param uid UID of the driver.
     * @return driver data.
     */
    ServerResponseObject<DriverInfoDto> loadDriverInformation(String uid);

    /**
     * Complete waypoint request.
     *
     * @param wayPointUID UID of the waypoint.
     * @param uid UID of the driver.
     * @return ServerResponse object.
     */
    ServerResponse completeWaypoint(Integer wayPointUID, String uid);

    /**
     * Update driver's session status method.
     *
     * @param uid UID of the driver.
     * @return ServerResponse object.
     */
   ServerResponse changeSession(String uid);

    /**
     * Update driver's session status method.
     *
     * @param uid UID of the driver.
     * @return ServerResponse object.
     */
    ServerResponse changeStatus( DriverStatusType statusType, String uid);

    /**
     * Completes the order.
     *
     * @param uid UID of the driver.
     * @return ServerResponse object.
     */
   ServerResponse completeOrder(String uid);

    /**
     * Start the order.
     *
     * @param uid UID of the driver.
     * @return ServerResponse object.
     */
    ServerResponse startOrder(String uid);
}
