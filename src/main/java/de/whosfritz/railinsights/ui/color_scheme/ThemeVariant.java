package de.whosfritz.railinsights.ui.color_scheme;

public enum ThemeVariant {
    DARK("dark"), LIGHT("light");

    private final String caption;

    ThemeVariant(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public String getAttribute() {
        return name().toLowerCase();
    }
}
