package com.ishvatov.controller;

import com.ishvatov.model.dto.UserDto;
import com.ishvatov.model.entity.enum_types.UserRoleType;
import com.ishvatov.service.inner.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller is designed to process the requests made by the admin.
 *
 * @author Sergey Khvatov
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    /**
     * User service autowired instance.
     */
    @Autowired
    private UserService userService;

    /**
     * Adds new instance of the DTO objects to the model
     * if it is necessary.
     *
     * @param model object, which implements {@link Model}.
     */
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("userDto", new UserDto());
    }

    /**
     * Redirects admin to the admin homepage.
     *
     * @return {@link ModelAndView} instance.
     */
    @RequestMapping("/homepage")
    public ModelAndView redirectToAdminHomePage() {
        ModelAndView modelAndView = new ModelAndView("admin/admin_homepage");
        modelAndView.addObject("usersList", userService.findAll());
        return modelAndView;
    }

    /**
     * Redirects admin to add regular user page.
     *
     * @return {@link ModelAndView} instance.
     */
    @RequestMapping("/add_operator")
    public ModelAndView redirectToAddUserPage() {
        return new ModelAndView("admin/admin_add_operator");
    }

    /**
     * Redirects admin to the user editing page.
     *
     * @return {@link ModelAndView} instance.
     */
    @RequestMapping("/edit_user")
    public ModelAndView redirectToEditUserPage(@RequestParam(name = "uid") String uid) {
        ModelAndView modelAndView = new ModelAndView("admin/admin_edit_employee");
        modelAndView.addObject("user", userService.findByUniqueKey(uid));
        return modelAndView;
    }

    /**
     * Saves new user entity to the DB
     * and redirects user to the admin homepage.
     *
     * @return {@link ModelAndView} instance.
     */
    @RequestMapping("/save_operator")
    public String saveUser(@ModelAttribute UserDto userDto, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "error/error";
        }

        userDto.setAuthority(UserRoleType.ROLE_USER);
        userService.save(userDto);
        return "redirect:/admin/homepage";
    }
}
