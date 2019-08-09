package com.ishvatov.controller;

import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.service.inner.city.CityService;
import com.ishvatov.service.inner.driver.DriverService;
import com.ishvatov.service.inner.truck.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


/**
 * This controller is designed to process the requests made by the admin.
 *
 * @author Sergey Khvatov
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

    /**
     * Autowired service, used
     * to fetch data from DAO layer.
     */
    @Autowired
    private TruckService truckService;

    /**
     * Autowired service, used
     * to fetch data from DAO layer.
     */
    @Autowired
    private DriverService driverService;

    /**
     * Autowired service, used
     * to fetch data from DAO layer.
     */
    @Autowired
    private CityService cityService;

    /**
     * Truck form input validator.
     */
    @Autowired
    @Qualifier("truckFormValidator")
    private Validator validator;

    /**
     * Adds the validator to the binder.
     *
     * @param binder {@link WebDataBinder} instance.
     */
    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    /**
     * Adds new instance of the DTO objects to the model
     * if it is necessary.
     *
     * @param model object, which implements {@link Model}.
     */
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("truckDto", new TruckDto());
        model.addAttribute("cityList", cityService.getAllCityNames());
        model.addAttribute("driverList", driverService.getAllDriversUID());
    }

    /**
     * Redirects employee to the homepage.
     *
     * @return {@link ModelAndView} instance.
     */
    @RequestMapping("/homepage")
    public ModelAndView showEmployeeHomePage() {
        ModelAndView modelAndView = new ModelAndView("employee/emp_homepage");

        // get username from the security application context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        modelAndView.addObject("user", currentPrincipalName);
        return modelAndView;
    }

    /**
     * Redirects employee to the page with list of all trucks.
     *
     * @return {@link ModelAndView} instance.
     */
    @RequestMapping("/list_trucks")
    public ModelAndView showTrucksPage() {
        ModelAndView modelAndView = new ModelAndView("employee/emp_list_trucks");
        modelAndView.addObject("truckDtoList", truckService.findAll());
        return modelAndView;
    }

    /**
     * Redirects employee to the add truck page.
     *
     * @return {@link ModelAndView} instance.
     */
    @GetMapping("/add_truck")
    public ModelAndView showAddTruckPage() {
        return new ModelAndView("employee/emp_add_truck");
    }

    /**
     * Adds new truck to the database and redirects user to the page
     * with the list of all trucks.
     *
     * @return string representation of the view.
     */
    @PostMapping("/add_truck")
    public String addNewTruck(@ModelAttribute("truckDto") @Validated TruckDto truckDto,
                              BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "employee/emp_add_truck";
        }

        truckService.save(truckDto);
        return "redirect:/employee/list_trucks";
    }
}
