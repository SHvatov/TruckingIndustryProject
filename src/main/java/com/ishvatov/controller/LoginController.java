package com.ishvatov.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller is designed to process login requests.
 *
 * @author Sergey Khvatov
 */
@Controller
@Log4j
public class LoginController {

    /**
     * Redirects user to the login page.
     *
     * @return String representation of the source.
     */
    @RequestMapping("/login/login")
    public ModelAndView loginPage() {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return new ModelAndView("login/login_page");
    }

    /**
     * Redirects user to the error page, if login failed.
     *
     * @return String representation of the source.
     */
    @RequestMapping("/login/login_failed")
    public ModelAndView loginFailure() {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return new ModelAndView("login/login_failed");
    }

    /**
     * Redirects employee to the homepage.
     *
     * @return {@link ModelAndView} instance.
     */
    @RequestMapping("/employee/homepage")
    public ModelAndView showEmployeeHomePage() {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        return new ModelAndView("employee/homepage");
    }

    /**
     * Redirects employee to the homepage.
     *
     * @return {@link ModelAndView} instance.
     */
    @RequestMapping("/driver/homepage")
    public ModelAndView showDriverHomePage() {
        // logging
        log.debug("Entering: "
            + getClass() + "."
            + Thread.currentThread()
            .getStackTrace()[1]
            .getMethodName());

        ModelAndView modelAndView = new ModelAndView("driver/homepage");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        modelAndView.addObject("driverUID", currentPrincipalName);
        return modelAndView;
    }
}
