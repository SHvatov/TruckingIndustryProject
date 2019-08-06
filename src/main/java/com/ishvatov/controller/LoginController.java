package com.ishvatov.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller is designed to process login requests.
 *
 * @author Sergey Khvatov
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    /**
     * Redirects user to the login page.
     *
     * @return String representation of the source.
     */
    @RequestMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login/login_page");
    }

    /**
     * Redirects user to the error page, if login failed.
     *
     * @return String representation of the source.
     */
    @RequestMapping("/login_failed")
    public ModelAndView loginFailure() {
        return new ModelAndView("login/login_failed");
    }
}
