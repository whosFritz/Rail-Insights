package de.whosfritz.railinsights.ui.color_scheme;

import lombok.Getter;

/**
 * Enum for color schemes
 * Provides the color schemes for the application and returns the color as a string (HEX)
 */
@Getter
public enum ColorScheme {

    PRIMARY("#1455C0"),
    SECONDARY("#309FD1"),
    SUCCESS("#408335"),
    ERROR("#EC0016"),
    CRITICAL("#9B000E"),
    WARNING_HARD("#F39200"),
    WARNING_SOFT("#FFD800"),
    CONTRAST("#646973"),
    INFO("#00A099");

    private final String color;

    ColorScheme(String color) {
        this.color = color;
    }

}