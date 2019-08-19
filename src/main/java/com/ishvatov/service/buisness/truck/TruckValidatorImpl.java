package com.ishvatov.service.buisness.truck;

import com.ishvatov.service.buisness.response.ServerResponse;
import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.model.entity.enum_types.TruckStatusType;
import com.ishvatov.service.inner.city.CityService;
import com.ishvatov.service.inner.truck.TruckService;
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
@Component("truckValidator")
public class TruckValidatorImpl implements CustomValidator<TruckDto, String> {

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
    private static final int MAX_SHIFT_SIZE = 12;

    /**
     * Maximum size of the drivers shift.
     */
    private static final int MIN_SHIFT_SIZE = 8;

    /**
     * Minimum capacity.
     */
    private static final double MIN_CAPACITY = 10.0;

    /**
     * Maximum capacity.
     */
    private static final double MAX_CAPACITY = 1000.0;

    /**
     * Validates truck entity before deleting it.
     *
     * @param entityUID UID of the truck entity.
     * @param response  Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeLoad(String entityUID, ServerResponse response) {
        if (truckService.exists(entityUID)) {
            return true;
        }
        response.addError("uniqueIdentificator",
            messageSource.getMessage("not.exist.truck", null, Locale.ENGLISH));
        return false;
    }

    /**
     * Validates truck entity before updating its capacity.
     *
     * @param entityDto DTO object.
     * @param response  Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeUpdate(TruckDto entityDto, ServerResponse response) {
        return validateOrder(entityDto.getOrderId(), response)
            && validateCapacity(entityDto.getCapacity(), response)
            && validateShiftSize(entityDto.getShiftSize(), response)
            && validateCondition(entityDto.getStatus(), response)
            && validateCity(entityDto.getCityId(), response);
    }

    /**
     * Validates truck entity before deleting it.
     *
     * @param entityUID UID of the truck entity.
     * @param response  Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeDelete(String entityUID, ServerResponse response) {
        TruckDto dto = truckService.find(entityUID);
        if (dto == null) {
            return true;
        }
        return validateOrder(dto.getOrderId(), response);
    }

    /**
     * Validates user's input before saving new truck entity.
     *
     * @param entityDto DTO object.
     * @param response  Object, which stores server response.
     * @return true, if validation was passed, false otherwise.
     */
    @Override
    public boolean validateBeforeSave(TruckDto entityDto, ServerResponse response) {
        boolean validated = validateUIDBeforeSave(entityDto.getUniqueIdentificator(), response);
        validated &= validateCapacity(entityDto.getCapacity(), response);
        validated &= validateShiftSize(entityDto.getShiftSize(), response);
        validated &= validateCondition(entityDto.getStatus(), response);
        validated &= validateCity(entityDto.getCityId(), response);
        return validated;
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
            response.addError("shiftSize",
                messageSource.getMessage("not.empty.truck.shift", null, Locale.ENGLISH));
            return false;
        } else if (shiftSize < MIN_SHIFT_SIZE || shiftSize > MAX_SHIFT_SIZE) {
            response.addError("shiftSize",
                messageSource.getMessage("incorrect.truck.shift", null, Locale.ENGLISH));
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
            response.addError("capacity",
                messageSource.getMessage("not.empty.truck.capacity", null, Locale.ENGLISH));
            return false;
        } else if (Double.compare(capacity, MIN_CAPACITY) < 0
            || Double.compare(capacity, MAX_CAPACITY) >= 0) {
            response.addError("capacity",
                messageSource.getMessage("incorrect.truck.capacity", null, Locale.ENGLISH));
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
    private boolean validateCondition(TruckStatusType condition, ServerResponse response) {
        if (condition == null) {
            response.addError("status",
                messageSource.getMessage("not.empty.truck.status", null, Locale.ENGLISH));
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
            response.addError("cityId",
                messageSource.getMessage("not.empty.truck.city", null, Locale.ENGLISH));
            return false;
        } else if (!cityService.exists(cityUID)) {
            response.addError("cityId",
                messageSource.getMessage("not.exist.city", null, Locale.ENGLISH));
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
                messageSource.getMessage("not.empty.truck.uid", null, Locale.ENGLISH));
            return false;
        } else if (!uid.matches(UID_PATTERN) || uid.length() != UID_LEN) {
            response.addError("uniqueIdentificator",
                messageSource.getMessage("incorrect.truck.uid", null, Locale.ENGLISH));
            return false;
        } else if (truckService.exists(uid)) {
            response.addError("uniqueIdentificator",
                messageSource.getMessage("exist.truck", null, Locale.ENGLISH));
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
            response.addError("orderId",
                messageSource.getMessage("incorrect.truck.order", null, Locale.ENGLISH));
            return false;
        }
        return true;
    }
}
