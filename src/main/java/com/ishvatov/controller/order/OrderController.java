package com.ishvatov.controller.order;

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
@RequestMapping("employee/order")
@Log4j
public class OrderController {

    /**
     * Redirects employee to the add new order page.
     *
     * @return {@link ModelAndView} instance.
     */
    @GetMapping("add")
    public ModelAndView showAddNewOrderPage() {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());
        
        return new ModelAndView("employee/order/add_order");
    }

    /**
     * Redirects employee to the show order information page.
     *
     * @param orderId UID of the requested order.
     * @return {@link ModelAndView} instance.
     */
    @GetMapping("{uid}/show")
    public ModelAndView showOrderPage(@PathVariable(name = "uid") String orderId) {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        ModelAndView modelAndView = new ModelAndView("employee/order/show_order");
        modelAndView.addObject("orderId", orderId);
        return modelAndView;
    }
}
