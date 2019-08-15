package com.ishvatov.controller.order;

import com.ishvatov.controller.response.ServerResponseObject;
import com.ishvatov.model.dto.DriverDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Basic regular controller.
 *
 * @author Sergey Khvatov
 */
@Controller
@RequestMapping("employee/order")
public class OrderController {

    /**
     * Redirects employee to the add new order page.
     *
     * @return {@link ModelAndView} instance.
     */
    @GetMapping("add")
    public ModelAndView showAddNewDriverPage() {
        return new ModelAndView("employee/order/add_order");
    }

    /**
     * Redirects employee to the show order information page.
     *
     * @param orderUID UID of the requested order.
     * @return {@link ModelAndView} instance.
     */
    @GetMapping("{uid}/show")
    public ModelAndView showTruckPage(@PathVariable(name = "uid") String orderUID) {
        ModelAndView modelAndView = new ModelAndView("employee/order/show_order");
        modelAndView.addObject("orderUID", orderUID);
        return modelAndView;
    }
}
