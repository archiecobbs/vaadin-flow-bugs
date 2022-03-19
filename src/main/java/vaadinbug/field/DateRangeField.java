
/*
 * Copyright (C) 2022 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.field;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import vaadinbug.model.DateRange;

@SuppressWarnings("serial")
public class DateRangeField extends CustomField<DateRange> {

    private final DatePicker startDateField = new DatePicker();
    private final DatePicker endDateField = new DatePicker();

    public DateRangeField() {
        this.startDateField.setWidth("120px");
        this.startDateField.setPlaceholder("Starting");
        this.startDateField.setRequired(true);
        this.endDateField.setWidth("120px");
        this.endDateField.setPlaceholder("Ending");
        this.endDateField.setRequired(true);

        final HorizontalLayout layout = new HorizontalLayout();
        this.add(layout);
        layout.add(this.startDateField);
        layout.add(new Text("-"));
        layout.add(this.endDateField);

        // When any sub-field changes, recalculate this field's value
        this.startDateField.addValueChangeListener(e -> this.updateValue());
        this.endDateField.addValueChangeListener(e -> this.updateValue());
    }

// CustomField

    @Override
    protected DateRange generateModelValue() {
        final DateRange dateRange = new DateRange();
        dateRange.setStartDate(this.startDateField.getValue());
        dateRange.setEndDate(this.endDateField.getValue());
        return dateRange;
    }

    @Override
    protected void setPresentationValue(DateRange dateRange) {
        this.startDateField.setValue(dateRange != null ? dateRange.getStartDate() : null);
        this.endDateField.setValue(dateRange != null ? dateRange.getEndDate() : null);
    }
}
