package com.ishvatov.model.dao.user;

import com.ishvatov.model.dao.BaseDaoInterface;
import com.ishvatov.model.entity.security.UserEntity;

/**
 * Basic interface, which is used to retrieve user data
 * from the data base.
 *
 * @author Sergey Khvatov
 */
public interface UserDao extends BaseDaoInterface<String, UserEntity> {
    // empty interface
}
