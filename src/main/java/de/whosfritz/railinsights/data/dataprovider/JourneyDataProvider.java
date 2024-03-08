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
import de.whosfritz.railinsights.utils.TripUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
                // if the stopovers are null its not possible to filter the trips probably this is a part of the journey by foot
                if (leg.getStopovers() == null) {
                    continue;
                }
                List<Stop> stops = leg.getStopovers().parallelStream().map(Stopover::getStop).toList();
                List<String> stopNames = stops.parallelStream().map(Stop::getName).toList();
                // filter out all trips where the stop is null
                trips.removeIf(trip -> trip.getStop() == null);
                trips.removeIf(trip -> !stopNames.contains(trip.getStop().getName()) && !Objects.equals(leg.getDestination().getName(), trip.getStop().getName()) && !Objects.equals(leg.getOrigin().getName(), trip.getStop().getName()));
                // remove all trips where the

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
                LocalDateTime legPlannedDeparture = LocalDateTime.parse(leg.getPlannedDeparture(), formatter);

                // remove all trips where week day is not the same with the leg
                trips.removeIf(trip -> {
                    LocalDateTime tripDeparture = trip.getPlannedWhen();
                    return tripDeparture.getDayOfWeek() != legPlannedDeparture.getDayOfWeek();
                });


                List<LocalDate> localDates = trips.parallelStream().map(trip -> trip.getPlannedWhen().toLocalDate()).distinct().toList();
                List<Trip> uniqueTrips = new ArrayList<>();
                for (LocalDate localDate : localDates){
                    List<Trip> tripsForLocalDate = trips.parallelStream().filter(trip -> trip.getPlannedWhen().toLocalDate().equals(localDate)).toList();
                    uniqueTrips.addAll(TripUtil.removeDuplicates(tripsForLocalDate));
                }
                trips = uniqueTrips;

                oldTrips.put(count, trips);
            }
        }

        return oldTrips;
    }

}
