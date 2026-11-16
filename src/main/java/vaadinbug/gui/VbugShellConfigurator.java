
/*
 * Copyright (C) 2026 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.gui;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Meta;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.theme.lumo.Lumo;

@Meta(name = "application-name", content = "VBUG")
@Meta(name = "description", content = "Vaadin Bug Demonstration WebApp")
@Meta(name = "publisher", content = "archie.cobbs@gmail.com")
@PageTitle("Vaadin Bug Demonstration")
@Push
@SuppressWarnings("serial")
@StyleSheet(Lumo.STYLESHEET)
@StyleSheet(Lumo.COMPACT_STYLESHEET)
public final class VbugShellConfigurator implements AppShellConfigurator {

    @Override
    public void configurePage(AppShellSettings settings) {
        AppShellConfigurator.super.configurePage(settings);
        //settings.addFavIcon("icon", "images/favicon.ico", "48x48");
    }
}
