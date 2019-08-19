package com.ishvatov.service.buisness.driver;

import com.ishvatov.service.buisness.response.ServerResponse;
import com.ishvatov.model.dto.DriverDto;
import com.ishvatov.service.buisness.CustomValidator;
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
     * Min length of the Id.
     */
    private static final int MIN_UID_LEN = 5;

    /**
     * Max length of the Id.
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
     * @param driverId Id of the entity.
     * @param response Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeLoad(String driverId, ServerResponse response) {
        if (driverService.exists(driverId)) {
            return true;
        }
        response.addError("uniqueIdentificator",
            messageSource.getMessage("not.exist.driver", null, Locale.ENGLISH));
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
        boolean validated = validateIdBeforeSave(entityDto.getUniqueIdentificator(), response);
        validated &= validateName(entityDto.getName(), response, "name");
        validated &= validateName(entityDto.getSurname(), response, "surname");
        validated &= validatePassword(entityDto.getPassword(), response);
        validated &= validateCity(entityDto.getCityId(), response);
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
        return validateOrder(entityDto.getOrderId(), response)
            && validateName(entityDto.getName(), response, "name")
            && validateName(entityDto.getSurname(), response, "surname")
            && validateCity(entityDto.getCityId(), response);
    }

    /**
     * Validates entity before deleting it.
     *
     * @param driverId Id of the entity.
     * @param response Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeDelete(String driverId, ServerResponse response) {
        DriverDto dto = driverService.find(driverId);
        if (dto == null) {
            return true;
        }
        return validateOrder(dto.getOrderId(), response);
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
            response.addError("password",
                messageSource.getMessage("not.empty.driver.password", null, Locale.ENGLISH));
            return false;
        } else if (password.length() < MIN_PASSWORD_LEN
            || password.length() > MAX_PASSWORD_LEN) {
            response.addError("password",
                messageSource.getMessage("incorrect.driver.password", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }

    /**
     * Validates input value of the city.
     *
     * @param cityId   new city.
     * @param response Object, which stores server response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean validateCity(String cityId, ServerResponse response) {
        if (cityId == null) {
            response.addError("cityId",
                messageSource.getMessage("not.empty.driver.city", null, Locale.ENGLISH));
            return false;
        } else if (!cityService.exists(cityId)) {
            response.addError("cityId",
                messageSource.getMessage("not.exist.city", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }

    /**
     * Validates input value of the name.
     *
     * @param name     new name.
     * @param response Object, which stores server response.
     * @param field    Name of the validating field.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean validateName(String name, ServerResponse response, String field) {
        if (name == null || name.isEmpty()) {
            response.addError(field,
                messageSource.getMessage("incorrect.driver.name", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }

    /**
     * Validates Id before saving.
     *
     * @param uid      new Id.
     * @param response Object, which stores server response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean validateIdBeforeSave(String uid, ServerResponse response) {
        if (uid == null || uid.isEmpty()) {
            response.addError("uniqueIdentificator",
                messageSource.getMessage("not.empty.driver.uid", null, Locale.ENGLISH));
            return false;
        } else if (uid.length() < MIN_UID_LEN || uid.length() > MAX_UID_LEN) {
            response.addError("uniqueIdentificator",
                messageSource.getMessage("incorrect.driver.uid", null, Locale.ENGLISH));
            return false;
        } else if (driverService.exists(uid)) {
            response.addError("uniqueIdentificator",
                messageSource.getMessage("exist.driver", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }

    /**
     * Validates input value of the shift size.
     *
     * @param orderId  Id of the order.
     * @param response Object, which stores server response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean validateOrder(String orderId, ServerResponse response) {
        if (orderId != null) {
            response.addError("orderId",
                messageSource.getMessage("incorrect.driver.order", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }
}
