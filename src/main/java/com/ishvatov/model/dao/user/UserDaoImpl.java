package com.ishvatov.model.dao.user;

import com.ishvatov.model.dao.AbstractDao;
import com.ishvatov.model.entity.buisness.UserEntity;
import org.springframework.stereotype.Repository;

/**
 * {@link UserDao} implementation.
 *
 * @author Sergey Khvatov
 */
@Repository("userDao")
public class UserDaoImpl extends AbstractDao<String, UserEntity> implements UserDao {
    // no implementation
}
