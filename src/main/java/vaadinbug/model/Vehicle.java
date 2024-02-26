
/*
 * Copyright (C) 2022 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.model;

import jakarta.validation.constraints.NotNull;

public class Vehicle {

    private String model;
    private Color color;

    @NotNull(message = "Model is required")
    public String getModel() {
        return this.model;
    }
    public void setModel(final String model) {
        this.model = model;
    }

    @NotNull(message = "Color is required")
    public Color getColor() {
        return this.color;
    }
    public void setColor(final Color color) {
        this.color = color;
    }

// Object

    @Override
    public String toString() {
        return String.format("Vehicle[model=\"%s\",color=%s]", this.model, this.color);
    }
}
