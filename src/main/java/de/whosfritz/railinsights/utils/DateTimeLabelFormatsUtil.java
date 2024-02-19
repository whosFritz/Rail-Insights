package de.whosfritz.railinsights.utils;

import com.vaadin.flow.component.charts.model.DateTimeLabelFormats;

/**
 * Simple utility class to get a cleaned DateTimeLabelFormats object
 */
public class DateTimeLabelFormatsUtil {

    /**
     * Returns a cleaned DateTimeLabelFormats object
     * does not provide any date / time parameters
     *
     * @return DateTimeLabelFormats
     */
    public static DateTimeLabelFormats getCleanedDateTimeLabelFormat() {
        DateTimeLabelFormats dateTimeLabelFormats = new DateTimeLabelFormats();
        dateTimeLabelFormats.setDay("");
        dateTimeLabelFormats.setHour("");
        dateTimeLabelFormats.setMinute("");
        dateTimeLabelFormats.setMonth("");
        dateTimeLabelFormats.setWeek("");
        dateTimeLabelFormats.setYear("");
        return dateTimeLabelFormats;
    }

}
