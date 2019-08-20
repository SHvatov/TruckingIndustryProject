package com.ishvatov.service.buisness.order;

import com.ishvatov.service.buisness.response.ServerResponse;
import com.ishvatov.model.dto.OrderWaypointDto;
import com.ishvatov.model.dto.WayPointDto;
import com.ishvatov.service.buisness.CustomValidator;

import java.util.List;

/**
 * Specialization of the {@link CustomValidator} interface for order entities.
 */
public interface OrderValidator extends CustomValidator<OrderWaypointDto, String> {

    /**
     * Validates the list of the waypoints, submitted by the user.
     *
     * @param wayPointDtoList list of waypoints ordered by the user.
     * @param response        Server's response.
     * @return true, if validation was passed, false otherwise.
     */
    boolean validateWayPoints(List<WayPointDto> wayPointDtoList, ServerResponse response);

    /**
     * Validates user input data before fetching data from server.
     *
     * @param dto      DTO object.
     * @param response Server's response.
     * @return true, if validation was passed, false otherwise.
     */
    boolean validateBeforeTruckFetch(OrderWaypointDto dto, ServerResponse response);

    /**
     * Validates user input data before fetching data from server.
     *
     * @param dto      DTO object.
     * @param response Server's response.
     * @return true, if validation was passed, false otherwise.
     */
    boolean validateBeforeDriverFetch(OrderWaypointDto dto, ServerResponse response);
}
