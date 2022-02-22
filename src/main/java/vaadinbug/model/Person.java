
/*
 * Copyright (C) 2022 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.model;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Person {

    private String name;
    private Set<Color> favoriteColors;
    private LocalDate birthday;

    @NotNull(message = "Name required")
    public String getName() {
        return this.name;
    }
    public void setName(final String name) {
        this.name = name;
    }

    @NotNull(message = "Colors required")
    @Size(min = 1, message = "At least one color required")
    public Set<Color> getFavoriteColors() {
        return this.favoriteColors;
    }
    public void setFavoriteColors(final Set<Color> favoriteColors) {
        this.favoriteColors = favoriteColors;
    }

    @NotNull(message = "Birthday required")
    public LocalDate getBirthday() {
        return this.birthday;
    }
    public void setBirthday(final LocalDate birthday) {
        this.birthday = birthday;
    }

// Object

    @Override
    public String toString() {
        return String.format("Person[name=\"%s\",favoriteColors=%s,birthday=%s]", this.name, this.favoriteColors, this.birthday);
    }
}
