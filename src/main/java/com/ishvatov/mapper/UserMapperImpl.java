package com.ishvatov.mapper;

import com.ishvatov.model.dto.UserDto;
import com.ishvatov.model.entity.buisness.UserEntity;
import org.springframework.stereotype.Component;

/**
 * {@link Mapper} interface implementation for user.
 *
 * @author Sergey Khvatov
 */
@Component
public class UserMapperImpl implements Mapper<UserEntity, UserDto> {

    /**
     * Maps existing entity object from DB to DTO.
     *
     * @param src entity object.
     * @return new DTO initialized with values from entity.
     */
    @Override
    public UserDto map(UserEntity src) {
        UserDto dest = new UserDto();
        dest.setUniqueIdentificator(src.getUniqueIdentificator());
        dest.setAuthority(src.getAuthority());
        dest.setPassword(null);
        return dest;
    }
}
