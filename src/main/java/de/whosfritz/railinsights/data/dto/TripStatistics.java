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

    public TripStatistics(double percentageCancelled, double percentageDelayed, double percentageOnTime) {
        this.percentageCancelled = percentageCancelled;
        this.percentageDelayed = percentageDelayed;
        this.percentageOnTime = percentageOnTime;
    }
}
