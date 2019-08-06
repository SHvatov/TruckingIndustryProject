package com.ishvatov.model.dto;

import com.ishvatov.model.entity.enum_types.UserRoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Basic user DTO implementation.
 *
 * @author Sergey Khvatov
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements BaseDtoInterface<String> {

    /**
     * Unique id of the user.
     */
    private String uniqueIdentificator;

    /**
     * UserEntity login userPassword.
     */
    private String password;

    /**
     * Role of the user.
     */
    private UserRoleType authority;
}
