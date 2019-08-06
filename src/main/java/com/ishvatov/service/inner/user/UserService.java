package com.ishvatov.service.inner.user;

import com.ishvatov.exception.DAOException;
import com.ishvatov.model.dto.UserDto;
import com.ishvatov.service.BaseService;

/**
 * Defines a basic interface to interact with
 * user DAO layer.
 *
 * @author Sergey Khvatov
 */
public interface UserService extends BaseService<String, UserDto> {

    /**
     * Updates password of the user.
     *
     * @param userUID  UID of the user.
     * @param password new password of the user.
     * @throws DAOException if no such entity exists.
     */
    void updatePassword(String userUID, String password);
}
