package de.whosfritz.railinsights.data.dataprovider;

import com.vaadin.flow.server.VaadinService;
import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.journey.Journey;
import de.olech2412.adapter.dbadapter.model.journey.sub.Leg;
import de.olech2412.adapter.dbadapter.model.journey.sub.Stopover;
import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.whosfritz.railinsights.data.services.trip_services.TripService;
import de.whosfritz.railinsights.exception.JPAError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JourneyDataProvider {


    TripService tripService = VaadinService.getCurrent().getInstantiator().getOrCreate(TripService.class);

    /**
     * Find old journeys from the database with the same lineId
     *
     * @param journey the journey
     * @return the list of journeys with the same lineId
     */
    public HashMap<Integer, List<Trip>> findTripsByJourney(Journey journey) {
        List<Leg> legs = journey.getLegs();

        HashMap<Integer, List<Trip>> oldTrips = new HashMap<>();
        int count = 0;
        for (Leg leg : legs) {
            Result<List<Trip>, JPAError> result = tripService.findAllTripsByLeg(leg);

            if (result.isSuccess()) {
                count++;

                // remove all trips where stop is not in stopovers and is not des
                List<Trip> trips = result.getData();
                List<Stop> stops = leg.getStopovers().parallelStream().map(Stopover::getStop).toList();
                List<Long> stopIds = stops.parallelStream().map(Stop::getStopId).toList();
                trips.removeIf(trip -> !stopIds.contains(trip.getStop().getStopId()) && !Objects.equals(leg.getDestination().getStopId(), trip.getStop().getStopId()) && !Objects.equals(leg.getOrigin().getStopId(), trip.getStop().getStopId()));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
                LocalDateTime legPlannedDeparture = LocalDateTime.parse(leg.getPlannedDeparture(), formatter);

                // remove all trips where week day is not the same with the leg
                trips.removeIf(trip -> {
                    LocalDateTime tripDeparture = trip.getPlannedWhen();
                    return tripDeparture.getDayOfWeek() != legPlannedDeparture.getDayOfWeek();
                });


                oldTrips.put(count, result.getData());
            }
        }

        return oldTrips;
    }

}
