
/*
 * Copyright (C) 2026 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.support;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SuppressWarnings("serial")
public class LoggingThreadPoolTaskScheduler extends ThreadPoolTaskScheduler {

    @SuppressWarnings("this-escape")
    public LoggingThreadPoolTaskScheduler() {
        this.setErrorHandler(t -> this.logger.error("exception in async task: " + t, t));
    }
}
