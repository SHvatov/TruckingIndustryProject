package com.ishvatov.model.dao.user;

import com.ishvatov.model.dao.BaseDaoInterface;
import com.ishvatov.model.entity.buisness.UserEntity;

/**
 * Defines basic interface to work with user entities in the database.
 *
 * @author Sergey Khvatov.
 */
public interface UserDao extends BaseDaoInterface<String, UserEntity> {
    // empty interface
}
