package com.ishvatov.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This controller is designed to process login requests.
 *
 * @author Sergey Khvatov
 */
@Controller
public class LoginController {

    /**
     * Redirects user to the login page.
     *
     * @return String representation of the source.
     */
    @RequestMapping("/login/login")
    public String loginPage() {
        return "login/login_page";
    }

    /**
     * Redirects user to the error page, if login failed.
     *
     * @return String representation of the source.
     */
    @RequestMapping("login/login_failed")
    public String loginFailure() {
        return "login/login_failed";
    }

    /**
     * Redirects employee to the employee homepage.
     *
     * @return String representation of the source.
     */
    @RequestMapping("/employee/homepage")
    public String adminHomepage() {
        return "employee/homepage";
    }

    /**
     * Redirects admin to the admin homepage.
     *
     * @return String representation of the source.
     */
    @RequestMapping("/admin/homepage")
    public String userHomepage() {
        return "admin/homepage";
    }

    /**
     * Redirects driver to the driver homepage.
     *
     * @return String representation of the source.
     */
    @RequestMapping("/driver/homepage")
    public String driverHomepage() {
        return "driver/homepage";
    }
}
