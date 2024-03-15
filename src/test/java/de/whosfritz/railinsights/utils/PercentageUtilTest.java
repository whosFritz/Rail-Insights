package de.whosfritz.railinsights.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PercentageUtilTest {

    @Test
    public void convertToTwoDecimalPlaces_withRegularNumber() {
        double input = 123.456;
        Double expected = 123.46;
        Double actual = PercentageUtil.convertToTwoDecimalPlaces(input);
        assertEquals(expected, actual);
    }

    @Test
    public void convertToTwoDecimalPlaces_withZero() {
        double input = 0.0;
        Double expected = 0.0;
        Double actual = PercentageUtil.convertToTwoDecimalPlaces(input);
        assertEquals(expected, actual);
    }

    @Test
    public void convertToTwoDecimalPlaces_withNegativeNumber() {
        double input = -123.456;
        Double expected = -123.46;
        Double actual = PercentageUtil.convertToTwoDecimalPlaces(input);
        assertEquals(expected, actual);
    }

    @Test
    public void convertToTwoDecimalPlaces_withTwoDecimalNumber() {
        double input = 123.45;
        Double expected = 123.45;
        Double actual = PercentageUtil.convertToTwoDecimalPlaces(input);
        assertEquals(expected, actual);
    }

    @Test
    public void convertToTwoDecimalPlaces_withOneDecimalNumber() {
        double input = 123.4;
        Double expected = 123.4;
        Double actual = PercentageUtil.convertToTwoDecimalPlaces(input);
        assertEquals(expected, actual);
    }
}