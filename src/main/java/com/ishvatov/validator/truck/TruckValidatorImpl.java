package com.ishvatov.validator.truck;

import com.ishvatov.controller.response.ServerResponse;
import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.model.entity.enum_types.TruckConditionType;
import com.ishvatov.service.inner.city.CityService;
import com.ishvatov.service.inner.truck.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * {@link TruckValidator} implementation class.
 *
 * @author Sergey Khvatov
 */
@Component("truckValidator")
public class TruckValidatorImpl implements TruckValidator {

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
    private TruckService truckService;

    /**
     * UID pattern.
     */
    private static final String UID_PATTERN = "[a-zA-Z]{2}[0-9]{5}";

    /**
     * Length of the UID.
     */
    private static final int UID_LEN = 7;

    /**
     * Maximum size of the drivers shift.
     */
    private static final int MAX_SHIFT_SIZE = 10;

    /**
     * Maximum size of the drivers shift.
     */
    private static final int MIN_SHIFT_SIZE = 6;

    /**
     * Validates truck entity before deleting it.
     *
     * @param truckUID UID of the truck entity.
     * @param response Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeLoad(String truckUID, ServerResponse response) {
        if (truckService.exists(truckUID)) {
            return true;
        }
        response.addError("uniqueIdentificator", messageSource.getMessage("NotExist.truck", null, Locale.ENGLISH));
        return false;
    }

    /**
     * Validates truck entity before updating its capacity.
     *
     * @param truckDto DTO object.
     * @param response Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeUpdate(TruckDto truckDto, ServerResponse response) {
        boolean valiadted = validateUIDBeforeUpdate(truckDto.getUniqueIdentificator(), response);
        valiadted &= validateCapacity(truckDto.getTruckCapacity(), response);
        valiadted &= validateShiftSize(truckDto.getTruckDriverShiftSize(), response);
        valiadted &= validateCondition(truckDto.getTruckCondition(), response);
        valiadted &= validateCity(truckDto.getTruckCityUID(), response);
        valiadted &= validateOrder(truckDto.getTruckOrderUID(), response);
        return valiadted;
    }

    /**
     * Validates truck entity before deleting it.
     *
     * @param truckUID UID of the truck entity.
     * @param response Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeDelete(String truckUID, ServerResponse response) {
        TruckDto dto = truckService.find(truckUID);
        if (dto == null) {
            return true;
        }
        return validateOrder(dto.getTruckOrderUID(), response);
    }

    /**
     * Validates user's input before saving new truck entity.
     *
     * @param truckDto DTO object.
     * @param response Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeSave(TruckDto truckDto, ServerResponse response) {
        boolean valiadted = validateUIDBeforeSave(truckDto.getUniqueIdentificator(), response);
        valiadted &= validateCapacity(truckDto.getTruckCapacity(), response);
        valiadted &= validateShiftSize(truckDto.getTruckDriverShiftSize(), response);
        valiadted &= validateCondition(truckDto.getTruckCondition(), response);
        valiadted &= validateCity(truckDto.getTruckCityUID(), response);
        return valiadted;
    }

    /**
     * Validates input value of the shift size.
     *
     * @param shiftSize new shift size.
     * @param response  Object, which stores server response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean validateShiftSize(Integer shiftSize, ServerResponse response) {
        if (shiftSize == null) {
            response.addError("truckDriverShiftSize", messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
            return false;
        } else if (shiftSize < MIN_SHIFT_SIZE || shiftSize > MAX_SHIFT_SIZE) {
            response.addError("truckDriverShiftSize", messageSource.getMessage("Incorrect.truck.shift_size", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }

    /**
     * Validates input value of the capacity.
     *
     * @param capacity new capacity.
     * @param response Object, which stores server response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean validateCapacity(Double capacity, ServerResponse response) {
        if (capacity == null) {
            response.addError("truckCapacity", messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
            return false;
        } else if (Double.compare(capacity, 0.0) <= 0 || Double.compare(capacity, Double.MAX_VALUE) >= 0) {
            response.addError("truckCapacity", messageSource.getMessage("Incorrect.truck.capacity", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }

    /**
     * Validates input value of the condition.
     *
     * @param condition new condition.
     * @param response  Object, which stores server response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean validateCondition(TruckConditionType condition, ServerResponse response) {
        if (condition == null) {
            response.addError("truckCondition", messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
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
            response.addError("truckCityUID", messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
            return false;
        } else if (!cityService.exists(cityUID)) {
            response.addError("truckCityUID", messageSource.getMessage("NotExist.city", null, Locale.ENGLISH));
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
            response.addError("uniqueIdentificator", messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
            return false;
        } else if (!uid.matches(UID_PATTERN) || uid.length() != UID_LEN) {
            response.addError("uniqueIdentificator", messageSource.getMessage("Incorrect.truck.uid", null, Locale.ENGLISH));
            return false;
        } else if (truckService.exists(uid)) {
            response.addError("uniqueIdentificator", messageSource.getMessage("NotUnique.truck", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }

    /**
     * Validates UID before updating.
     *
     * @param uid      existing UID.
     * @param response Object, which stores server response.
     * @return true, if validation was successful, false otherwise.
     */
    private boolean validateUIDBeforeUpdate(String uid, ServerResponse response) {
        if (uid == null || uid.isEmpty()) {
            response.addError("uniqueIdentificator", messageSource.getMessage("NotEmpty.field", null, Locale.ENGLISH));
            return false;
        } else if (!truckService.exists(uid)) {
            response.addError("uniqueIdentificator", messageSource.getMessage("NotExist.truck", null, Locale.ENGLISH));
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
            response.addError("truckOrderUID", messageSource.getMessage("Business.truck.has_order", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }
}