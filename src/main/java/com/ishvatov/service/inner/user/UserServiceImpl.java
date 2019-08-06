package com.ishvatov.service.inner.user;

import com.ishvatov.exception.DAOException;
import com.ishvatov.mapper.Mapper;
import com.ishvatov.model.dao.user.UserDao;
import com.ishvatov.model.dto.UserDto;
import com.ishvatov.model.entity.buisness.UserEntity;
import com.ishvatov.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic {@link UserService} interface implementation.
 *
 * @author Sergey Khvatov
 */
@Service("userService")
public class UserServiceImpl extends AbstractService<String, UserEntity, UserDto> implements UserService {

    /**
     * Autowired DAO field.
     */
    private UserDao userDao;

    /**
     * Autowired Encoder field.
     */
    private BCryptPasswordEncoder encoder;

    /**
     * Default class constructor, that is used
     * to inject DAO interface implementation and
     * initialize the super class.
     *
     * @param userDao {@link UserDao} interface implementation.
     * @param encoder {@link BCryptPasswordEncoder} instance.
     * @param mapper  {@link Mapper} implementation.
     */
    @Autowired
    public UserServiceImpl(UserDao userDao, Mapper<UserEntity, UserDto> mapper, BCryptPasswordEncoder encoder) {
        super(userDao, mapper);
        this.userDao = userDao;
        this.encoder = encoder;
    }

    /**
     * Adds entity to the DB. Check if entity already exists.
     *
     * @param dtoObj new entity to add.
     * @throws DAOException if entity with this UID already exists
     */
    @Transactional
    @Override
    public void save(UserDto dtoObj) {
        if (userDao.exists(dtoObj.getUniqueIdentificator())) {
            throw new DAOException(getClass(), "save", "Entity with such UID already exists");
        } else {
            UserEntity entity = new UserEntity();
            entity.setUniqueIdentificator(dtoObj.getUniqueIdentificator());
            entity.setPassword(dtoObj.getPassword());
            entity.setAuthority(dtoObj.getAuthority());
            userDao.save(entity);
        }
    }

    /**
     * Updates password of the user.
     *
     * @param userUID  UID of the user.
     * @param password new password of the user.
     * @throws DAOException if no such entity exists.
     */
    @Transactional
    @Override
    public void updatePassword(String userUID, String password) {
        UserEntity entity = userDao.findByUniqueKey(userUID);
        if (entity == null) {
            throw new DAOException(getClass(), "updatePassword", "Entity with such UID does not exist");
        } else {
            entity.setPassword(encoder.encode(password));
        }
    }
}
