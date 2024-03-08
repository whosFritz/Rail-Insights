package de.whosfritz.railinsights.calculation;

import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.whosfritz.railinsights.data.LoadFactor;
import de.whosfritz.railinsights.data.dto.TripCounts;
import de.whosfritz.railinsights.data.dto.TripStatistics;
import de.whosfritz.railinsights.utils.PercentageUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
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

    public static String minutesToHoursAndMinutesAndSeconds(double totalMinutes) {
        if (totalMinutes == 0.0) {
            return "0 Minuten";
        }

        int hours = (int) (totalMinutes / 60);
        double remainingMinutes = totalMinutes % 60;

        int minutes = (int) remainingMinutes;
        int seconds = (int) Math.round((remainingMinutes - minutes) * 60);

        StringBuilder time = new StringBuilder();
        if (hours > 0) {
            time.append(hours).append("h ");
        }
        if (minutes > 0) {
            time.append(minutes).append("m ");
        }
        if (seconds > 0) {
            time.append(seconds).append("s");
        }

        return time.toString().trim();
    }

    public static LoadFactor getLoadFactorAsEnum(String loadFactor) {
        if (loadFactor == null) {
            return LoadFactor.KEINE;
        }
        String lowerCaseLoadFactor = loadFactor.toLowerCase();
        return switch (lowerCaseLoadFactor) {
            case "low-to-medium" -> LoadFactor.WENIG_BIS_NORMAL;
            case "high" -> LoadFactor.HOCH;
            case "very-high" -> LoadFactor.SEHR_HOCH;
            default -> LoadFactor.valueOf(lowerCaseLoadFactor);
        };
    }

    public static DataSeries buildDailyHighestLoadFactorSeries(List<Trip> tripsCorrespondingToLine) {
        Map<LocalDate, List<LoadFactor>> loadFactorByDay = new TreeMap<>();

        // Step 2: Iterate over the trips
        for (Trip trip : tripsCorrespondingToLine) {
            // Step 3: Get the plannedWhen date and the loadFactor
            LocalDate date = trip.getPlannedWhen().toLocalDate();
            LoadFactor loadFactor = getLoadFactorAsEnum(trip.getLoadFactor());

            // Step 4: Add the loadFactor to the corresponding date in the map
            loadFactorByDay.computeIfAbsent(date, k -> new ArrayList<>()).add(loadFactor);
        }

        DataSeries dailyHighestLoadFactorSeries = new DataSeries();
        dailyHighestLoadFactorSeries.setName("Höchste Auslastung");

        // Step 5: Iterate over the map
        for (Map.Entry<LocalDate, List<LoadFactor>> entry : loadFactorByDay.entrySet()) {
            LocalDate date = entry.getKey();
            List<LoadFactor> loadFactors = entry.getValue();

            // Step 6: Find the highest LoadFactor in the ArrayList
            LoadFactor highestLoadFactor = Collections.max(loadFactors);

            // Create a DataSeriesItem with the date and the highest LoadFactor
            DataSeriesItem item = new DataSeriesItem(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant(), highestLoadFactor.ordinal());

            // Step 7: Add the DataSeriesItem to the DataSeries
            dailyHighestLoadFactorSeries.add(item);
        }

        return dailyHighestLoadFactorSeries;
    }

    public static DataSeries buildDailyLowestLoadFactorSeries(List<Trip> tripsCorrespondingToLine) {


        Map<LocalDate, List<LoadFactor>> loadFactorByDay = new TreeMap<>();

        // Step 2: Iterate over the trips
        for (Trip trip : tripsCorrespondingToLine) {
            // Step 3: Get the plannedWhen date and the loadFactor
            LocalDate date = trip.getPlannedWhen().toLocalDate();
            LoadFactor loadFactor = getLoadFactorAsEnum(trip.getLoadFactor());

            // Step 4: Add the loadFactor to the corresponding date in the map
            loadFactorByDay.computeIfAbsent(date, k -> new ArrayList<>()).add(loadFactor);
        }

        DataSeries dailyLowestLoadFactorSeries = new DataSeries();
        dailyLowestLoadFactorSeries.setName("Geringste Auslastung");

        // Step 5: Iterate over the map
        for (Map.Entry<LocalDate, List<LoadFactor>> entry : loadFactorByDay.entrySet()) {
            LocalDate date = entry.getKey();
            List<LoadFactor> loadFactors = entry.getValue();

            // Step 6: Find the highest LoadFactor in the ArrayList
            LoadFactor lowestLoadFactor = Collections.min(loadFactors);

            // Create a DataSeriesItem with the date and the highest LoadFactor
            DataSeriesItem item = new DataSeriesItem(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant(), lowestLoadFactor.ordinal());

            // Step 7: Add the DataSeriesItem to the DataSeries
            dailyLowestLoadFactorSeries.add(item);
        }

        return dailyLowestLoadFactorSeries;
    }

    public static DataSeries buildDailyDelaySeries(List<Trip> tripsCorrespondingToLine) {
        Map<LocalDate, List<Integer>> delayByDay = new TreeMap<>();

        // Iterate over the trips
        for (Trip trip : tripsCorrespondingToLine) {
            // Get the plannedWhen date and the delay
            LocalDate date = trip.getPlannedWhen().toLocalDate();
            Integer delay = trip.getDelay();

            if (trip.getCancelled() != null) {
                continue;
            }
            delayByDay.computeIfAbsent(date, k -> new ArrayList<>()).add(delay >= 360 ? delay : 0);
        }

        DataSeries dailyDelaySeries = new DataSeries();
        dailyDelaySeries.setName("Durchschnittliche Verspätung");

        // Iterate over the map
        for (Map.Entry<LocalDate, List<Integer>> entry : delayByDay.entrySet()) {
            LocalDate date = entry.getKey();
            List<Integer> delays = entry.getValue();

            // Calculate the average delay in the ArrayList
            double averageDelay = delays.stream().mapToInt(Integer::intValue).average().orElse(0);

            // Create a DataSeriesItem with the date and the average delay
            DataSeriesItem item = new DataSeriesItem(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant(), PercentageUtil.convertToTwoDecimalPlaces(averageDelay / 60));

            // Add the DataSeriesItem to the DataSeries
            dailyDelaySeries.add(item);
        }

        return dailyDelaySeries;
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
                key.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant(),
                value)));

        dailyTripCountSeries.setName("Tägliche Anzahl an Fahrten");

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
        TreeMap<LocalDate, Integer> dailyTripCounts = new TreeMap<>();
        TreeMap<LocalDate, Integer> dailyTripLongDistanceCounts = new TreeMap<>();
        TreeMap<LocalDate, Integer> dailyTripRegionalCounts = new TreeMap<>();

        TreeMap<LocalDateTime, Integer> hourlyTripCounts = new TreeMap<>();
        TreeMap<LocalDateTime, Integer> hourlyTripLongDistanceCounts = new TreeMap<>();
        TreeMap<LocalDateTime, Integer> hourlyTripRegionalCounts = new TreeMap<>();

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

        //run through dailytripcounts if a date does not exist in longdistancecounts or regionalcounts fill up with 0 values
        for (LocalDate date : dailyTripCounts.keySet()) {
            if (!dailyTripLongDistanceCounts.containsKey(date)) {
                dailyTripLongDistanceCounts.put(date, 0);
            }
            if (!dailyTripRegionalCounts.containsKey(date)) {
                dailyTripRegionalCounts.put(date, 0);
            }
        }

        for (LocalDateTime date : hourlyTripCounts.keySet()) {
            if (!hourlyTripLongDistanceCounts.containsKey(date)) {
                hourlyTripLongDistanceCounts.put(date, 0);
            }
            if (!hourlyTripRegionalCounts.containsKey(date)) {
                hourlyTripRegionalCounts.put(date, 0);
            }
        }

        TripCounts tripCounts = new TripCounts();
        tripCounts.setDailyTripCounts(dailyTripCounts);
        tripCounts.setDailyTripLongDistanceCounts(dailyTripLongDistanceCounts);
        tripCounts.setDailyTripRegionalCounts(dailyTripRegionalCounts);
        tripCounts.setHourlyTripCounts(hourlyTripCounts);
        tripCounts.setHourlyTripLongDistanceCounts(hourlyTripLongDistanceCounts);
        tripCounts.setHourlyTripRegionalCounts(hourlyTripRegionalCounts);

        return tripCounts;
    }

    public TripCounts countTrips(List<Trip> trips) {
        // for every day count the trips
        HashMap<LocalDate, Integer> dailyTripCounts = new HashMap<>();
        trips.forEach(trip -> {
            // If the trip is cancelled, skip this iteration
            if (trip.getCancelled() != null && trip.getCancelled()) {
                return;
            }
            LocalDate date = trip.getPlannedWhen().toLocalDate();
            dailyTripCounts.put(date, dailyTripCounts.getOrDefault(date, 0) + 1);
        });
        TripCounts tripCounts = new TripCounts();
        tripCounts.setDailyTripCounts(dailyTripCounts);
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

        AtomicInteger delayMoreThan6min = new AtomicInteger();
        AtomicInteger delayMoreThan15min = new AtomicInteger();
        AtomicInteger delayMoreThan60min = new AtomicInteger();


        trips.parallelStream().forEach(trip -> {
            if (trip.getCancelled() != null && trip.getCancelled()) {
                cancelledTrips.incrementAndGet();
            } else if (trip.getDelay() != null && trip.getDelay() >= 360) {
                delayedTrips.incrementAndGet();
            }
            if (trip.getDelay() != null && trip.getDelay() >= 360) {
                delayMoreThan6min.incrementAndGet();
            }
            if (trip.getDelay() != null && trip.getDelay() >= 900) {
                delayMoreThan15min.incrementAndGet();
            }
            if (trip.getDelay() != null && trip.getDelay() >= 3600) {
                delayMoreThan60min.incrementAndGet();
            }
        });

        double percentageCancelled = calculatePercentage(cancelledTrips.get(), totalTrips);
        double percentageDelayed = calculatePercentage(delayedTrips.get(), totalTrips);
        double percentageOnTime = PercentageUtil.convertToTwoDecimalPlaces(100 - percentageCancelled - percentageDelayed);

        double delayMoreThan6minPercentage = calculatePercentage(delayMoreThan6min.get(), totalTrips);
        double delayMoreThan15minPercentage = calculatePercentage(delayMoreThan15min.get(), totalTrips);
        double delayMoreThan60minPercentage = calculatePercentage(delayMoreThan60min.get(), totalTrips);

        TripStatistics tripStatistics = new TripStatistics();
        tripStatistics.setPercentageOnTime(percentageOnTime);
        tripStatistics.setPercentageDelayed(percentageDelayed);
        tripStatistics.setPercentageCancelled(percentageCancelled);
        tripStatistics.setDelayMoreThan6min(delayMoreThan6minPercentage);
        tripStatistics.setDelayMoreThan15min(delayMoreThan15minPercentage);
        tripStatistics.setDelayMoreThan60min(delayMoreThan60minPercentage);

        return tripStatistics;
    }


}
