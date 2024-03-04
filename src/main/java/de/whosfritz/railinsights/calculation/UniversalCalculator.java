package de.whosfritz.railinsights.calculation;

import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.whosfritz.railinsights.data.dto.TripCounts;
import de.whosfritz.railinsights.data.dto.TripStatistics;
import de.whosfritz.railinsights.utils.PercentageUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A universal calculator for RailInsights can be used to calculate different kinds of information.
 */
public class UniversalCalculator {

    public UniversalCalculator() {
    }

    /**
     * Calculate the percentage of a count in relation to the total.
     *
     * @param count the count
     * @param total the total
     * @return the percentage of the count in relation to the total
     */
    private static double calculatePercentage(int count, double total) {
        double percentage = (count / total) * 100;
        percentage = PercentageUtil.convertToTwoDecimalPlaces(percentage);
        return percentage;
    }

    /**
     * Calculate the top delayed trips ordered by delay and remove double delayed trips.
     *
     * @param trips the trips to calculate the top delayed trips from
     * @return the top delayed trips ordered by delay
     */
    public List<Trip> calculateTopDelayedTripsOrderedByDelay(List<Trip> trips, int limit) {
        return trips.stream()
                .filter(trip -> trip.getDelay() != null)
                .sorted((o1, o2) -> o2.getDelay().compareTo(o1.getDelay()))
                .limit(limit)
                .toList();
    }

    /**
     * Calculate the ratio of happened trips (regional and national)
     *
     * @param trips the trips to calculate the ratio from
     * @return the DataSeries with the ratio of happened trips
     */
    public DataSeries calculatePercentageTripRegioVsFernverkehr(List<Trip> trips) {

        AtomicReference<Double> percentageNationalTrips = new AtomicReference<>(0.0);
        AtomicReference<Double> percentageNationalExpressTrips = new AtomicReference<>(0.0);
        AtomicReference<Double> subUrbanTrips = new AtomicReference<>(0.0);
        AtomicReference<Double> percentageRegionalExpressTrips = new AtomicReference<>(0.0);
        AtomicReference<Double> percentageRegionalTrips = new AtomicReference<>(0.0);

        trips.parallelStream().forEach(trip -> {
            String product = trip.getLine().getProduct();
            if (product.equals("national")) {
                percentageNationalTrips.updateAndGet(val -> val + 1);
            } else if (product.equals("nationalExpress")) {
                percentageNationalExpressTrips.updateAndGet(val -> val + 1);
            } else if (product.contains("suburban")) {
                subUrbanTrips.updateAndGet(val -> val + 1);
            } else if (product.equals("regionalExpress")) {
                percentageRegionalExpressTrips.updateAndGet(val -> val + 1);
            } else if (product.equals("regional")) {
                percentageRegionalTrips.updateAndGet(val -> val + 1);
            }
        });

        int totalTrips = trips.size();
        double nationalTripsPercentage = Math.round(percentageNationalTrips.get() / totalTrips * 10000.0) / 100.0;
        double nationalExpressTripsPercentage = Math.round(percentageNationalExpressTrips.get() / totalTrips * 10000.0) / 100.0;
        double subUrbanTripsPercentage = Math.round(subUrbanTrips.get() / totalTrips * 10000.0) / 100.0;
        double regionalExpressTripsPercentage = Math.round(percentageRegionalExpressTrips.get() / totalTrips * 10000.0) / 100.0;
        double regionalTripsPercentage = Math.round(percentageRegionalTrips.get() / totalTrips * 10000.0) / 100.0;

        nationalTripsPercentage = PercentageUtil.convertToTwoDecimalPlaces(nationalTripsPercentage);
        nationalExpressTripsPercentage = PercentageUtil.convertToTwoDecimalPlaces(nationalExpressTripsPercentage);
        subUrbanTripsPercentage = PercentageUtil.convertToTwoDecimalPlaces(subUrbanTripsPercentage);
        regionalExpressTripsPercentage = PercentageUtil.convertToTwoDecimalPlaces(regionalExpressTripsPercentage);
        regionalTripsPercentage = PercentageUtil.convertToTwoDecimalPlaces(regionalTripsPercentage);

        DataSeries nationalRegionalSeries = new DataSeries();
        nationalRegionalSeries.add(new DataSeriesItem("Fernverkehr", nationalTripsPercentage));
        nationalRegionalSeries.add(new DataSeriesItem("Fernverkehr (Express)", nationalExpressTripsPercentage));
        nationalRegionalSeries.add(new DataSeriesItem("Nahverkehr (Regional)", regionalTripsPercentage));
        nationalRegionalSeries.add(new DataSeriesItem("Nahverkehr (RegionalExpress)", regionalExpressTripsPercentage));
        nationalRegionalSeries.add(new DataSeriesItem("Nahverkehr (S-Bahn)", subUrbanTripsPercentage));

        return nationalRegionalSeries;
    }

    /**
     * Calculate the daily trip counts.
     *
     * @param dailyTripCounts the daily trip counts to calculate the series from
     * @return the DataSeries with the daily trip counts
     */
    public DataSeries buildDailyTripCountSeries(Map<LocalDate, Integer> dailyTripCounts) {
        DataSeries dailyTripCountSeries = new DataSeries();
        // Parallel stream for processing entries concurrently
        dailyTripCounts.forEach((key, value) -> dailyTripCountSeries.add(new DataSeriesItem(
                key.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                value)));
        return dailyTripCountSeries;
    }

    /**
     * Calculate the hourly trip counts.
     *
     * @param hourlyTripCounts the hourly trip counts to calculate the series from
     * @return the DataSeries with the hourly trip counts
     */
    public DataSeries buildHourlyTripCountSeries(Map<LocalDateTime, Integer> hourlyTripCounts) {
        DataSeries hourlyTripCountSeries = new DataSeries();
        // Parallel stream for processing entries concurrently
        hourlyTripCounts.forEach((key, value) -> hourlyTripCountSeries.add(new DataSeriesItem(
                key.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                value)));
        return hourlyTripCountSeries;
    }

    /**
     * Count the trips for a specific time period.
     *
     * @param trips the trips to count
     * @param from  the start of the time period
     * @param to    the end of the time period
     * @return the trip counts for the specific time period
     */
    public TripCounts countTrips(List<Trip> trips, LocalDateTime from, LocalDateTime to) {
        HashMap<LocalDate, Integer> dailyTripCounts = new HashMap<>();
        HashMap<LocalDate, Integer> dailyTripLongDistanceCounts = new HashMap<>();
        HashMap<LocalDate, Integer> dailyTripRegionalCounts = new HashMap<>();

        HashMap<LocalDateTime, Integer> hourlyTripCounts = new HashMap<>();
        HashMap<LocalDateTime, Integer> hourlyTripLongDistanceCounts = new HashMap<>();
        HashMap<LocalDateTime, Integer> hourlyTripRegionalCounts = new HashMap<>();

        for (Trip trip : trips) {
            LocalDateTime date;
            if (trip.getPlannedWhen() != null) {
                date = trip.getPlannedWhen();
            } else if (trip.getWhen() != null) {
                date = trip.getWhen();
            } else {
                date = trip.getCreatedAt();
            }

            if (date != null) {
                LocalDate localDate = date.toLocalDate();
                if (from.toLocalDate().until(to.toLocalDate()).getDays() > 3) {
                    if (!dailyTripCounts.containsKey(localDate)) {
                        dailyTripCounts.put(localDate, 0); // Füge einen neuen Eintrag hinzu, falls keiner vorhanden ist
                    }
                    dailyTripCounts.put(localDate, dailyTripCounts.get(localDate) + 1);
                    if (trip.getLine().getProduct().contains("national")) {
                        if (!dailyTripLongDistanceCounts.containsKey(localDate)) {
                            dailyTripLongDistanceCounts.put(localDate, 0); // Füge einen neuen Eintrag hinzu, falls keiner vorhanden ist
                        }
                        dailyTripLongDistanceCounts.put(localDate, dailyTripLongDistanceCounts.get(localDate) + 1);
                    } else {
                        if (!dailyTripRegionalCounts.containsKey(localDate)) {
                            dailyTripRegionalCounts.put(localDate, 0); // Füge einen neuen Eintrag hinzu, falls keiner vorhanden ist
                        }
                        dailyTripRegionalCounts.put(localDate, dailyTripRegionalCounts.get(localDate) + 1);
                    }
                } else {
                    date = date.withMinute(0).withSecond(0).withNano(0);
                    if (!hourlyTripCounts.containsKey(date)) {
                        hourlyTripCounts.put(date, 0); // Füge einen neuen Eintrag hinzu, falls keiner vorhanden ist
                    }
                    hourlyTripCounts.put(date, hourlyTripCounts.get(date) + 1);
                    if (trip.getLine().getProduct().contains("national")) {
                        if (!hourlyTripLongDistanceCounts.containsKey(date)) {
                            hourlyTripLongDistanceCounts.put(date, 0); // Füge einen neuen Eintrag hinzu, falls keiner vorhanden ist
                        }
                        hourlyTripLongDistanceCounts.put(date, hourlyTripLongDistanceCounts.get(date) + 1);
                    } else {
                        if (!hourlyTripRegionalCounts.containsKey(date)) {
                            hourlyTripRegionalCounts.put(date, 0); // Füge einen neuen Eintrag hinzu, falls keiner vorhanden ist
                        }
                        hourlyTripRegionalCounts.put(date, hourlyTripRegionalCounts.get(date) + 1);
                    }
                }
            }
        }

        TripCounts tripCounts = new TripCounts();
        tripCounts.setDailyTripCounts(new HashMap<>(dailyTripCounts));
        tripCounts.setDailyTripLongDistanceCounts(new HashMap<>(dailyTripLongDistanceCounts));
        tripCounts.setDailyTripRegionalCounts(new HashMap<>(dailyTripRegionalCounts));
        tripCounts.setHourlyTripCounts(new HashMap<>(hourlyTripCounts));
        tripCounts.setHourlyTripLongDistanceCounts(new HashMap<>(hourlyTripLongDistanceCounts));
        tripCounts.setHourlyTripRegionalCounts(new HashMap<>(hourlyTripRegionalCounts));

        return tripCounts;
    }

    /**
     * Calculate the trip statistics for a specific time period.
     *
     * @param trips the trips to calculate the statistics from
     * @return the trip statistics for the specific time period
     */
    public TripStatistics calculateTripStatistics(List<Trip> trips) {

        double totalTrips = trips.size();

        AtomicInteger cancelledTrips = new AtomicInteger();
        AtomicInteger delayedTrips = new AtomicInteger();

        trips.parallelStream().forEach(trip -> {
            if (trip.getCancelled() != null && trip.getCancelled()) {
                cancelledTrips.incrementAndGet();
            } else if (trip.getDelay() != null && trip.getDelay() >= 360) {
                delayedTrips.incrementAndGet();
            }
        });

        double percentageCancelled = calculatePercentage(cancelledTrips.get(), totalTrips);
        double percentageDelayed = calculatePercentage(delayedTrips.get(), totalTrips);
        double percentageOnTime = PercentageUtil.convertToTwoDecimalPlaces(100 - percentageCancelled - percentageDelayed);

        return new TripStatistics(percentageCancelled, percentageDelayed, percentageOnTime);
    }

}
