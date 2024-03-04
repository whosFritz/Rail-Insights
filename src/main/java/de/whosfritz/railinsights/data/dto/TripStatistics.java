package de.whosfritz.railinsights.data.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * A class to store the trip statistics for a specific time period
 */
@Getter
@Setter
public class TripStatistics {

    private double percentageOnTime;
    private double percentageDelayed;
    private double percentageCancelled;

    private double delayMoreThan6min;
    private double delayMoreThan15min;
    private double delayMoreThan60min;

}
