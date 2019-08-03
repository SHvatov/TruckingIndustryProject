package com.ishvatov.model.entity.security;

import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.enum_types.UserRoleType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Represents a basic user in the data base.
 *
 * @author Sergey Khvatov
 */
@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends AbstractEntity {

    /**
     * String representation of the 'password'
     * column name in the table.
     */
    public static final String PASSWORD = "password";

    /**
     * String representation of the 'authority'
     * column name in the table.
     */
    public static final String AUTHORITY = "authority";

    /**
     * UserEntity login password.
     */
    @Column(name = PASSWORD)
    private String userPassword;

    /**
     * User authority.
     */
    @Column(name = AUTHORITY)
    @Enumerated(EnumType.STRING)
    private UserRoleType userAuthority;
}
