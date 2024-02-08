package de.whosfritz.railinsights.data.services.station_services.sub;

import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.station.sub.StationLocation;
import de.whosfritz.railinsights.data.repositories.station_repositories.sub.StationLocationRepository;
import de.whosfritz.railinsights.exception.JPAError;
import de.whosfritz.railinsights.exception.JPAErrors;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class StationLocationService {

    @Autowired
    private StationLocationRepository stationLocationRepository;

    /**
     * Deletes all {@link StationLocation} objects.
     *
     * @param stationLocations the stationLocations to delete
     */
    public void deleteAll(List<StationLocation> stationLocations) {
        stationLocationRepository.deleteAll(stationLocations);
    }

    public Result<StationLocation, JPAError> isAlreadyInDatabase(StationLocation stationLocation) {
        try {
            Optional<StationLocation> stationLocationsFromDatabase = stationLocationRepository.findByLongitudeAndLatitude(stationLocation.getLongitude(), stationLocation.getLatitude());

            return stationLocationsFromDatabase.<Result<StationLocation, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if stationLocation is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("StationLocation: " + stationLocation.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Deletes the {@link StationLocation} object with the given id.
     *
     * @param id the id of the stationLocation to delete
     */
    public Result<StationLocation, JPAError> deleteStationLocationById(Long id) {
        try {
            StationLocation stationLocation = stationLocationRepository.findById(id).orElse(null);

            if (stationLocation == null)
                return Result.error(new JPAError(JPAErrors.NOT_FOUND));

            stationLocationRepository.deleteById(id);
            return Result.success(stationLocation);
        } catch (Exception e) {
            log.error("Error while deleting stationLocation: " + e.getMessage() + " " + e.getCause());
            log.error("StationLocation id: " + id);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Returns all {@link StationLocation} objects.
     *
     * @return all stationLocations
     */
    public Iterable<StationLocation> getAllStationLocations() {
        return stationLocationRepository.findAll();
    }

    /**
     * Returns the {@link StationLocation} object with the given id.
     *
     * @param id the id of the stationLocation to return
     * @return the stationLocation with the given id
     */
    public StationLocation getStationLocationById(Long id) {
        return stationLocationRepository.findById(id).orElse(null);
    }

    /**
     * Saves the given {@link StationLocation} object.
     *
     * @param stationLocation the stationLocation to save
     */
    @Transactional
    public Result<StationLocation, JPAError> saveStationLocation(StationLocation stationLocation) {
        try {
            if (!isAlreadyInDatabase(stationLocation).isSuccess()) {
                stationLocationRepository.save(stationLocation);
                return Result.success(stationLocation);
            } else {
                return Result.error(new JPAError(JPAErrors.ALREADY_EXISTS));
            }
        } catch (Exception e) {
            log.error("Error while saving stationLocation: " + e.getMessage() + " " + e.getCause());
            log.error("StationLocation: " + stationLocation.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

}
