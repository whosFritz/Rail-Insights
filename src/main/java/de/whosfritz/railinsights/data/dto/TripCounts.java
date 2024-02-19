package de.whosfritz.railinsights.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * A class to store the trip counts for a specific time period.
 */
@Getter
@Setter
public class TripCounts {

    private Map<LocalDate, Integer> dailyTripCounts = new HashMap<>();
    private Map<LocalDate, Integer> dailyTripLongDistanceCounts = new HashMap<>();
    private Map<LocalDate, Integer> dailyTripRegionalCounts = new HashMap<>();
    private Map<LocalDateTime, Integer> hourlyTripCounts = new HashMap<>();
    private Map<LocalDateTime, Integer> hourlyTripLongDistanceCounts = new HashMap<>();
    private Map<LocalDateTime, Integer> hourlyTripRegionalCounts = new HashMap<>();
}