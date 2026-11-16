
/*
 * Copyright (C) 2026 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.dellroad.stuff.pobj.NotConfiguredException;
import org.dellroad.stuff.spring.AbstractBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import vaadinbug.service.DateService;

/**
 * Controller for incoming REST API HTTP requests.
 */
@Controller
public class RestApiController extends AbstractBean implements InitializingBean {

    @Autowired
    private DateService dateService;

// InitializingBean

    @Override
    public void afterPropertiesSet() {
        if (this.dateService == null)
            throw new RuntimeException("RestApiController: my DateService bean failed to autowire");
    }

    /**
     * Handle incoming time of day requests.
     */
    @RequestMapping(value = "/date", method = RequestMethod.GET)
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String result = String.format("The current time is %s%n", this.dateService.getCurrentTime());
        this.log.info("REST API {} request for current date from {}; returning \"{}\"",
          request.getMethod(), this.getOrigin(request), result);
        final HttpStatus status = HttpStatus.OK;
        response.setStatus(status.value());
        response.setHeader(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(result.length()));
        response.getWriter().println(String.format("%d %s", status.value(), status.getReasonPhrase()));
        response.getWriter().println();
        response.getWriter().print(result);
    }

    @ExceptionHandler(NotConfiguredException.class)
    protected void handleInvalidDateString(HttpServletResponse response, NotConfiguredException e) throws IOException {
        this.log.error("can't handle REST API request: not configured");
        this.sendError(response, HttpStatus.SERVICE_UNAVAILABLE, "System Not Configured");
    }

    private String getOrigin(HttpServletRequest request) throws IOException {
        final String forwardedFor = request.getHeader(Deployment.X_FORWARDED_FOR_HEADER);
        return forwardedFor != null ? forwardedFor : request.getRemoteAddr() + ":" + request.getRemotePort();
    }

    private void sendError(HttpServletResponse response, HttpStatus status, String message, Object... args) throws IOException {
        message = String.format(message, args);
        response.setStatus(status.value());
        response.setHeader(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.getWriter().println(String.format("%d %s", status.value(), status.getReasonPhrase()));
        response.getWriter().println();
        response.getWriter().println(message);
    }
}
