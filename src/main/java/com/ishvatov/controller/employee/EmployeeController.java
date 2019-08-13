package com.ishvatov.controller.employee;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
     * Redirects employee to the homepage.
     *
     * @return {@link ModelAndView} instance.
     */
    @RequestMapping("/homepage")
    public ModelAndView showEmployeeHomePage() {
        return new ModelAndView("employee/homepage");
    }
}
