
/*
 * Copyright (C) 2022 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PreserveOnRefresh
@Route("")
@SuppressWarnings("serial")
public class VaadinBugView extends VerticalLayout {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Button button = new Button("Press Me", e -> this.buttonPressed());

    private int numButtonPresses;

    public VaadinBugView() {
        this.add(new Text("Vaadin Bug Demo"));
        this.add(this.button);
    }

    private void buttonPressed() {
        final StreamResource resource = new StreamResource("foo", (stream, session) -> stream.close());
        final VaadinSession session = VaadinSession.getCurrent();
        session.access(() -> session.getResourceRegistry().registerResource(resource));
    }
}
