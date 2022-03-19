
/*
 * Copyright (C) 2022 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.field;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;

import java.util.EnumSet;
import java.util.Objects;

import vaadinbug.model.Color;
import vaadinbug.model.Vehicle;

@SuppressWarnings("serial")
public class VehicleField extends CustomField<Vehicle> {

    private final TextField modelField = new TextField();
    private final ComboBox<Color> colorField = new ComboBox<>();
    private final BeanValidationBinder<Vehicle> validationBinder = new BeanValidationBinder<>(Vehicle.class);

    public VehicleField() {

        // Configure sub-fields
        this.modelField.setPlaceholder("Model...");
        this.modelField.setRequired(true);
        this.modelField.setRequiredIndicatorVisible(true);
        this.colorField.setItems(EnumSet.allOf(Color.class));
        this.colorField.setPlaceholder("Color...");

        final HorizontalLayout layout = new HorizontalLayout();
        this.add(layout);
        layout.add(new Text("Model:"));
        layout.add(this.modelField);
        layout.add(new Text("Color:"));
        layout.add(this.colorField);

        // When sub-field changes, recalculate this field's value
        this.modelField.addValueChangeListener(e -> this.updateValue());
        this.colorField.addValueChangeListener(e -> this.updateValue());

        // Configure internal binder for validation
        this.validationBinder.forField(this.modelField)
          .withNullRepresentation("")
          .bind("model");
        this.validationBinder.forField(this.colorField).bind("color");
    }

// Self-validation

    public ValidationResult validate(Vehicle ignored, ValueContext ctx) {
        this.validationBinder.validate();
        return this.validationBinder.validate().isOk() ? ValidationResult.ok() : ValidationResult.error("Invalid vehicle");
    }

// CustomField

    @Override
    protected Vehicle generateModelValue() {
        final String model = this.modelField.getValue();
        final Color color = this.colorField.getValue();
        final boolean modelNull = Objects.equals(model, this.modelField.getEmptyValue());
        final boolean colorNull = Objects.equals(color, this.colorField.getEmptyValue());
        if (modelNull && colorNull)
            return null;
        final Vehicle vehicle = new Vehicle();
        vehicle.setModel(modelNull ? null : model);
        vehicle.setColor(colorNull ? null : color);
        return vehicle;
    }

    @Override
    protected void setPresentationValue(Vehicle vehicle) {
        this.modelField.setValue(vehicle != null ? vehicle.getModel() : this.modelField.getEmptyValue());
        this.colorField.setValue(vehicle != null ? vehicle.getColor() : this.colorField.getEmptyValue());
    }
}
