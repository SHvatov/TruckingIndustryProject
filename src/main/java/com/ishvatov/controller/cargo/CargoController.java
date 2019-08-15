package com.ishvatov.controller.cargo;

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
@RequestMapping("employee/cargo")
public class CargoController {

    /**
     * Redirects employee to the add new cargo page.
     *
     * @return {@link ModelAndView} instance.
     */
    @GetMapping("add")
    public ModelAndView showAddNewDriverPage() {
        return new ModelAndView("employee/cargo/add_cargo");
    }

    /**
     * Redirects employee to the show cargo information page.
     *
     * @param cargoUID UID of the requested cargo.
     * @return {@link ModelAndView} instance.
     */
    @GetMapping("{uid}/show")
    public ModelAndView showTruckPage(@PathVariable(name = "uid") Integer cargoUID) {
        ModelAndView modelAndView = new ModelAndView("employee/cargo/show_cargo");
        modelAndView.addObject("cargoUID", cargoUID);
        return modelAndView;
    }
}
