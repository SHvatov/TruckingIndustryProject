package com.ishvatov.service.inner.user;

import com.ishvatov.exception.DAOException;
import com.ishvatov.exception.ValidationException;
import com.ishvatov.mapper.Mapper;
import com.ishvatov.model.dao.user.UserDao;
import com.ishvatov.model.dto.UserDto;
import com.ishvatov.model.entity.buisness.UserEntity;
import com.ishvatov.service.inner.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Basic {@link UserService} interface implementation.
 *
 * @author Sergey Khvatov
 */
@Service("userService")
@Transactional
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
    @Override
    public void save(UserDto dtoObj) {
        validateRequiredFields(dtoObj);
        if (exists(dtoObj.getUniqueIdentificator())) {
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
     * Updates data in the database. Updates
     * entity fields using all not-null fields from the
     * DTO object.
     *
     * @param dtoObj values to update in the entity.
     * @throws DAOException if entity with this UID already exists
     */
    @Override
    public void update(UserDto dtoObj) {
        validateRequiredFields(dtoObj);
        UserEntity userEntity = Optional.ofNullable(userDao.findByUniqueKey(dtoObj.getUniqueIdentificator()))
            .orElseThrow(() -> new DAOException(getClass(), "update", "Entity with such UID does not exist"));
        userEntity.setPassword(encoder.encode(dtoObj.getPassword()));
    }

    /**
     * Deletes entity from the DB if it exists.
     *
     * @param key UID of the entity.
     */
    @Override
    public void delete(String key) {
        Optional<UserEntity> userEntity = Optional.ofNullable(
            userDao.findByUniqueKey(
                Optional.ofNullable(key)
                    .orElseThrow(() -> new ValidationException(getClass(), "find", "Key is null"))
            )
        );

        userEntity.ifPresent(e -> userDao.delete(e));
    }

    /**
     * Validates the input DTO object and throws NPE,
     * if object or one of required fields is null.
     *
     * @param dto DTO object.
     */
    private void validateRequiredFields(UserDto dto) {
        if (dto == null || dto.getPassword() == null
        || dto.getAuthority() == null || dto.getUniqueIdentificator() == null) {
            throw new ValidationException();
        }
    }
}
