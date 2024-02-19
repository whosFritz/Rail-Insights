package de.whosfritz.railinsights.utils;

import de.olech2412.adapter.dbadapter.model.trip.Trip;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Make your life easier with {@link TripUtil}
 * it provides some common tasks for trips
 */
public class TripUtil {

    public static LocalDateTime getDateFromTrip(Trip trip) {
        if (trip.getPlannedWhen() != null) {
            return trip.getPlannedWhen();
        } else if (trip.getWhen() != null) {
            return trip.getWhen();
        } else {
            return trip.getCreatedAt();
        }
    }

    /**
     * Removes duplicates from a list of trips
     *
     * @param trips the list of trips
     * @return a list of trips without duplicates
     */
    public static List<Trip> removeDuplicates(List<Trip> trips) {
        List<Trip> doubledTrips = new ArrayList<>();
        for (int i = 0; i < trips.size(); i++) { // loop through all trips
            for (int j = i + 1; j < trips.size(); j++) { // loop through all trips after the current trip
                if (trips.get(i).getLine().getFahrtNr().equals(trips.get(j).getLine().getFahrtNr())
                        && trips.get(i).getPlannedWhen().equals(trips.get(j).getPlannedWhen())) {
                    doubledTrips.add(trips.get(j));
                }
            }
        }
        trips = new ArrayList<>(trips); // Create a mutable copy important for removing elements because the list is unmodifiable
        if (!doubledTrips.isEmpty()) {
            trips.removeAll(doubledTrips);
        }
        return trips;
    }

}
