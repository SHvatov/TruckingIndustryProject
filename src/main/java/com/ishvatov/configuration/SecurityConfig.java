package com.ishvatov.configuration;

import com.ishvatov.handler.SuccessLoginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Security configuration class.
 *
 * @author Sergey Khvatov 
 */
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"com.ishvatov"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * {@link UserDetailsService} instance.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Configure {@link DaoAuthenticationProvider} object.
     *
     * @return configured instance of the {@link DaoAuthenticationProvider}.
     */
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Creates an instance of the {@link BCryptPasswordEncoder}, which is used
     * to encrypt the passwords.
     *
     * @return Singleton instance of the {@link BCryptPasswordEncoder}.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates an instance of the {@link SuccessLoginHandler}, which
     * implements {@link AuthenticationSuccessHandler} and is used
     * to redirect users according to their roles.
     *
     * @return Singleton instance of the {@link SuccessLoginHandler}.
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SuccessLoginHandler();
    }

    /**
     * Implementation of the configure method.
     *
     * @param auth {@link AuthenticationManagerBuilder} instance.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

    /**
     * Implementation of the {@link HttpSecurity} object configure method.
     *
     * @param http {@link HttpSecurity} instance to configure.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            // mapToDto user permissions
            .antMatchers("/login/**").permitAll()
            .antMatchers("/resources/**").permitAll()
            .antMatchers("/error/**").permitAll()
            .antMatchers("/employee/**").hasRole("USER")
            .antMatchers("/driver/**").hasRole("DRIVER")
            .antMatchers("/", "/**").hasRole("ADMIN")
            .anyRequest().authenticated()
            // configure login process
            .and()
            .formLogin()
            .loginPage("/login/login")
            .successHandler(authenticationSuccessHandler())
            .loginProcessingUrl("/login/perform_login")
            .failureUrl("/login/login_failed")
            // configure logout process
            .and()
            .logout()
            .logoutUrl("/login/perform_logout")
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true);
        http.csrf().disable();
    }
}
