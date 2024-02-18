package de.whosfritz.railinsights.data.dataprovider;

import de.olech2412.adapter.dbadapter.model.trip.Trip;

public class TripFilter {
    private String searchTerm;

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public boolean test(Trip trip) {
        return matchesAny(
                trip.getLine() != null ? trip.getLine().getName() : null,
                trip.getDirection(), trip.getTripId()
        );
    }

    private boolean matchesAny(String... values) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return true;
        }
        for (String value : values) {
            if (value != null && value.toLowerCase().contains(searchTerm.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
