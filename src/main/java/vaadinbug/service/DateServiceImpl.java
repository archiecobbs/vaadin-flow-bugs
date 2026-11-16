
/*
 * Copyright (C) 2026 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

// This is an example of a @Component bean that lives in the root application context
@Component
public class DateServiceImpl implements DateService {

    @Override
    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}
