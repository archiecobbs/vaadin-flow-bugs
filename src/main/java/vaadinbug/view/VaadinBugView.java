
/*
 * Copyright (C) 2022 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.view;

import com.google.common.base.Preconditions;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vaadinbug.model.Contract;

@PreserveOnRefresh
@Route("")
@SuppressWarnings("serial")
public class VaadinBugView extends VerticalLayout {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private Notification errorNotification;

    public VaadinBugView() {

        // Add title
        this.add(new Text("Vaadin Bug Demo"));

        // Configure fields
        final TextField firstNameField = new TextField();
        firstNameField.setMaxLength(Contract.NAME_MAX_LENGTH);
        firstNameField.setPattern(Contract.NAME_PATTERN);
        firstNameField.setRequired(true);
        firstNameField.setRequiredIndicatorVisible(true);
        final TextField lastNameField = new TextField();
        lastNameField.setMaxLength(Contract.NAME_MAX_LENGTH);
        lastNameField.setPattern(Contract.NAME_PATTERN);
        lastNameField.setRequired(true);
        lastNameField.setRequiredIndicatorVisible(true);

        // Build form
        final FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("1px", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        formLayout.addFormItem(firstNameField, "First Name");
        formLayout.addFormItem(lastNameField, "Last Name");
        this.add(formLayout);

        // Add buttons
        final HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(new Button("Reset", e -> this.resetForm()));
        buttonLayout.add(new Button("Submit", e -> this.submitForm()));
        this.add(buttonLayout);

        // Add Enter listeners
        Shortcuts.addShortcutListener(firstNameField,
          e -> this.info("First name: \"" + firstNameField.getValue() + "\""),
          Key.ENTER)
        .listenOn(firstNameField)
        .resetFocusOnActiveElement();

        Shortcuts.addShortcutListener(lastNameField,
          e -> this.info("Last name: \"" + lastNameField.getValue() + "\""),
          Key.ENTER)
        .listenOn(lastNameField)
        .resetFocusOnActiveElement();
    }

    private void resetForm() {
        this.log.info("VaadinBugView: form reset");
        this.resetErrorNotification();
    }

    private void submitForm() {
        this.log.info("VaadinBugView: form submitted");
        this.resetErrorNotification();
    }

    private void resetErrorNotification() {
        if (this.errorNotification != null) {
            this.errorNotification.close();
            this.errorNotification = null;
        }
    }

    private String[] toStrings(ValidationException e) {
        return e.getValidationErrors().stream()
          .map(ValidationResult::getErrorMessage)
          .toArray(String[]::new);
    }

    private void info(String message) {
        this.notify(NotificationVariant.LUMO_CONTRAST, Notification.Position.MIDDLE, 1000, message);
    }

    /**
     * Show a notification.
     *
     * @param variant type of message
     * @param position position on screen
     * @param duration how long to stay on screen, or zero for infinity
     * @param message main message
     * @param details extra message(s), or null
     * @throws IllegalArgumentException if any parameter other than {@code details} is null
     */
    private Notification notify(NotificationVariant variant,
      Notification.Position position, int duration, String message, String... details) {

        // Sanity check
        Preconditions.checkArgument(variant != null, "null variant");
        Preconditions.checkArgument(position != null, "null position");
        Preconditions.checkArgument(message != null, "null message");

        // Build notification
        final Notification notification = new Notification();
        notification.addThemeVariants(variant);
        notification.setPosition(position);
        notification.setDuration(duration);

        // Add top row
        final HorizontalLayout topRow = new HorizontalLayout();
        topRow.setAlignItems(FlexComponent.Alignment.CENTER);
        topRow.add(new Div(new Text(message)));
        final Button closeButton = new Button(new Icon("lumo", "cross"), e -> notification.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.getElement().setAttribute(ElementConstants.ARIA_LABEL_ATTRIBUTE_NAME, "Close");
        topRow.add(closeButton);
        notification.add(topRow);

        // Add subsequent rows, if any
        if (details != null) {
            final List<Div> extraDivs = Stream.of(details)
              .filter(Objects::nonNull)
              .map(String::trim)
              .filter(s -> !s.isEmpty())
              .map(Text::new)
              .map(Div::new)
              .collect(Collectors.toList());
            if (!extraDivs.isEmpty()) {
                final VerticalLayout extraLayout = new VerticalLayout();
                extraLayout.setAlignItems(FlexComponent.Alignment.START);
                extraDivs.forEach(extraLayout::add);
                notification.add(extraLayout);
            }
        }

        // Display notification
        notification.open();
        return notification;
    }
}
