package com.ishvatov.service.security;

import com.ishvatov.model.entity.buisness.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Basic class, that stores the authentication credentials of the user,
 * such as userAuthority, id, userPassword and etc. Implements {@link UserDetails} interface.
 *
 * @author Sergey Khvatov
 */
@Data
@AllArgsConstructor
public class UserAuthCredentials implements UserDetails {

    /**
     * User unique id.
     */
    private final String userIdentificator;

    /**
     * User userAuthority in the system.
     */
    private final String userAuthority;

    /**
     * User's userPassword.
     */
    private final String userPassword;

    /**
     * Default class constructor from values, stored in the
     * {@link UserEntity} object from database.
     *
     * @param dbUserEntity database entity with user's credentials.
     */
    public UserAuthCredentials(UserEntity dbUserEntity) {
        this(dbUserEntity.getUniqueIdentificator(),
            dbUserEntity.getAuthority().getName(),
            dbUserEntity.getPassword()
        );
    }

    /**
     * Gets all the list with all authorities of the user.
     *
     * @return list with one element - userAuthority of the user in the system.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userAuthority));
        return authorities;
    }

    /**
     * Get the userPassword of the user method.
     *
     * @return userPassword of the user.
     */
    @Override
    public String getPassword() {
        return userPassword;
    }

    /**
     * Get the username of the user method.
     *
     * @return unique id of the user.
     */
    @Override
    public String getUsername() {
        return userIdentificator;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
