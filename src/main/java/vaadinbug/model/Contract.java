
/*
 * Copyright (C) 2022 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Contract {

    public static final String NAME_PATTERN = "^[\\p{Alpha}]+( [\\p{Alpha}]+)*$";
    public static final int NAME_MAX_LENGTH = 12;

    private String name;
    private Vehicle vehicle;

    @NotNull(message = "Name is required")
    @Pattern(regexp = NAME_PATTERN, message = "Alphabetic words only")
    @Size(max = NAME_MAX_LENGTH, message = "At most 12 characters")
    public String getName() {
        return this.name;
    }
    public void setName(final String name) {
        this.name = name;
    }

    @NotNull(message = "Vehicle is required")
    @Valid
    public Vehicle getVehicle() {
        return this.vehicle;
    }
    public void setVehicle(final Vehicle vehicle) {
        this.vehicle = vehicle;
    }

// Object

    @Override
    public String toString() {
        return String.format("Contract[name=\"%s\",vehicle=%s]", this.name, this.vehicle);
    }
}
