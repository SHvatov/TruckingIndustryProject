package com.ishvatov.mapper;

import com.ishvatov.model.dto.UserDto;
import com.ishvatov.model.entity.buisness.UserEntity;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@link Mapper} interface implementation for user.
 *
 * @author Sergey Khvatov
 */
@Component
public class UserMapperImpl implements Mapper<UserEntity, UserDto> {

    /**
     * Autowired {@link DozerBeanMapper} instance.
     */
    @Autowired
    private DozerBeanMapper mapper;

    /**
     * Maps existing entity object from DB to DTO.
     *
     * @param src entity object.
     * @return new DTO initialized with values from entity.
     */
    @Override
    public UserDto map(UserEntity src) {
        UserDto dest = mapper.map(src, UserDto.class);
        dest.setPassword(null);
        return dest;
    }
}
