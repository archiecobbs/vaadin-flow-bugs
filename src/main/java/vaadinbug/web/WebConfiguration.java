
/*
 * Copyright (C) 2026 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.web;

import org.dellroad.stuff.spring.AbstractBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring configuration for the dispatcher servlet application context.
 *
 * @see vaadinbug.context.ApplicationConfiguration
 */
@Configuration
@EnableWebMvc
@ComponentScan("vaadinbug.web")
public class WebConfiguration extends AbstractBean implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(HttpMessageConverters.ServerBuilder builder) {

        // we would add any message converters here...
    }
}
