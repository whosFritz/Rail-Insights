package de.whosfritz.railinsights.data.services.trip_services;

import de.olech2412.adapter.dbadapter.APIConfiguration;
import de.olech2412.adapter.dbadapter.DB_Adapter_v6;
import de.olech2412.adapter.dbadapter.exception.Error;
import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.journey.sub.Leg;
import de.olech2412.adapter.dbadapter.model.station.Station;
import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.olech2412.adapter.dbadapter.model.stop.sub.Line;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.olech2412.adapter.dbadapter.model.trip.sub.Remark;
import de.whosfritz.railinsights.data.repositories.trip_repositories.TripsRepository;
import de.whosfritz.railinsights.data.services.LineService;
import de.whosfritz.railinsights.data.services.station_services.StationService;
import de.whosfritz.railinsights.data.services.stop_services.StopService;
import de.whosfritz.railinsights.data.services.trip_services.sub.RemarkService;
import de.whosfritz.railinsights.exception.JPAError;
import de.whosfritz.railinsights.exception.JPAErrors;
import de.whosfritz.railinsights.utils.TripUtil;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class TripService {

    @Autowired
    private TripsRepository tripsRepository;

    @Autowired
    private StationService stationService;

    @Autowired
    private StopService stopService;

    @Autowired
    private LineService lineService;

    @Autowired
    private RemarkService remarkService;

    /**
     * Deletes all {@link Trip} objects.
     *
     * @param trips the trips to delete
     */
    public void deleteAll(List<Trip> trips) {
        tripsRepository.deleteAll(trips);
    }

    public Result<Trip, JPAError> isAlreadyInDatabase(Trip trip) {
        try {
            Optional<Trip> tripOptional = tripsRepository.findByTripIdAndStop(trip.getTripId(), trip.getStop());

            return tripOptional.<Result<Trip, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if trip is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("Trip: " + trip.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Deletes the {@link Trip} object with the given id.
     *
     * @param id the id of the trip to delete
     */
    public Result<Trip, JPAError> deleteTripById(Long id) {
        try {
            Trip trip = tripsRepository.findById(id).orElse(null);

            if (trip == null)
                return Result.error(new JPAError(JPAErrors.NOT_FOUND));

            tripsRepository.deleteById(id);
            return Result.success(trip);
        } catch (Exception e) {
            log.error("Error while deleting trip: " + e.getMessage() + " " + e.getCause());
            log.error("Trip id: " + id);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    @Transactional
    public Result<Trip, JPAError> updateTrip(Trip trip) {
        try {
            Trip updatedTrip = tripsRepository.save(trip);
            return Result.success(updatedTrip);
        } catch (Exception e) {
            log.error("Error while updating trip: " + e.getMessage() + " " + e.getCause());
            log.error("Trip: " + trip.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Returns all {@link Trip} objects.
     *
     * @return all trips
     */
    public List<Trip> getAllTrips() {
        return tripsRepository.findAll();
    }

    /**
     * Returns all {@link Trip} objects with the given stop.
     *
     * @param stop the stop to search for
     * @return all trips with the given stop
     */
    public List<Trip> getAllTripsByStop(Stop stop) {
        return tripsRepository.findAllByStop(stop).orElse(null);
    }

    /**
     * Returns the {@link Trip} object with the given id.
     *
     * @param id the id of the remark to return
     * @return the trip with the given id
     */
    public Trip getTripById(Long id) {
        return tripsRepository.findById(id).orElse(null);
    }

    /**
     * Saves the given {@link Trip} object.
     *
     * @param trip the trip to save
     */
    @Transactional
    public Result<Trip, JPAError> saveTrip(Trip trip) {
        try {
            Result<Stop, JPAError> stopFromDB = stopService.findStopByStopId(trip.getStop().getStopId());
            boolean tripHasValidStop = false;
            if (!stopFromDB.isSuccess()) {
                Stop stopToSave = trip.getStop();
                // check if stop is really not in a database
                if (stopService.findByName(stopToSave.getName()).isEmpty()) {
                    // check if stop_id starts with 80
                    if (stopToSave.getStopId() > 8000000 && stopToSave.getStopId() < 8100000) {
                        stopToSave.setStation(null); // at this point we cant be sure that the stop is complete -> so ignore and care later
                        tripHasValidStop = true;
                        stopFromDB = stopService.saveStop(trip.getStop());
                    }
                } else {
                    Stop stop = stopService.findByName(stopToSave.getName()).get(0);
                    stopFromDB = Result.success(stop);

                }
            }

            trip.setStop(stopFromDB.getData());

            try {
                Result<Stop, JPAError> destinationStopFromDB = stopService.findStopByStopId(trip.getDestination().getStopId());
                if (!destinationStopFromDB.isSuccess()) {
                    Stop stopToSave = trip.getDestination();
                    if (stopService.findByName(stopToSave.getName()).isEmpty()) {
                        if (stopToSave.getStopId() > 8000000 && stopToSave.getStopId() < 8100000) {
                            stopToSave.setStation(null); // at this point we cant be sure that the stop is complete -> so ignore and care later
                            tripHasValidStop = true;
                            destinationStopFromDB = stopService.saveStop(trip.getDestination());
                        }
                    } else {
                        Stop stop = stopService.findByName(stopToSave.getName()).get(0);
                        destinationStopFromDB = Result.success(stop);
                    }
                }
                trip.setDestination(destinationStopFromDB.getData());
            } catch (
                    Exception exception) { // that's a bad practice, but we have to catch the exception here probably the trip is an arrival
                log.debug("Unable to process destination stop: " + exception.getMessage() + " " + exception.getCause());
            }

            try {
                Result<Stop, JPAError> originStopFromDB = stopService.findStopByStopId(trip.getOrigin().getStopId());
                if (!originStopFromDB.isSuccess()) {
                    Stop stopToSave = trip.getOrigin();
                    if (stopService.findByName(stopToSave.getName()).isEmpty()) {
                        if (stopToSave.getStopId() > 8000000 && stopToSave.getStopId() < 8100000) {
                            stopToSave.setStation(null); // at this point we cant be sure that the stop is complete -> so ignore and care later
                            tripHasValidStop = true;
                            originStopFromDB = stopService.saveStop(trip.getOrigin());
                        }
                    }
                }
                trip.setOrigin(originStopFromDB.getData());
            } catch (
                    Exception exception) { // that's a bad practice, but we have to catch the exception here probably the trip is an arrival
                log.debug("Unable to process destination stop: " + exception.getMessage() + " " + exception.getCause());
            }

            try {
                // now its save to work with the stop objects
                if (trip.getStop().getStation() != null && tripHasValidStop) {
                    Station station = trip.getStop().getStation();

                    Result<Station, JPAError> stationFromDB = stationService.findStationByStationId(station.getStationId());

                    if (stationFromDB.isSuccess()) {
                        if (stopFromDB.isSuccess()) {
                            // if the station and the stop are already in the database we have to check if the stop has the station set
                            if (stopFromDB.getData().getStation() == null) {
                                // if the stop does not have the station set we have to set it
                                stopFromDB.getData().setStation(stationFromDB.getData());
                                stopService.updateStop(stopFromDB.getData());
                            }
                        }
                        // if the station is not in the database we have to save it
                    } else {
                        // the station is not in the database so we have to save it
                        // sometimes the stop object does not provide all information so better ask the api
                        APIConfiguration apiConfiguration = new APIConfiguration();
                        apiConfiguration.setBaseUrl("http://localhost:3000");
                        DB_Adapter_v6 db_adapter_v6 = new DB_Adapter_v6(apiConfiguration);

                        Result<Station, Error> stationFromAPI = db_adapter_v6.getStationById(Math.toIntExact(station.getStationId()), Collections.emptyList());

                        if (stationFromAPI.isSuccess()) {
                            station = stationFromAPI.getData();
                            Result<Station, JPAError> stationFromDB2 = stationService.saveStation(station);
                            if (stationFromDB2.isSuccess()) {
                                station = stationFromDB2.getData();
                            } else {
                                log.error("Could not save station: " + stationFromDB2.getError().getMessage());
                            }
                            trip.getStop().setStation(station);
                        } else {
                            log.error("Could not fetch station from api: " + stationFromAPI.getError().getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                log.debug("Unable to process station: " + e.getMessage() + " " + e.getCause());
            }


            if (trip.getLine() != null) {
                Line line = trip.getLine();
                Result<Line, JPAError> lineFromDB = lineService.findByLineIdAndFahrtNr(line.getLineId(), line.getFahrtNr());
                if (lineFromDB.isSuccess()) {
                    trip.setLine(lineFromDB.getData());
                } else {
                    Result<Line, JPAError> result = lineService.saveLine(line);
                    if (result.isSuccess()) {
                        trip.setLine(result.getData());
                    } else {
                        log.error("Error while saving line: " + result.getError().getMessage());
                    }
                }
            }

            if (trip.getRemarks() != null) {
                List<Remark> remarks = new ArrayList<>();
                trip.getRemarks().forEach(remark -> {
                    if (remark.getText() != null) {
                        Result<Remark, JPAError> remarkFromDatabase = remarkService.isAlreadyInDatabase(remark);
                        if (!remarkFromDatabase.isSuccess()) {
                            Result<Remark, JPAError> result = remarkService.saveRemark(remark);
                            if (result.isSuccess()) {
                                remarks.add(result.getData());
                            } else {
                                log.error("Error while saving remark: " + result.getError().getMessage());
                            }
                        } else {
                            Result<Remark, JPAError> result = remarkService.isAlreadyInDatabase(remark);
                            if (result.isSuccess()) {
                                remarks.add(result.getData());
                            } else {
                                log.error("Error while receive remark: " + result.getError().getMessage());
                            }
                        }
                    }
                });
                trip.setRemarks(remarks);
            }

            Pageable pageable = PageRequest.of(0, 90000); // Begrenze auf 90.000 Ergebnisse
            Optional<List<Trip>> trips = tripsRepository.findAllByTripIdAndStopAndCreatedAtAfter(trip.getTripId(), stopFromDB.getData(), LocalDateTime.now().minusHours(48), pageable);
            trips.ifPresent(tripList -> tripsRepository.deleteAll(tripList));

            tripsRepository.save(trip);
            return Result.success(trip);

        } catch (Exception e) {
            log.error("Error while saving trip: " + e.getMessage() + " " + e.getCause());
            log.error("Trip: " + trip.toString());
            throw e;
        }
    }

    @Transactional
    public Result<List<Trip>, JPAError> findAllByStopAndPlannedWhenAfterAndPlannedWhenBefore(Stop stop, LocalDateTime start, LocalDateTime end) {
        try {
            // Ensure that also the lazy loaded objects are loaded
            Optional<List<Trip>> trips = tripsRepository.findAllByStopAndPlannedWhenAfterAndPlannedWhenBefore(stop, start, end);
            for (Trip t : trips.get()) {
                Hibernate.initialize(t.getRemarks());
            }

            List<Trip> cleanedTrips = TripUtil.removeDuplicatesForMultipleLines(trips.get());
            trips = Optional.of(cleanedTrips);

            return trips.<Result<List<Trip>, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while finding trip by stop and when after and when before: " + e.getMessage() + " " + e.getCause());
            log.error("Stop: " + stop.toString());
            log.error("Start: " + start.toString());
            log.error("End: " + end.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    @Transactional
    public Result<List<Trip>, JPAError> findAllByPlannedWhenIsAfterAndPlannedWhenIsBeforeAndLine_Name(LocalDateTime plannedWhenAfter, LocalDateTime plannedWhenBefore, String name) {
        try {
            Optional<List<Trip>> trip = tripsRepository.findAllByPlannedWhenIsAfterAndPlannedWhenIsBeforeAndLineName(plannedWhenAfter, plannedWhenBefore, name);
            return trip.<Result<List<Trip>, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while finding trip by planned when is after and planned when is before and line fahrt nr: " + e.getMessage() + " " + e.getCause());
            log.error("Planned when after: " + plannedWhenAfter.toString());
            log.error("Planned when before: " + plannedWhenBefore.toString());
            log.error("FahrtNr: " + name);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    @Transactional
    public Result<List<Trip>, JPAError> findAllByStop(Stop stop) {
        try {
            Optional<List<Trip>> trip = tripsRepository.findAllByStop(stop);
            return trip.<Result<List<Trip>, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while finding trip by stop: " + e.getMessage() + " " + e.getCause());
            log.error("Stop: " + stop.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    @Transactional
    public Result<List<Trip>, JPAError> findAllByTripId(String tripId) {
        try {
            Optional<List<Trip>> trip = tripsRepository.findAllByTripId(tripId);
            for (Trip t : trip.get()) {
                Hibernate.initialize(t.getRemarks());
            }
            return trip.<Result<List<Trip>, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while finding trip by trip id: " + e.getMessage() + " " + e.getCause());
            log.error("Trip id: " + tripId);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    @Transactional
    public Result<List<Trip>, JPAError> findAllTripsByLeg(Leg leg) {
        try {
            Optional<List<Trip>> trips = tripsRepository.findAllByLineLineId(leg.getLine().getLineId());
            return trips.<Result<List<Trip>, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while finding trip by leg: " + e.getMessage() + " " + e.getCause());
            log.error("Stop: " + leg.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    @Transactional
    public Result<List<Trip>, JPAError> findAllByLineNameAndTripIdContains(String lineName, String tripId) {
        try {
            Optional<List<Trip>> trips = tripsRepository.findAllByLineNameAndTripIdContains(lineName, tripId);
            for (Trip t : trips.get()) {
                Hibernate.initialize(t.getRemarks());
            }
            return trips.<Result<List<Trip>, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while finding trip by line name and trip id contains: " + e.getMessage() + " " + e.getCause());
            log.error("Line name: " + lineName);
            log.error("Trip id: " + tripId);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }
}