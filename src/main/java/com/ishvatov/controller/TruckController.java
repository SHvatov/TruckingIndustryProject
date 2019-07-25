package com.ishvatov.controller;

import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.service.truck.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

/**
 * Controller which processes the requests,
 * that manage truck resources.
 *
 * @author Sergey Khvatov
 */
@Controller
public class TruckController {

    /**
     * Truck service object.
     */
    @Autowired
    TruckService service;

    /**
     * Message source object.
     */
    @Autowired
    @Qualifier("setupMessageSource")
    MessageSource messageSource;

    /**
     * Generates model attribute when it is necessary.
     *
     * @return TruckDto object.
     */
    @ModelAttribute("truck")
    public TruckDto formBackingObject() {
        return new TruckDto();
    }

    /**
     * Lists all the trucks in the DB.
     *
     * @param model {@link ModelMap} instance.
     * @return path to the JSP view.
     */
    @RequestMapping(value = "/")
    public String start(ModelMap model) {
        return "start";
    }

    /**
     * Lists all the trucks in the DB.
     *
     * @param model {@link ModelMap} instance.
     * @return path to the JSP view.
     */
    @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
    public String listTrucks(ModelMap model) {
        List<TruckDto> trucks = service.findAll();
        model.addAttribute("trucks", trucks);
        return "display_trucks";
    }

    /**
     * Shows the add page.
     *
     * @param model {@link ModelMap} instance.
     * @return path to the JSP view.
     */
    @RequestMapping(value = {"/new"}, method = RequestMethod.GET)
    public String newEmployee(ModelMap model) {
        TruckDto truck = new TruckDto();
        model.addAttribute("truck", truck);
        return "add_truck";
    }

    /**
     * Adds truck to the DB using the user input.
     *
     * @param truck  Truck model attribute.
     * @param result {@link BindingResult} object.
     * @param model  {@link ModelMap} instance.
     * @return path to the JSP view.
     */
    @RequestMapping(value = {"/new"}, method = RequestMethod.POST)
    public String saveTruck(@ModelAttribute("truck") @Valid TruckDto truck, BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "start";
        }

        if (!service.isUniqueKey(truck.getTruckRegistrationNumber())) {
            FieldError fieldError = new FieldError(
                "truck", "RegistrationNumber",
                messageSource.getMessage("non.unique.key", new String[]{truck.getTruckRegistrationNumber()}, Locale.getDefault()));
            result.addError(fieldError);
            return "start";
        }

        service.save(truck);

        model.addAttribute("success", "Truck " + truck.toString() + " registered successfully");
        return "success";
    }
}
