
/*
 * Copyright (C) 2022 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Contract {

    public static final String NAME_PATTERN = "^[\\p{Alpha}]+( [\\p{Alpha}]+)*$";
    public static final int NAME_MAX_LENGTH = 12;

    private String name;
    private DateRange term;

    @NotNull(message = "Name is required")
    @Pattern(regexp = NAME_PATTERN, message = "Alphabetic words only")
    @Size(max = NAME_MAX_LENGTH, message = "At most 12 characters")
    public String getName() {
        return this.name;
    }
    public void setName(final String name) {
        this.name = name;
    }

    @NotNull(message = "Contract is required")
    @Valid
    public DateRange getTerm() {
        return this.term;
    }
    public void setTerm(final DateRange term) {
        this.term = term;
    }

// Object

    @Override
    public String toString() {
        return String.format("Person[name=\"%s\",term=%s]", this.name, this.term);
    }
}
