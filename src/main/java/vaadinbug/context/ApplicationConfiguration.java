
/*
 * Copyright (C) 2026 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.context;

import com.google.common.base.Preconditions;
import com.vaadin.flow.i18n.DefaultI18NProvider;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.spring.annotation.EnableVaadin;
import com.vaadin.flow.spring.annotation.VaadinTaskExecutor;

import java.util.List;
import java.util.Locale;

import org.dellroad.stuff.spring.AbstractBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import vaadinbug.support.LoggingThreadPoolTaskExecutor;
import vaadinbug.support.LoggingThreadPoolTaskScheduler;

/**
 * Spring configuration for the main application context.
 *
 * <p>
 * This configuration is activated via {@link WebApplicationConfiguration}.
 *
 * @see WebApplicationConfiguration
 */
@ComponentScan(
  basePackages = { "vaadinbug", "com.vaadin.flow.spring" },
  excludeFilters = {

    // Exclude @Component beans that belong in the dispatcher servlet application context
    @ComponentScan.Filter(type = FilterType.REGEX, pattern = "vaadinbug\\.web\\..*"),

    // Exclude Vaadin beans that we don't want (bleh)
    @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.vaadin\\.flow\\.spring\\.security\\..*")
  }
)
@Configuration
@EnableSpringConfigured
@EnableVaadin("vaadinbug.gui")
@PropertySource("classpath:vbug.properties")
public class ApplicationConfiguration extends AbstractBean {

    /**
     * Determine if the given application context is the root application context for the entire application.
     *
     * <p>
     * The other possibility is the dispatcher servlet application context, which is a child of the root context.
     */
    public static boolean isRootContext(ApplicationContext context) {
        Preconditions.checkArgument(context != null, "negative context");
        return context.getParent() == null;
    }

// Bean factory beans

    // Substitute ${properties.like.this} in this application context using vbug.properties
    // If using spring-boot, this can be removed because it's automatically included
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

// Primary Executor and Scheduler

    @Bean(Beans.PRIMARY_TASK_EXECUTOR)
    public LoggingThreadPoolTaskExecutor primaryTaskExecutor() {
        return this.newTaskExecutor();
    }

    @Bean(Beans.PRIMARY_TASK_SCHEDULER)
    public LoggingThreadPoolTaskScheduler primaryTaskScheduler() {
        return this.newTaskScheduler();
    }

// Secondary Executor and Scheduler

    @Bean(Beans.SECONDARY_TASK_EXECUTOR)
    @VaadinTaskExecutor
    public LoggingThreadPoolTaskExecutor secondaryTaskExecutor() {
        return this.newTaskExecutor();
    }

    @Bean(Beans.SECONDARY_TASK_SCHEDULER)
    public LoggingThreadPoolTaskScheduler secondaryTaskScheduler() {
        return this.newTaskScheduler();
    }

// Required to silence annoying repetitive Vaadin log messages

    @Bean
    public I18NProvider i18nProvider() {
        return new DefaultI18NProvider(List.of(Locale.ENGLISH));
    }

// Helper methods

    private LoggingThreadPoolTaskExecutor newTaskExecutor() {
        final LoggingThreadPoolTaskExecutor executor = new LoggingThreadPoolTaskExecutor();
        executor.setCorePoolSize(16);
        executor.setMaxPoolSize(32);
        executor.setQueueCapacity(10000);
        return executor;
    }

    private LoggingThreadPoolTaskScheduler newTaskScheduler() {
        final LoggingThreadPoolTaskScheduler scheduler = new LoggingThreadPoolTaskScheduler();
        scheduler.setPoolSize(8);
        return scheduler;
    }
}
