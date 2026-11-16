
/*
 * Copyright (C) 2026 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.service;

import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.scopes.VaadinSessionScope;

import org.dellroad.stuff.spring.AbstractBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;

// This is an example of a @Configurable bean that lives in the root application context in the Vaadin scope
// It has autowired into it a @Component bean that lives in the root application context in the normal scope
@Configurable
@Scope(VaadinSessionScope.VAADIN_SESSION_SCOPE_NAME)
public class SessionServiceImpl extends AbstractBean implements ApplicationContextAware, SessionService, InitializingBean {

    @Autowired
    private DateService dateService;

    @Value("${vbug.version}")
    private String applicationVersion;

    private ApplicationContext applicationContext;

    public SessionServiceImpl() {
        this.log("starting construction of new instance");
    }

// ApplicationContextAware

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.log("setApplicationContext() invoked");
        this.applicationContext = applicationContext;
    }

// InitializingBean

    @Override
    public void afterPropertiesSet() {
        this.log("afterPropertiesSet() invoked");
        this.log("  - this.applicationContext={}", this.applicationContext);
        this.log("  - this.dateService={}", this.dateService);
        this.log("  - this.applicationVersion={}", this.applicationVersion);
        if (this.dateService == null)
            throw new RuntimeException("SessionServiceImpl: my DateService bean failed to autowire");
        if (this.applicationVersion == null)
            throw new RuntimeException("SessionServiceImpl: my application version was not set");
    }

// SessionService

    @Override
    public String getApplicationVersion() {
        this.log("getApplicationVersion() invoked, returning {}", this.applicationVersion);
        return this.applicationVersion;
    }

    @Override
    public String getPushId() {
        return VaadinSession.getCurrent().getPushId();
    }

// Debug

    private void log(String format, Object... args) {
        final Object[] args2 = new Object[2 + args.length];
        System.arraycopy(args, 0, args2, 2, args.length);
        args2[0] = this.getClass().getSimpleName();
        args2[1] = String.format("0x%08x", System.identityHashCode(this));
        this.log.info("{}@{}: " + format, args2);
    }
}
