package com.ishvatov.validator;

import com.ishvatov.controller.response.ServerResponse;
import com.ishvatov.model.dto.DriverDto;
import com.ishvatov.service.inner.city.CityService;
import com.ishvatov.service.inner.driver.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * {@link CustomValidator} implementation class.
 *
 * @author Sergey Khvatov
 */
@Component("driverValidator")
public class DriverValidatorImpl implements CustomValidator<DriverDto, String> {

    /**
     * Min length of the UID.
     */
    private static final int MIN_UID_LEN = 5;

    /**
     * Max length of the UID.
     */
    private static final int MAX_UID_LEN = 20;

    /**
     * Min len of the password.
     */
    private static final int MIN_PASSWORD_LEN = 5;

    /**
     * Max len of the password.
     */
    private static final int MAX_PASSWORD_LEN = 15;

    /**
     * Autowired message source.
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * Autowired service.
     */
    @Autowired
    private CityService cityService;

    /**
     * Autowired service.
     */
    @Autowired
    private DriverService driverService;

    /**
     * Validates entity before loading it.
     *
     * @param entityUID UID of the entity.
     * @param response  Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeLoad(String entityUID, ServerResponse response) {
        if (driverService.exists(entityUID)) {
            return true;
        }
        response.addError("uniqueIdentificator",
            messageSource.getMessage("NotExist.driver", null, Locale.ENGLISH));
        return false;
    }

    /**
     * Validates user's input before saving new entity.
     *
     * @param entityDto DTO object.
     * @param response  Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeSave(DriverDto entityDto, ServerResponse response) {
        boolean validated = validateUIDBeforeSave(entityDto.getUniqueIdentificator(), response);
        validated &= validateName(entityDto.getDriverName(), response);
        validated &= validateName(entityDto.getDriverSurname(), response);
        validated &= validatePassword(entityDto.getDriverPassword(), response);
        validated &= validateCity(entityDto.getCurrentCityUID(), response);
        return validated;
    }

    /**
     * Validates entity before updating its capacity.
     *
     * @param entityDto DTO object.
     * @param response  Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeUpdate(DriverDto entityDto, ServerResponse response) {
        return validateOrder(entityDto.getDriverOrderUID(), response)
            &&validateName(entityDto.getDriverName(), response)
            && validateName(entityDto.getDriverSurname(), response)
            && validateCity(entityDto.getCurrentCityUID(), response);
    }

    /**
     * Validates entity before deleting it.
     *
     * @param entityUID UID of the entity.
     * @param response  Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeDelete(String entityUID, ServerResponse response) {
        DriverDto dto = driverService.find(entityUID);
        if (dto == null) {
            return true;
        }
        return validateOrder(dto.getDriverOrderUID(), response);
    }

    /**
     * Validates input value of the password.
     *
     * @param password new password.
     * @param response Object, which stores server response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean validatePassword(String password, ServerResponse response) {
        if (password == null || password.isEmpty()) {
            response.addError("driverPassword",
                messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
            return false;
        } else if (password.length() < MIN_PASSWORD_LEN || password.length() > MAX_PASSWORD_LEN) {
            response.addError("driverPassword",
                messageSource.getMessage("Incorrect.password", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }

    /**
     * Validates input value of the city.
     *
     * @param cityUID  new city.
     * @param response Object, which stores server response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean validateCity(String cityUID, ServerResponse response) {
        if (cityUID == null) {
            response.addError("currentCityUID",
                messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
            return false;
        } else if (!cityService.exists(cityUID)) {
            response.addError("currentCityUID",
                messageSource.getMessage("NotExist.city", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }

    /**
     * Validates input value of the name.
     *
     * @param name     new name.
     * @param response Object, which stores server response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean validateName(String name, ServerResponse response) {
        if (name == null || name.isEmpty()) {
            response.addError("driverName",
                messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }

    /**
     * Validates UID before saving.
     *
     * @param uid      new UID.
     * @param response Object, which stores server response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean validateUIDBeforeSave(String uid, ServerResponse response) {
        if (uid == null || uid.isEmpty()) {
            response.addError("uniqueIdentificator",
                messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
            return false;
        } else if (uid.length() < MIN_UID_LEN || uid.length() > MAX_UID_LEN) {
            response.addError("uniqueIdentificator",
                messageSource.getMessage("Incorrect.driver.uid", null, Locale.ENGLISH));
            return false;
        } else if (driverService.exists(uid)) {
            response.addError("uniqueIdentificator",
                messageSource.getMessage("NotUnique.driver", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }

    /**
     * Validates input value of the shift size.
     *
     * @param orderUID UID of the order.
     * @param response Object, which stores server response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean validateOrder(String orderUID, ServerResponse response) {
        if (orderUID != null) {
            response.addError("driverOrderUID",
                messageSource.getMessage("Incorrect.truck.has_order", null,
                Locale.ENGLISH));
            return false;
        }
        return true;
    }
}
