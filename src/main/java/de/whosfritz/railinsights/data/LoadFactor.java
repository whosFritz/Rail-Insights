package de.whosfritz.railinsights.data;

import lombok.Getter;

/**
 * Enum to represent the load factor of a train.
 */
@Getter
public enum LoadFactor {
    KEINE("no-load", "keine"),
    WENIG_BIS_NORMAL("low to medium", "wenig bis normal"),
    HOCH("high", "hoch"),
    SEHR_HOCH("very-high", "sehr hoch");

    private final String value;

    private final String alias;

    LoadFactor(String value, String alias) {
        this.value = value;
        this.alias = alias;
    }


    /**
     * Returns the LoadFactor for the given string.
     *
     * @param text the string to convert
     * @return the LoadFactor for the given string
     */
    public static LoadFactor fromString(String text) {
        for (LoadFactor b : LoadFactor.values()) {
            if (b.value.equalsIgnoreCase(text) || (b.alias != null && b.alias.equalsIgnoreCase(text))) {
                return b;
            }
        }
        return null;
    }
}