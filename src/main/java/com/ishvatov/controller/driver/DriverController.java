package com.ishvatov.controller.driver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Basic regular controller.
 *
 * @author Sergey Khvatov
 */
@Controller
@RequestMapping("employee/driver")
public class DriverController {

    /**
     * Redirects employee to the add new driver page.
     *
     * @return {@link ModelAndView} instance.
     */
    @GetMapping("add")
    public ModelAndView showAddNewDriverPage() {
        return new ModelAndView("employee/driver/add_driver");
    }

    /**
     * Redirects employee to the show driver information page.
     *
     * @param driverUID UID of the requested driver.
     * @return {@link ModelAndView} instance.
     */
    @GetMapping("{uid}/show")
    public ModelAndView showTruckPage(@PathVariable(name = "uid") String driverUID) {
        ModelAndView modelAndView = new ModelAndView("employee/driver/show_driver");
        modelAndView.addObject("driverUID", driverUID);
        return modelAndView;
    }
}
