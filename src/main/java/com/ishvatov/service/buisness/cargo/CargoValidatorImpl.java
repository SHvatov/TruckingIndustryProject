package com.ishvatov.service.buisness.cargo;

import com.ishvatov.service.buisness.response.ServerResponse;
import com.ishvatov.model.dto.CargoDto;
import com.ishvatov.service.inner.cargo.CargoService;
import com.ishvatov.service.buisness.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * {@link CustomValidator} implementation class.
 *
 * @author Sergey Khvatov
 */
@Component("cargoValidator")
public class CargoValidatorImpl implements CustomValidator<CargoDto, Integer> {

    /**
     * Max mass value of the cargo.
     */
    private static final double MAX_MASS = 500.0;

    /**
     * Min mass value of the cargo.
     */
    private static final double MIN_MASS = 0.01;

    /**
     * Min length of the cargo name.
     */
    private static final int MIN_NAME_LEN = 5;

    /**
     * Max length of the cargo name.
     */
    private static final int MAX_NAME_LEN = 10;

    /**
     * Autowired message source.
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * Autowired service.
     */
    @Autowired
    private CargoService cargoService;

    /**
     * Validates entity before loading it.
     *
     * @param entityUID UID of the entity.
     * @param response  Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeLoad(Integer entityUID, ServerResponse response) {
        if (cargoService.exists(entityUID)) {
            return true;
        }
        response.addError("id",
            messageSource.getMessage("not.exist.truck", null, Locale.ENGLISH));
        return false;
    }

    /**
     * Validates entity before updating its capacity.
     *
     * @param entityDto DTO object.
     * @param response  Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeUpdate(CargoDto entityDto, ServerResponse response) {
        if (cargoService.hasOrder(entityDto.getId())) {
            response.addError("error",
                messageSource.getMessage("incorrect.cargo.order", null, Locale.ENGLISH));
            return false;
        }
        return validateName(entityDto.getName(), response)
            && validateMass(entityDto.getMass(), response);
    }

    /**
     * Validates entity before deleting it.
     *
     * @param entityUID UID of the entity.
     * @param response  Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeDelete(Integer entityUID, ServerResponse response) {
        CargoDto dto = cargoService.find(entityUID);
        if (dto == null) {
            return true;
        } else if (cargoService.hasOrder(entityUID)) {
            response.addError("error",
                messageSource.getMessage("incorrect.cargo.order", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }

    /**
     * Validates user's input before saving new entity.
     *
     * @param entityDto DTO object.
     * @param response  Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeSave(CargoDto entityDto, ServerResponse response) {
        boolean validated = validateName(entityDto.getName(), response);
        validated &= validateMass(entityDto.getMass(), response);
        return validated;
    }

    /**
     * Validates user's input value for the name field.
     *
     * @param name     new name.
     * @param response Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    private boolean validateName(String name, ServerResponse response) {
        if (name == null || name.isEmpty()) {
            response.addError("name",
                messageSource.getMessage("not.empty.cargo.name", null, Locale.ENGLISH));
            return false;
        } else if (name.length() < MIN_NAME_LEN || name.length() > MAX_NAME_LEN) {
            response.addError("name",
                messageSource.getMessage("incorrect.cargo.name", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }

    /**
     * Validates user's input value for the mass field.
     *
     * @param mass     new mass.
     * @param response Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    private boolean validateMass(Double mass, ServerResponse response) {
        if (mass == null) {
            response.addError("mass",
                messageSource.getMessage("not.empty.cargo.mass", null, Locale.ENGLISH));
            return false;
        } else if (Double.compare(mass, MIN_MASS) < 0 || Double.compare(mass, MAX_MASS) > 0) {
            response.addError("mass",
                messageSource.getMessage("incorrect.cargo.mass", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }
}
