package com.ishvatov.model.entity.enum_types;

/**
 * Defines basic roles for the users in the system.
 *
 * @author Sergey Khvatov
 */
public enum UserRoleType {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_DRIVER;

    /**
     * Get the userAuthority user-friendly description.
     *
     * @return string representation of the userAuthority.
     */
    public String getRole() {
        return name().replace("ROLE_", "");
    }

    /**
     * Get the userAuthority user-friendly description.
     *
     * @return string representation of the userAuthority.
     */
    public String getName() {
        return name();
    }
}
