
/*
 * Copyright (C) 2026 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import vaadinbug.service.DateService;
import vaadinbug.service.SessionService;
import vaadinbug.service.SessionServiceImpl;

@PreserveOnRefresh
@Route("")
@SuppressWarnings("serial")
public class VaadinBugView extends VerticalLayout {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Div currentTimeLabel = new Div();
    private final Div pushIdLabel = new Div();
    private final Div versionLabel = new Div();

    @Autowired
    private DateService dateService;

    @SuppressWarnings("this-escape")
    public VaadinBugView() {
        this.add(new Div("Vaadin Bug Demo"));
        this.add(new Button("Show Current Time", e -> this.showCurrentTime()));
        this.add(this.currentTimeLabel);
        this.add(new Button("Show Current Push ID", e -> this.showPushId()));
        this.add(this.pushIdLabel);
        this.add(new Button("Show Application Version", e -> this.showApplicationVersion()));
        this.add(this.versionLabel);
        this.add(new Button("Clear", e -> this.clearLabels()));
    }

    // Access an autowired @Component bean
    private void showCurrentTime() {
        this.updateLabel(this.currentTimeLabel, () -> "The current time is " + this.dateService.getCurrentTime());
    }

    // Instantiate and access a @Configurable bean
    private void showPushId() {
        final SessionService sessionService = new SessionServiceImpl();
        this.updateLabel(this.pushIdLabel, () -> "My push ID is " + sessionService.getPushId());
    }

    // Instantiate and access a @Configurable bean
    private void showApplicationVersion() {
        final SessionService sessionService = new SessionServiceImpl();
        this.updateLabel(this.versionLabel, () -> "Application Version is " + sessionService.getApplicationVersion());
    }

    private void updateLabel(Div label, Supplier<String> textGenerator) {
        String text = null;
        try {
            text = textGenerator.get();
        } catch (Throwable t) {
            this.log.error("error generating label text", t);
            text = "ERROR: " + t;
        }
        label.removeAll();
        label.add(text);
    }

    private void clearLabels() {
        Stream.of(this.currentTimeLabel, this.pushIdLabel, this.versionLabel)
          .forEach(label -> this.updateLabel(label, () -> null));
    }
}
