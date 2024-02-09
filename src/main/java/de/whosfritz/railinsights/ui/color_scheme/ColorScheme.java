package de.whosfritz.railinsights.ui.color_scheme;

import lombok.Getter;

/**
 * Enum for color schemes
 * Provides the color schemes for the application and returns the color as a string (HEX)
 */
@Getter
public enum ColorScheme {

    PRIMARY("#EC0016"),
    SECONDARY("#F75056"),
    SUCCESS("#408335"),
    ERROR("#740009"),
    CRITICAL("#4D0820"),
    WARNING_HARD("#F39200"),
    WARNING_SOFT("#FFD800"),
    CONTRAST("#646973"),
    INFO("#1455C0"),
    INFO_LIGHT("#73AEF4");

    private final String color;

    ColorScheme(String color) {
        this.color = color;
    }

}