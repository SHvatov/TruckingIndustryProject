package com.ishvatov.model.dto;

import com.ishvatov.model.entity.enum_types.UserRoleType;
import lombok.Data;

/**
 * Basic user DTO implementation.
 *
 * @author Sergey Khvatov
 */
@Data
public class UserDto {

    /**
     * Unique id of the user.
     */
    private String uniqueIdentificator;

    /**
     * UserEntity login userPassword.
     */
    private String password;

    /**
     * Non-serializable field, used to confirm the userPassword.
     */
    private String confirmPassword;

    /**
     * Role of the user.
     */
    private UserRoleType role;
}
