
/*
 * Copyright (C) 2022 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug.model;

public enum Color {
    BLUE,
    GREEN,
    INDIGO,
    MAUVE,
    ORANGE,
    PURPLE,
    RED,
    TAN,
    YELLOW;

    @Override
    public String toString() {
        return this.name().substring(0, 1) + this.name().substring(1).toLowerCase();
    }
}
