
/*
 * Copyright (C) 2026 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.context;

import com.vaadin.flow.server.InitParameters;
import com.vaadin.flow.spring.SpringServlet;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.SessionTrackingMode;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.WebAppRootListener;
import org.springframework.web.util.WebUtils;

import vaadinbug.servlet.ExceptionLoggingFilter;
import vaadinbug.web.Deployment;
import vaadinbug.web.WebConfiguration;

/**
 * Configures the web application.
 *
 * <p>
 * This is effectively "where it all starts". Tomcat invokes this class when the web application is loaded.
 */
public class WebApplicationConfiguration implements WebApplicationInitializer {

    private static final String VAADIN_SERVLET_NAME = "VBUG";
    private static final String DISPATCHER_SERVLET_NAME = "dispatcher";

    private static final String UNCAUGHT_EXCEPTION_FILTER_NAME = "uncaughtExceptionFilter";

    // Servlet load ordering
    private static final int VAADIN_SERVLET_LOAD_ORDER = 1;
    private static final int DISPATCHER_SERVLET_LOAD_ORDER = 2;

    // HTTP session timeout
    private static final Duration SESSION_TIMEOUT = Duration.ofHours(1);

    //
    // Vaadin session timeout
    //
    // If "heartbeatInterval" is defined, UI's are deemed "inactive" and automatically
    // closed (via UI.close()) after (3.1 * heartbeatInterval) with no heartbeat received.
    // This is important so that UI's can be cleaned up if a user just closes the tab.
    // Otherwise, UI's would stay "active" until explicitly closed by the code.
    //
    // If "closeIdleSessions" is true, then VaadinSession's are deemed "inactive" and
    // closed after HttpSession.getMaxInactiveInterval() seconds with no UIDL requests
    // received (heartbeats don't count here). So this means if you open UI's but don't
    // do anything for HttpSession.getMaxInactiveInterval() seconds, then the Vaadin
    // session will be closed. This provides typical "idle timeout" behavior.
    //
    private static final Duration HEARTBEAT_INTERVAL = Duration.ofSeconds(193); // 3.22 min, for an effective ~10 minute idle time

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        // Log something
        this.log.warn("*** Starting up VBUG");

        // Configure request character encoding
        servletContext.setRequestCharacterEncoding(StandardCharsets.UTF_8);

        // This sets the "${vbug.wardir}" system property to whatever is the web application root directory
        servletContext.addListener(new WebAppRootListener());
        servletContext.setInitParameter(WebUtils.WEB_APP_ROOT_KEY_PARAM, Deployment.WEBAPP_ROOT_PROPERTY_NAME);

        // Add servlet filter that logs uncaught exceptions
        final FilterRegistration.Dynamic uncaughtExceptionRegistration = servletContext.addFilter(
          UNCAUGHT_EXCEPTION_FILTER_NAME, new ExceptionLoggingFilter());
        uncaughtExceptionRegistration.setAsyncSupported(true);
        uncaughtExceptionRegistration.addMappingForUrlPatterns(null, true, "/*");

        // Create the main ApplicationContext
        final AnnotationConfigWebApplicationContext mainContext = new AnnotationConfigWebApplicationContext();
        mainContext.register(ApplicationConfiguration.class);
        this.log.info("the ROOT application context is {}", mainContext);

        // Add startup listener
        servletContext.addListener(new ContextLoaderListener(mainContext));

        // Create the ApplicationContext used by the DispatcherServlet as a child of the main ApplicationContext
        final AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.setParent(mainContext);
        dispatcherContext.register(WebConfiguration.class);
        this.log.info("the DISPATCHER application context is {}", dispatcherContext);

        // Create and register the DispatcherServlet
        final ServletRegistration.Dynamic dispatcherRegistration = servletContext.addServlet(
          DISPATCHER_SERVLET_NAME, new DispatcherServlet(dispatcherContext));
        dispatcherRegistration.setLoadOnStartup(DISPATCHER_SERVLET_LOAD_ORDER);
        dispatcherRegistration.addMapping(Deployment.DISPATCHER_SERVLET_MAPPING);

        // Create and register the Vaadin servlet
        final ServletRegistration.Dynamic vaadinRegistration = servletContext.addServlet(
          VAADIN_SERVLET_NAME, new SpringServlet(mainContext, Deployment.VAADIN_SERVLET_MAPPING.equals("/*")));
        vaadinRegistration.setLoadOnStartup(VAADIN_SERVLET_LOAD_ORDER);
        vaadinRegistration.setAsyncSupported(true);
        vaadinRegistration.addMapping(Deployment.VAADIN_SERVLET_MAPPING);

        // Set some Vaadin parameters
        vaadinRegistration.setInitParameter(InitParameters.SERVLET_PARAMETER_PRODUCTION_MODE, "true");
        vaadinRegistration.setInitParameter(InitParameters.SERVLET_PARAMETER_CLOSE_IDLE_SESSIONS, "true");
        vaadinRegistration.setInitParameter(InitParameters.SERVLET_PARAMETER_HEARTBEAT_INTERVAL,
          String.valueOf(HEARTBEAT_INTERVAL.toSeconds()));

        // Vaadin mystery voo-doo
//        vaadinRegistration.addMapping("/VAADIN/*");
//        vaadinRegistration.setInitParameter(ApplicationConfig.JSR356_MAPPING_PATH,
//          Deployment.VAADIN_SERVLET_MAPPING + "/" + Constants.PUSH_MAPPING);
//        servletContext.setInitParameter(RootMappedCondition.URL_MAPPING_PROPERTY, Deployment.VAADIN_SERVLET_MAPPING);

        // Configure HTTP sessions
        servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
        servletContext.setSessionTimeout((int)SESSION_TIMEOUT.toMinutes());

        // Start up main context - WHY? This seems wrong.
        mainContext.setServletContext(servletContext);
        mainContext.refresh();
    }
}
