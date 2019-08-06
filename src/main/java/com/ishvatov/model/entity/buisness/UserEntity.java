package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.enum_types.UserRoleType;
import lombok.*;

import javax.persistence.*;

/**
 * Represents a basic user in the data base.
 *
 * @author Sergey Khvatov
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends AbstractEntity {

    /**
     * String representation of the 'password'
     * column name in the table.
     */
    public static final String PASSWORD_FIELD = "password";

    /**
     * String representation of the 'authority'
     * column name in the table.
     */
    public static final String AUTHORITY_FIELD = "authority";

    /**
     * UserEntity login userPassword.
     */
    @Column(name = PASSWORD_FIELD)
    private String password;

    /**
     * User authority.
     */
    @Column(name = AUTHORITY_FIELD)
    @Enumerated(EnumType.STRING)
    private UserRoleType authority;
}
