package com.ishvatov.handler;

import com.ishvatov.model.entity.enum_types.UserRoleType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Basic implementation of the {@link AuthenticationSuccessHandler} interface.
 * Redirects user according to his userAuthority and create logs about the user
 * authentication process.
 *
 * @author Sergey Khvatov
 */
public class SuccessLoginHandler extends SimpleUrlAuthenticationSuccessHandler {

    /**
     * Handle the request / response method.
     *
     * @param httpServletRequest  HTTP request body.
     * @param httpServletResponse HTTP response body.
     * @param authentication      {@link Authentication} context instance.
     */
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                  Authentication authentication) throws
        IOException, ServletException {
        if (httpServletResponse.isCommitted()) {
            return;
        }
        getRedirectStrategy()
            .sendRedirect(httpServletRequest, httpServletResponse, determineRedirectUrl(authentication));
    }

    /**
     * Determines the redirection url according to the user userAuthority.
     *
     * @param authentication {@link Authentication} context instance.
     * @return string representation of the redirection target url.
     */
    private String determineRedirectUrl(Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals(UserRoleType.ROLE_ADMIN.getName())) {
                return "/admin/homepage";
            } else if (authority.getAuthority().equals(UserRoleType.ROLE_USER.getName())) {
                return "/employee/homepage";
            } else if (authority.getAuthority().equals(UserRoleType.ROLE_DRIVER.getName())) {
                return "/driver/homepage";
            }
        }

        return "/error/error_page";
    }
}
