package de.whosfritz.railinsights.ui.services;

import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.whosfritz.railinsights.data.dto.StopDto;
import de.whosfritz.railinsights.data.repositories.stop_repositories.StopRepository;
import de.whosfritz.railinsights.data.repositories.trip_repositories.TripsRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
@Log4j2
@EnableAsync
@EnableScheduling
public class DataProviderService {

    DataProviderServiceState state = DataProviderServiceState.PENDING;

    List<Trip> allTrips = new ArrayList<>();

    List<Stop> allStops = new ArrayList<>();
    Double stopsPercentageOnTime;
    Double stopsPercentageDelayed;
    Double stopsPercentageCancelled;
    @Autowired
    private TripsRepository tripsRepository;
    @Autowired
    private StopRepository stopRepository;

    public DataProviderService() {
    }

    /**
     * Run the data calculation every 20 minutes.
     */
    @Scheduled(cron = "0 0/20 * * * ?")
    @Async
    public void calculateData() {
        state = DataProviderServiceState.PENDING; // Set the state to pending
        log.info("Data calculation started...");
        allTrips = tripsRepository.findAll();
        allStops = stopRepository.findAll();

        DecimalFormat df = new DecimalFormat("#.##");
        df.setGroupingUsed(false);

        double percentageCancelled = ((double) allTrips.stream().filter(trip -> {
            if (trip.getCancelled() != null) {
                return trip.getCancelled();
            }
            return false;
        }).count() / allTrips.size());

        stopsPercentageCancelled = (Math.round(percentageCancelled * 100.0) / 100.0) * 100;
        String formatted = df.format(stopsPercentageCancelled);
        stopsPercentageCancelled = Double.parseDouble(formatted);

        double percentageDelayed = ((double) allTrips.stream().filter(trip -> {
            if (trip.getCancelled() == null || !trip.getCancelled()) {
                if (trip.getDelay() != null) {
                    return trip.getDelay() >= 300;
                }
            }
            return false;
        }).count() / allTrips.size());

        stopsPercentageDelayed = (Math.round(percentageDelayed * 100.0) / 100.0) * 100;
        formatted = df.format(stopsPercentageDelayed);
        stopsPercentageDelayed = Double.parseDouble(formatted);

        double percentageOnTime = (1 - percentageCancelled - percentageDelayed);

        stopsPercentageOnTime = (Math.round(percentageOnTime * 100.0) / 100.0) * 100;
        formatted = df.format(stopsPercentageOnTime);
        stopsPercentageOnTime = Double.parseDouble(formatted);

        log.info("Data calculation finished...");
        state = DataProviderServiceState.READY; // Set the state to ready
    }

    /**
     * Get all trips for a specific local date.
     * If the trip has a "when" date, it will be used otherwise the "plannedWhen" date will be used and if that is also null, the "createdAt" date will be used.
     *
     * @param localDate the local date to get the trips for
     * @return a list of trips for the given local date
     */
    public List<Trip> getAllTripsForLocalDate(java.time.LocalDate localDate) {
        return allTrips.stream().filter(trip -> {
            if (trip.getWhen() != null) {
                return trip.getWhen().toLocalDate().isEqual(localDate);
            } else if (trip.getPlannedWhen() != null) {
                return trip.getPlannedWhen().toLocalDate().isEqual(localDate);
            } else {
                return trip.getCreatedAt().toLocalDate().isEqual(localDate);
            }
        }).toList();
    }

    /**
     * Get all trips for a specific local date range.
     *
     * @param startDate the start date of the range
     * @param endDate   the end date of the range
     * @return a list of trips for the given local date range
     */
    public List<Trip> getAllTripsForLocalDateInRange(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        return allTrips.stream().filter(trip -> {
            if (trip.getWhen() != null) {
                return trip.getWhen().toLocalDate().isAfter(startDate) && trip.getWhen().toLocalDate().isBefore(endDate);
            } else if (trip.getPlannedWhen() != null) {
                return trip.getPlannedWhen().toLocalDate().isAfter(startDate) && trip.getPlannedWhen().toLocalDate().isBefore(endDate);
            } else {
                return trip.getCreatedAt().toLocalDate().isAfter(startDate) && trip.getCreatedAt().toLocalDate().isBefore(endDate);
            }
        }).toList();
    }

    /**
     * Get all trips for a specific trip id.
     *
     * @param tripId the trip id to get the trips for
     * @return a list of trips for the given trip id
     */
    public List<Trip> getAllTripsForSpecificTripId(String tripId) {
        return allTrips.stream().filter(trip -> trip.getTripId().equals(tripId)).toList();
    }

    /**
     * Get all trips with distinct trip id.
     *
     * @return a list of trips for the given trip id
     */
    public List<Trip> getAllTripsWithDistinctTripId() {
        return allTrips.stream().distinct().toList();
    }

    /**
     * Get all trips with distinct trip id and local date.
     *
     * @param localDate the local date to get the trips for
     * @return a list of trips for the given local date
     */
    public List<Trip> getAllTripsWithDistinctTripIdAndLocalDate(LocalDate localDate) {
        return getAllTripsForLocalDate(localDate).stream().distinct().toList();
    }

    /**
     * Get all stops as DTOs.
     *
     * @return the converted stopDTOs list
     */
    public List<StopDto> getAllStopsConvertedToDto() {
        List<StopDto> stopDtos = new ArrayList<>();
        allStops.forEach(stop -> stopDtos.add(new StopDto(stop.getStopId().toString(), stop.getName(), stop.getLocation().getLatitude(), stop.getLocation().getLongitude(), stop.getStation())));
        return stopDtos;
    }

    /**
     * Get all national stops as DTOs.
     *
     * @return the converted stopDTOs list
     */
    public List<StopDto> getAllNationalStopsConvertedToDto() {
        List<StopDto> stopDtos = new ArrayList<>();
        allStops.stream().filter(stop -> stop.getProducts().isNational()).toList().forEach(stop ->
                stopDtos.add(new StopDto(stop.getStopId().toString(), stop.getName(), stop.getLocation().getLatitude(), stop.getLocation().getLongitude(), stop.getStation())));
        return stopDtos;
    }

}
