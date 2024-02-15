package de.whosfritz.railinsights.data;

import de.olech2412.adapter.dbadapter.APIConfiguration;
import de.olech2412.adapter.dbadapter.DB_Adapter_v6;
import de.olech2412.adapter.dbadapter.exception.Error;
import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.olech2412.adapter.dbadapter.model.stop.sub.Line;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.olech2412.adapter.dbadapter.request.parameters.Parameter;
import de.olech2412.adapter.dbadapter.request.parameters.RequestParametersNames;
import de.whosfritz.railinsights.data.repositories.station_repositories.StationRepository;
import de.whosfritz.railinsights.data.services.LineService;
import de.whosfritz.railinsights.data.services.stop_services.StopService;
import de.whosfritz.railinsights.data.services.trip_services.TripService;
import de.whosfritz.railinsights.exception.JPAError;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
@EnableScheduling
public class DataDispatcher {

    @Autowired
    StopService stopService;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    TripService tripService;

    @Autowired
    LineService lineService;

    /**
     * Fetch all departures for all stops
     */
    @Scheduled(cron = "0 0/15 * * * ?")
    @Async
    public void fetchData() {
        log.info("---- Fetching departures ----");

        List<Stop> stops = stopService.findByProducts_National(true);

        if (!stops.isEmpty()) {
            log.info("Stops found: " + stops.size());

            APIConfiguration apiConfiguration = new APIConfiguration();
            apiConfiguration.setBaseUrl("http://localhost:3000");
            DB_Adapter_v6 db_adapter_v6 = new DB_Adapter_v6(apiConfiguration);

            for (int i = 0; i < stops.size(); i++) {
                try {

                    Result<Trip[], Error> arrivalsByStopId = db_adapter_v6.getArrivalsByStopId(Math.toIntExact(stops.get(i).getStopId()), new Parameter.ParameterBuilder()
                            .add(RequestParametersNames.NATIONAL, true)
                            .add(RequestParametersNames.NATIONAL_EXPRESS, true)
                            .add(RequestParametersNames.REGIONAL, true)
                            .add(RequestParametersNames.REGIONAL_EXPRESS, true)
                            .add(RequestParametersNames.SUBURBAN, true)
                            .add(RequestParametersNames.BUS, false)
                            .add(RequestParametersNames.TRAM, false)
                            .add(RequestParametersNames.FERRY, false)
                            .add(RequestParametersNames.SUBWAY, false)
                            .add(RequestParametersNames.TAXI, false)
                            .add(RequestParametersNames.RESULTS, 9999)
                            .add(RequestParametersNames.DURATION, 60)
                            .build());

                    Result<Trip[], Error> departuresByStopId = db_adapter_v6.getDeparturesByStopId(Math.toIntExact(stops.get(i).getStopId()), new Parameter.ParameterBuilder()
                            .add(RequestParametersNames.NATIONAL, true)
                            .add(RequestParametersNames.NATIONAL_EXPRESS, true)
                            .add(RequestParametersNames.REGIONAL, true)
                            .add(RequestParametersNames.REGIONAL_EXPRESS, true)
                            .add(RequestParametersNames.SUBURBAN, true)
                            .add(RequestParametersNames.BUS, false)
                            .add(RequestParametersNames.TRAM, false)
                            .add(RequestParametersNames.FERRY, false)
                            .add(RequestParametersNames.SUBWAY, false)
                            .add(RequestParametersNames.TAXI, false)
                            .add(RequestParametersNames.RESULTS, 9999)
                            .add(RequestParametersNames.DURATION, 60)
                            .build());

                    if (departuresByStopId.isSuccess() && arrivalsByStopId.isSuccess()) {
                        log.info("Arrivals found: " + arrivalsByStopId.getData().length + " for stop {}", stops.get(i).getName());
                        log.info("Departures found: " + departuresByStopId.getData().length + " for stop {}", stops.get(i).getName());
                        // merge arrays
                        Trip[] trips = new Trip[arrivalsByStopId.getData().length + departuresByStopId.getData().length];
                        System.arraycopy(arrivalsByStopId.getData(), 0, trips, 0, arrivalsByStopId.getData().length);
                        System.arraycopy(departuresByStopId.getData(), 0, trips, arrivalsByStopId.getData().length, departuresByStopId.getData().length);
                        for (Trip trip : trips) {
                            tripService.saveTrip(trip);
                        }
                    } else {
                        log.error("Error while fetching data for stop {}", stops.get(i).getName());
                    }
                } catch (Exception e) {
                    log.error("Error while fetching data for stop {}", stops.get(i).getName());
                    log.error(e.getMessage());
                }
            }
            log.info("---- Finished fetching departures ----");
        } else {
            log.error("No stops found. Please provide some stops in the database.");
        }
    }

    @Transactional
    public void fixTrips() {
        List<Line> lines = lineService.getAllLines();
        int totalLines = lines.size();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (Line line : lines) {
            executorService.submit(() -> {
                // show progress
                log.info("Processing line " + line.getFahrtNr() + " (" + lines.indexOf(line) + "/" + totalLines + ")");
                for (LocalDateTime date = LocalDateTime.parse("2024-01-29T00:00:00.000"); date.isBefore(LocalDateTime.now()); date = date.plusDays(1)) {
                    Result<List<Trip>, JPAError> result = tripService.findAllByPlannedWhenIsAfterAndPlannedWhenIsBeforeAndLine_FahrtNr(date.toLocalDate().atStartOfDay(), date.toLocalDate().atStartOfDay().plusDays(1), line.getFahrtNr());
                    if (result.isSuccess() && !result.getData().isEmpty()) {
                        List<Trip> trips = result.getData();
                        String originTripId = trips.get(0).getTripId();
                        String originLine = trips.get(0).getLine().getLineId();
                        List<Trip> theRealTrips = new ArrayList<>();
                        List<Trip> theTrashTrips = new ArrayList<>();

                        for (Trip trip : trips) {
                            if (!trip.getTripId().equals(originTripId) && trip.getLine().getLineId().equals(originLine)) {
                                if (theTrashTrips.contains(trip)) {
                                    continue;
                                }
                                log.error("TripId is not the same for all trips: " + originTripId + " on " + trip.getPlannedWhen() + " and " + trip.getLine().getFahrtNr());
                                Trip newestTrip = trips.stream().filter(t -> t.getStop().equals(trip.getStop())).max(Comparator.comparing(Trip::getCreatedAt)).get();
                                newestTrip.setTripId(originTripId);
                                theRealTrips.add(newestTrip);
                                theTrashTrips.addAll(trips.stream().filter(t -> t.getStop().equals(newestTrip.getStop())).toList());
                            }
                        }

                        if (!theRealTrips.isEmpty() && !theTrashTrips.isEmpty()) {
                            trips.removeAll(theTrashTrips);
                            trips.addAll(theRealTrips);
                            trips.forEach(tripService::updateTrip);
                        }
                    }
                }
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            log.error("Error occurred while waiting for threads to finish.", e);
            Thread.currentThread().interrupt();
        }
    }
}
