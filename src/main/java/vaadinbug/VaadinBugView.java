
/*
 * Copyright (C) 2022 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
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

    public VaadinBugView() throws Exception {

        // Title
        this.add(new Text("Vaadin Bug Demo"));

        // Build form
        final FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("1px", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        final Component nameField = this.buildNameField();
        formLayout.addFormItem(nameField, "Name:");
        this.add(formLayout);

        // Add buttons
        final HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(new Button("Reset", e -> this.resetForm()));
        buttonLayout.add(new Button("Submit", e -> this.submitForm()));
        this.add(buttonLayout);
    }

    private Component buildNameField() throws Exception {
        //return new com.vaadin.flow.component.textfield.TextField();                                       // this works!
        return (Component)Class.forName("com.vaadin.flow.component.textfield.TextField").newInstance();     // this doesn't!
    }

    private void resetForm() {
        this.log.info("VaadinBugView: form reset");
    }

    private void submitForm() {
        this.log.info("VaadinBugView: form submitted");
    }
}
