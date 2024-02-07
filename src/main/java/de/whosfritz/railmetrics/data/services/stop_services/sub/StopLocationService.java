package de.whosfritz.railmetrics.data.services.stop_services.sub;

import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.stop.sub.StopLocation;
import de.whosfritz.railmetrics.data.repositories.stop_repositories.sub.StopLocationRepository;
import de.whosfritz.railmetrics.exception.jpa.JPAError;
import de.whosfritz.railmetrics.exception.jpa.JPAErrors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class StopLocationService {

    @Autowired
    private StopLocationRepository stopLocationRepository;

    /**
     * Deletes all {@link StopLocation} objects.
     *
     * @param stopLocations the stopLocations to delete
     */
    public void deleteAll(List<StopLocation> stopLocations) {
        stopLocationRepository.deleteAll(stopLocations);
    }

    public Result<StopLocation, JPAError> isAlreadyInDatabase(StopLocation stopLocation) {
        try {
            Optional<StopLocation> stopLocationFromDatabase = stopLocationRepository.findByLongitudeAndLatitude(stopLocation.getLongitude(), stopLocation.getLatitude());

            return stopLocationFromDatabase.<Result<StopLocation, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if stopLocation is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("StopLocation: " + stopLocation.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Deletes the {@link StopLocation} object with the given id.
     *
     * @param id the id of the stopLocation to delete
     */
    public Result<StopLocation, JPAError> deleteStationLocationById(Long id) {
        try {
            StopLocation stopLocation = stopLocationRepository.findById(id).orElse(null);

            if (stopLocation == null)
                return Result.error(new JPAError(JPAErrors.NOT_FOUND));

            stopLocationRepository.deleteById(id);
            return Result.success(stopLocation);
        } catch (Exception e) {
            log.error("Error while deleting stopLocation: " + e.getMessage() + " " + e.getCause());
            log.error("StopLocation id: " + id);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Returns all {@link StopLocation} objects.
     *
     * @return all stopLocations
     */
    public Iterable<StopLocation> getAllStopLocations() {
        return stopLocationRepository.findAll();
    }

    /**
     * Returns the {@link StopLocation} object with the given id.
     *
     * @param id the id of the stopLocation to return
     * @return the stopLocation with the given id
     */
    public StopLocation getStopLocationById(Long id) {
        return stopLocationRepository.findById(id).orElse(null);
    }

    /**
     * Saves the given {@link StopLocation} object.
     *
     * @param stopLocation the stopLocation to save
     */
    public Result<StopLocation, JPAError> saveStopLocation(StopLocation stopLocation) {
        try {
            if (!isAlreadyInDatabase(stopLocation).isSuccess()) {
                stopLocationRepository.save(stopLocation);
                return Result.success(stopLocation);
            } else {
                return Result.error(new JPAError(JPAErrors.ALREADY_EXISTS));
            }
        } catch (Exception e) {
            log.error("Error while saving stopLocation: " + e.getMessage() + " " + e.getCause());
            log.error("StopLocation: " + stopLocation.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

}
