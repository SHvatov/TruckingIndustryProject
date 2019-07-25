package com.ishvatov.configuration;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * Web application java-based spring initialization class.
 *
 * @author Sergey Khvatov
 */
public class WebAppInit extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * Configures filter, which is used to process UTF-8 requests.
     *
     * @return Array of configured filters.
     */
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[]{characterEncodingFilter};
    }

    /**
     * Return array of classes, that are used as root configuration.
     *
     * @return array of classes, that are used as root configuration.
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{HibernateConfig.class};
    }

    /**
     * Return array of classes, that are used as servlet configuration.
     *
     * @return array of classes, that are used as servlet configuration.
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebAppConfig.class};
    }

    /**
     * Return array of servlets' mappings.
     *
     * @return array of servlets' mappings.
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
