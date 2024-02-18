package de.whosfritz.railinsights.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Utility class to handle percentage values
 */
public class PercentageUtil {

    /**
     * Converts a double to a two decimal places double
     *
     * @param value the value to convert
     * @return the value with two decimal places
     */
    public static Double convertToTwoDecimalPlaces(double value) {
        DecimalFormat df = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.US));

        String percentageString = df.format(value);
        value = Double.parseDouble(percentageString);
        return value;
    }

}
