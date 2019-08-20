package com.ishvatov.service.buisness.response;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines a basic class, which stores information about server's
 * response after request.
 *
 * @author Sergey Khvatov
 */
@Data
public class ServerResponse {

    /**
     * Status of the request if everything went fine.
     */
    public static final String SUCCESS = "success";

    /**
     * Status of the request, if it has failed.
     */
    public static final String FAILED = "failed";

    /**
     * Status of the request.
     */
    private String status;

    /**
     * Map with all the messages, if any exists.
     */
    private Map<String, String> messages = new HashMap<>();

    /**
     * Default class constructor.
     */
    public ServerResponse() {
        this.status = SUCCESS;
    }

    /**
     * Adds error to the messages field.
     *
     * @param field   field, where error has occurred.
     * @param message server's message.
     */
    public void addError(String field, String message) {
        this.status = FAILED;
        messages.put(field, message);
    }
}
