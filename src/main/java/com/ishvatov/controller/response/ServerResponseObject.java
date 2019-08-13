package com.ishvatov.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Server response object, which stores not only information about
 * server's response, but also an object or null reference in case of error.
 *
 * @param <T> type of the stored object.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerResponseObject<T> extends ServerResponse {

    /**
     * Stored object.
     */
    private T object;
}
