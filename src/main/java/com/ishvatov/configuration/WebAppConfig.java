package com.ishvatov.configuration;

import org.dozer.DozerBeanMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Web application java-based spring configuration class.
 *
 * @author Sergey Khvatov
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.ishvatov"})
public class WebAppConfig {

    /**
     * Configures an instance of a class that implements
     * the {@link ViewResolver} interface, which is used
     * to access the views stored in the /WEB-INF dir.
     *
     * @return configured instance of {@link InternalResourceViewResolver}.
     */
    @Bean
    public ViewResolver setupViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    /**
     * Configures JSR303 Validation - setups the path to the properties file with
     * internationalized validation error messages.
     *
     * @return configured {@link ResourceBundleMessageSource} object.
     */
    @Bean
    public MessageSource setupMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("error_messages");
        return messageSource;
    }

    /**
     * Configures and returns an instance of the DozerBeanMapper class used
     * for the mapping.
     *
     * @return singleton {@link DozerBeanMapper} instance.
     */
    @Bean
    public DozerBeanMapper getMapper() {
        return new DozerBeanMapper();
    }
}
