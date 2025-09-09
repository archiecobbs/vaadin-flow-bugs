
/*
 * Copyright (C) 2022 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PreserveOnRefresh
@Route("")
@SuppressWarnings("serial")
public class VaadinBugView extends VerticalLayout {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private final VerticalLayout mainLayout = new VerticalLayout();

    private final Span span = new Span("This is a <span>");
    private final Button button = new Button("Press Me", e -> this.buttonPressed());
    private final HorizontalLayout spanAndButtonBar = new HorizontalLayout(this.span, this.button);

    private int numButtonPresses;

    public VaadinBugView() {
        this.add(new Text("Vaadin Bug Demo"));
        this.mainLayout.add(this.spanAndButtonBar);
        this.add(this.mainLayout);
    }

    private void buttonPressed() {

        this.span.setText("Button has been pressed " + ++this.numButtonPresses + " time(s)");

        // Get new content
        final Component content = this.getContent();

        // Remove content from parent if already attached to something
        content.getParent()
          .filter(HasComponents.class::isInstance)
          .map(HasComponents.class::cast)
          .ifPresent(parent -> parent.remove(content));

        // Add content to main layout
        this.mainLayout.removeAll();
        this.mainLayout.add(this.spanAndButtonBar);
    }

    private Component getContent() {
        return this.spanAndButtonBar;
    }
}
