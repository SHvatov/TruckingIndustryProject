package com.ishvatov.controller.truck;

import lombok.extern.log4j.Log4j;
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
@RequestMapping("employee/truck")
@Log4j
public class TruckController {

    /**
     * Redirects employee to the add new truck page.
     *
     * @return {@link ModelAndView} instance.
     */
    @GetMapping("add")
    public ModelAndView showAddNewTruckPage() {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return new ModelAndView("employee/truck/add_truck");
    }

    /**
     * Redirects employee to the show truck information page.
     *
     * @param truckId UID of the requested truck.
     * @return {@link ModelAndView} instance.
     */
    @GetMapping("{uid}/show")
    public ModelAndView showTruckPage(@PathVariable(name = "uid") String truckId) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        ModelAndView modelAndView = new ModelAndView("employee/truck/show_truck");
        modelAndView.addObject("truckId", truckId);
        return modelAndView;
    }
}
