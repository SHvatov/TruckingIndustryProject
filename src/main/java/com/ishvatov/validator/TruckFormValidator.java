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
     * Implements the validation process.
     *
     * @param o      Object to validate.
     * @param errors {@link Errors} interface implementation.
     */
    @Override
    public void validate(Object o, Errors errors) {
        // check the validation  input
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "uid", "NotEmpty.user.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "capacity", "NotEmpty.user.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shift_size", "NotEmpty.user.address");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "condition", "NotEmpty.user.password");

        TruckDto dto = (TruckDto) o;
        if (dto.getUniqueIdentificator() == null || dto.getUniqueIdentificator().isEmpty()) {
            errors.rejectValue("uid", "NotEmpty.truck.uid");
        }

        if (!dto.getUniqueIdentificator().matches(UID_PATTERN) || dto.getUniqueIdentificator().length() != UID_LEN) {
            errors.rejectValue("uid", "Incorrect.truck.uid");
        }

        if (truckService.exists(dto.getUniqueIdentificator())) {
            errors.rejectValue("uid", "NotUnique.truck.uid");
        }

        if (dto.getTruckCapacity() == null) {
            errors.rejectValue("capacity", "NotEmpty.truck.capacity");
        }

        if (Double.compare(dto.getTruckCapacity(), 0) <= 0) {
            errors.rejectValue("capacity", "Incorrect.truck.capacity");
        }

        if (dto.getTruckDriverShiftSize() == null) {
            errors.rejectValue("shift_size", "NotEmpty.truck.shift_size");
        }

        if (dto.getTruckDriverShiftSize() <= 0 || dto.getTruckDriverShiftSize() > MAX_SHIFT_SIZE) {
            errors.rejectValue("shift_size", "NotEmpty.truck.shift_size");
        }

        if (dto.getTruckCondition() == null) {
            errors.rejectValue("condition", "NotEmpty.truck.condition");
        }

        if (dto.getTruckCityUID() != null && !cityService.exists(dto.getTruckCityUID())) {
            errors.rejectValue("city", "NotExist.truck.city");
        }

        if (dto.getTruckOrderUID() != null && !orderService.exists(dto.getTruckOrderUID())) {
            errors.rejectValue("order", "NotExist.truck.order");
        }

        if (dto.getTruckDriverUIDSet() != null && !dto.getTruckDriverUIDSet().isEmpty()) {
            for (String driverUID: dto.getTruckDriverUIDSet()) {
                if (!driverService.exists(driverUID)) {
                    errors.rejectValue("drivers", "NotExist.truck.driver");
                }
            }
        }
    }
}

