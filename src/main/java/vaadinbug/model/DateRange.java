
/*
 * Copyright (C) 2022 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.model;

import com.vaadin.flow.data.binder.ValidationResult;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.NotNull;

public class DateRange {

    private LocalDate startDate;
    private LocalDate endDate;

    @NotNull(message = "Required")
    public LocalDate getStartDate() {
        return this.startDate;
    }
    public void setStartDate(final LocalDate startDate) {
        this.startDate = startDate;
    }

    @NotNull(message = "Required")
    public LocalDate getEndDate() {
        return this.endDate;
    }
    public void setEndDate(final LocalDate endDate) {
        this.endDate = endDate;
    }

// Validation

    // Verify start date is before end date
    public ValidationResult validate() {
        if (this.startDate == null || this.endDate == null)         // null checks are handled by @NotNull annotations
            return ValidationResult.ok();
        if (this.endDate.isBefore(this.startDate))
            return ValidationResult.error("Out-of-order dates");
        return ValidationResult.ok();
    }

// Object

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        final DateRange that = (DateRange)obj;
        return Objects.equals(this.startDate, that.startDate) && Objects.equals(this.endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.startDate) ^ Objects.hashCode(this.endDate);
    }

    @Override
    public String toString() {
        return String.format("DateRange[start=%s,end=%s]", this.startDate, this.endDate);
    }
}
