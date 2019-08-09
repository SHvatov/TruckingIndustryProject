package com.ishvatov.validator;

import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.service.inner.city.CityService;
import com.ishvatov.service.inner.driver.DriverService;
import com.ishvatov.service.inner.order.OrderService;
import com.ishvatov.service.inner.truck.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Set;

/**
 * Basic validator, used to validate input from truck update and save s.
 *
 * @author Sergey Khvatov
 */
@Component("truckFormValidator")
public class TruckFormValidator implements Validator {

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
     * Autowired service.
     */
    @Autowired
    private OrderService orderService;

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
    private static final int MAX_SHIFT_SIZE = 8;

    /**
     * Maximum number of the drivers in the truck.
     */
    private static final int MAX_DRIVER_NUM = 2;

    /**
     * Checks if this validator supports validation of
     * objects, that are instances of aClass.
     *
     * @param aClass class of the object.
     * @return true, class is TruckDto, false otherwise.
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(TruckDto.class);
    }

    /**
     * Implements the validation of the {@link TruckDto} process.
     *
     * @param o      Object to validate.
     * @param errors {@link Errors} interface implementation.
     */
    @Override
    public void validate(Object o, Errors errors) {
        // check the validation  input
        validateRequiredFields(errors);

        if (o != null) {
            TruckDto dto = (TruckDto) o;

            // check uid
            validateUID(dto.getUniqueIdentificator(), errors);

            // check capacity
            validateCapacity(dto.getTruckCapacity(), errors);

            // check shift size
            validateShiftSize(dto.getTruckDriverShiftSize(), errors);

            // check condition
            if (dto.getTruckCondition() == null) {
                errors.rejectValue("truckCondition", "NotEmpty.field");
            }

            // check city
            if (dto.getTruckCityUID() != null && !cityService.exists(dto.getTruckCityUID())) {
                errors.rejectValue("truckCityUID", "NotExist.city");
            }

            // check order
            if (dto.getTruckOrderUID() != null && !orderService.exists(dto.getTruckOrderUID())) {
                errors.rejectValue("truckOrderUID", "NotExist.order");
            }

            // check driver set
            validateDriverSet(dto.getTruckDriverUIDSet(), errors);
        }
    }

    /**
     * Checks if required fields are empty.
     *
     * @param errors {@link Errors} interface implementation.
     */
    private void validateRequiredFields(Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "uniqueIdentificator", "NotEmpty.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "truckCapacity", "NotEmpty.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "truckDriverShiftSize", "NotEmpty.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "truckCondition", "NotEmpty.field");
    }

    /**
     * Checks the uid of the object.
     *
     * @param uid    UID of the object.
     * @param errors {@link Errors} interface implementation.
     */
    private void validateUID(String uid, Errors errors) {
        if (uid == null || uid.isEmpty()) {
            errors.rejectValue("uniqueIdentificator", "NotEmpty.field");
        } else {
            if (!uid.matches(UID_PATTERN) || uid.length() != UID_LEN) {
                errors.rejectValue("uniqueIdentificator", "Incorrect.truck.uid");
            }

            if (truckService.exists(uid)) {
                errors.rejectValue("uniqueIdentificator", "NotUnique.truck");
            }
        }
    }

    /**
     * Checks the capacity of the truck.
     *
     * @param capacity input capacity.
     * @param errors   {@link Errors} interface implementation.
     */
    private void validateCapacity(Double capacity, Errors errors) {
        if (capacity == null) {
            errors.rejectValue("truckCapacity", "NotEmpty.field");
        } else if (Double.compare(capacity, 0) <= 0) {
            errors.rejectValue("truckCapacity", "Incorrect.truck.capacity");
        }
    }

    /**
     * Checks the shift size.
     *
     * @param shiftSize input shift size
     * @param errors    {@link Errors} interface implementation.
     */
    private void validateShiftSize(Integer shiftSize, Errors errors) {
        if (shiftSize == null) {
            errors.rejectValue("truckDriverShiftSize", "NotEmpty.field");
        } else if (shiftSize <= 0 || shiftSize > MAX_SHIFT_SIZE) {
            errors.rejectValue("truckDriverShiftSize", "Incorrect.truck.shift_size");
        }
    }

    /**
     * Checks the set of drivers' UID.
     *
     * @param driverUIDSet set of drivers' UID.
     * @param errors       {@link Errors} interface implementation.
     */
    private void validateDriverSet(Set<String> driverUIDSet, Errors errors) {
        if (driverUIDSet != null && !driverUIDSet.isEmpty()) {
            if (driverUIDSet.size() > MAX_DRIVER_NUM) {
                errors.rejectValue("truckDriverUIDSet", "Incorrect.truck.drivers_number");
            }

            for (String driverUID : driverUIDSet) {
                if (driverUID != null && !driverService.exists(driverUID)) {
                    errors.rejectValue("truckDriverUIDSet", "NotExist.driver");
                }
            }
        }
    }
}

