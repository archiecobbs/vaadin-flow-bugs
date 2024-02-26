
/*
 * Copyright (C) 2022 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.transaction.annotation.EnableTransactionManagement;

// CHECKSTYLE OFF: HideUtilityClassConstructor
@EnableSpringConfigured                                     // enable @Configurable
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)     // enable @Transactional
@SpringBootApplication
@Push
@SuppressWarnings("serial")
@Theme(themeClass = Lumo.class)
public class VaadinBug extends SpringApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        final VaadinBug vaadinBug = new VaadinBug();
        vaadinBug.setSources(Collections.singleton(VaadinBug.class.getPackage().getName()));
        vaadinBug.run(args);
    }
}
