
/*
 * Copyright (C) 2026 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.web;

/**
 * Various constants related to the web deployment.
 */
public final class Deployment {

    // Vaadin servlet mapping
    public static final String VAADIN_SERVLET_MAPPING = "/vbug/*";

    // Dispatcher servlet mapping
    public static final String DISPATCHER_SERVLET_MAPPING = "/api/*";

    // Special HTTP headers
    public static final String X_FORWARDED_FOR_HEADER = "X-Forwarded-For";      // appears when we are behind a reverse proxy

    // System property that contains the webapp deployment directory in the filesystem
    public static final String WEBAPP_ROOT_PROPERTY_NAME = "vbug.wardir";

    private Deployment() {
    }
}
