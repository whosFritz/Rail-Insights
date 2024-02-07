package de.whosfritz.railinsights.data.services.stop_services;

import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.olech2412.adapter.dbadapter.model.stop.sub.StopLocation;
import de.whosfritz.railinsights.data.repositories.stop_repositories.StopRepository;
import de.whosfritz.railinsights.data.services.station_services.StationService;
import de.whosfritz.railinsights.data.services.stop_services.sub.StopLocationService;
import de.whosfritz.railinsights.jpa.JPAError;
import de.whosfritz.railinsights.jpa.JPAErrors;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class StopService {

    @Autowired
    private StationService stationService;

    @Autowired
    private StopRepository stopRepository;

    @Autowired
    private StopLocationService stopLocationService;

    /**
     * Deletes all {@link Stop} objects.
     *
     * @param stops the stops to delete
     */
    public void deleteAll(List<Stop> stops) {
        stopRepository.deleteAll(stops);
    }

    public Result<Stop, JPAError> isAlreadyInDatabase(Stop stop) {
        try {
            Optional<Stop> stationOptional = stopRepository.findByStopId(stop.getStopId());

            return stationOptional.<Result<Stop, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if stop is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("Stop: " + stop.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Deletes the {@link Stop} object with the given id.
     *
     * @param id the id of the stop to delete
     */
    public Result<Stop, JPAError> deleteStopById(Long id) {
        try {
            Stop station = stopRepository.findById(id).orElse(null);

            if (station == null)
                return Result.error(new JPAError(JPAErrors.NOT_FOUND));

            stopRepository.deleteById(id);
            return Result.success(station);
        } catch (Exception e) {
            log.error("Error while deleting stop: " + e.getMessage() + " " + e.getCause());
            log.error("Stop id: " + id);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Returns all {@link Stop} objects.
     *
     * @return all stops
     */
    public List<Stop> getAllStops() {
        return stopRepository.findAll();
    }

    /**
     * Returns the {@link Stop} object with the given id.
     *
     * @param id the id of the stop to return
     * @return the stop with the given id
     */
    public Stop getStopById(Long id) {
        return stopRepository.findById(id).orElse(null);
    }

    /**
     * Saves the given {@link Stop} object.
     *
     * @param stop the address to save
     */
    @Transactional
    public Result<Stop, JPAError> saveStop(Stop stop) {
        try {
            if (!isAlreadyInDatabase(stop).isSuccess()) {
                if (!stopLocationService.saveStopLocation(stop.getLocation()).isSuccess()) {
                    StopLocation stopLocation = stopLocationService.isAlreadyInDatabase(stop.getLocation()).getData();
                    stop.setLocation(stopLocation);
                }
                if (stop.getStation() != null) {
                    stop.setStation(null);
                }

                stopRepository.save(stop);
            } else {
                return Result.error(new JPAError(JPAErrors.ALREADY_EXISTS));
            }
            return Result.success(stop);
        } catch (Exception e) {
            log.error("Error while saving stop: " + e.getMessage() + " " + e.getCause());
            log.error("Stop: " + stop.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Returns the {@link Stop} object with the given stopId.
     *
     * @param stopId the stopId of the stop to return
     * @return the stop with the given stopId
     */
    public Result<Stop, JPAError> findStopByStopId(Long stopId) {
        try {
            Optional<Stop> stopOptional = stopRepository.findByStopId(stopId);

            return stopOptional.<Result<Stop, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if stop is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("Stop: " + stopId);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Updates the given {@link Stop} object.
     *
     * @param stop the stop to update
     * @return the updated stop
     */
    @Transactional
    public Result<Stop, JPAError> updateStop(Stop stop) {
        try {
            if (isAlreadyInDatabase(stop).isSuccess()) {
                stopRepository.save(stop);
                return Result.success(stop);
            } else {
                return Result.error(new JPAError(JPAErrors.NOT_FOUND));
            }
        } catch (Exception e) {
            log.error("Error while updating stop: " + e.getMessage() + " " + e.getCause());
            log.error("Stop: " + stop.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    public List<Stop> findByProducts_National(boolean national) {
        return stopRepository.findByProducts_National(national);
    }

    public List<Stop> findByName(String name) {
        return stopRepository.findByName(name);
    }

}
