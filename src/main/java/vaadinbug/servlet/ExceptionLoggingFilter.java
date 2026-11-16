
/*
 * Copyright (C) 2026 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.servlet;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Servlet filter that ensures any exceptions which are not handled by the servlet get logged somewhere.
 */
public class ExceptionLoggingFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Process the request. If an exception is thrown, it will be (possibly) logged and re-thrown.
     *
     * @see #shouldLogException shouldLogException()
     */
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException | RuntimeException | Error e) {
            if (this.shouldLogException(request, response, e))
                this.logException(request, response, e);
            throw e;
        } catch (Throwable e) {
            if (this.shouldLogException(request, response, e))
                this.logException(request, response, e);
           throw new ServletException(e);
        }
    }

    /**
     * Determine if an exception caught by this instance should be logged.
     *
     * <p>
     * The implementation in {@link ExceptionLoggingFilter} returns {@code true} except for
     * {@link SocketException} (typically caused by the client disconnecting).
     * Subclasses should override if necessary.
     *
     * @param request HTTP request
     * @param response HTTP response
     * @param t exception caught by this instance
     * @return true if exception is loggable
     */
    protected boolean shouldLogException(HttpServletRequest request, HttpServletResponse response, Throwable t) {
        for (Throwable e = t; e != null; e = e.getCause()) {
            if (e instanceof SocketException)
                return false;
            if (e.getClass().getName().equals("org.apache.catalina.connector.ClientAbortException"))
                return false;
        }
        return true;
    }

    /**
     * Log an exception caught by this instance and determined to be loggable by {@link #shouldLogException shouldLogException()}.
     *
     * <p>
     * The implementation in {@link ExceptionLoggingFilter} logs the exception as an error.
     * Subclasses should override if necessary.
     *
     * @param request HTTP request
     * @param response HTTP response
     * @param t exception caught by this instance
     */
    protected void logException(HttpServletRequest request, HttpServletResponse response, Throwable t) {
        this.log.error("exception within servlet", t);
    }
}
