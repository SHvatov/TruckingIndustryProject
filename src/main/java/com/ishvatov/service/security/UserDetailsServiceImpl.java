package com.ishvatov.service.security;

import com.ishvatov.model.dao.user.UserDao;
import com.ishvatov.model.entity.security.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link UserDetailsService} interface.
 *
 * @author Sergey Khvatov
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * DAO used in this service
     * to access the DB.
     */
    @Autowired
    private UserDao userDao;

    /**
     * Load user into the system by his / her unique id.
     *
     * @param userUniqueId unique id of the user.
     * @return {@link UserDetails} instance with the details about new user.
     * @throws UsernameNotFoundException if user's unique id was not found in the DB.
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String userUniqueId) {
        // find user entity
        UserEntity user = userDao.findByUniqueKey(userUniqueId);
        // if it was found
        if (user != null) {
            return new UserAuthCredentials(user);
        } else {
            throw new UsernameNotFoundException("User with id: [" + userUniqueId + "] was not found.");
        }
    }
}
