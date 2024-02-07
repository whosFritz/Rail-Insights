package de.whosfritz.railmetrics.data.services.station_services.sub;

import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.station.sub.GeographicCoordinates;
import de.whosfritz.railmetrics.data.repositories.station_repositories.sub.GeographicCoordinatesRepository;
import de.whosfritz.railmetrics.exception.jpa.JPAError;
import de.whosfritz.railmetrics.exception.jpa.JPAErrors;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for {@link GeographicCoordinates}.
 */
@Service
@Log4j2
public class GeographicCoordinatesService {

    @Autowired
    private GeographicCoordinatesRepository geographicCoordinatesRepository;

    /**
     * Deletes all {@link GeographicCoordinates} objects.
     *
     * @param geographicCoordinates the geographicCoordinates to delete
     */
    public void deleteAllGeohraphicCoordinates(List<GeographicCoordinates> geographicCoordinates) {
        geographicCoordinatesRepository.deleteAll(geographicCoordinates);
    }

    public Result<GeographicCoordinates, JPAError> isAlreadyInDatabase(GeographicCoordinates geographicCoordinates) {
        try {
            Optional<GeographicCoordinates> geographicCoordinatesFromDatabase = geographicCoordinatesRepository.findByLatitudeAndLongitude(geographicCoordinates.getLatitude(), geographicCoordinates.getLongitude());

            return geographicCoordinatesFromDatabase.<Result<GeographicCoordinates, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if geographicCoordinates is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("GeographicCoordinates: " + geographicCoordinates.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Deletes the {@link GeographicCoordinates} object with the given id.
     *
     * @param id the id of the geographicCoordinates to delete
     */
    public Result<GeographicCoordinates, JPAError> deleteGeographicCoordinatesById(Long id) {
        try {
            GeographicCoordinates geographicCoordinates = geographicCoordinatesRepository.findById(id).orElse(null);

            if (geographicCoordinates == null)
                return Result.error(new JPAError(JPAErrors.NOT_FOUND));

            geographicCoordinatesRepository.deleteById(id);
            return Result.success(geographicCoordinates);
        } catch (Exception e) {
            log.error("Error while deleting geographicCoordinates: " + e.getMessage() + " " + e.getCause());
            log.error("GeographicCoordinates id: " + id);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Returns all {@link GeographicCoordinates} objects.
     *
     * @return all geographicCoordinates
     */
    public Iterable<GeographicCoordinates> getAllGeographicCoordinates() {
        return geographicCoordinatesRepository.findAll();
    }

    /**
     * Returns the {@link GeographicCoordinates} object with the given id.
     *
     * @param id the id of the geographicCoordinates to return
     * @return the geographicCoordinates with the given id
     */
    public GeographicCoordinates getGeographicCoordinatesById(Long id) {
        return geographicCoordinatesRepository.findById(id).orElse(null);
    }

    /**
     * Saves the given {@link GeographicCoordinates} object.
     *
     * @param geographicCoordinates the geographicCoordinates to save
     */
    @Transactional
    public Result<GeographicCoordinates, JPAError> saveGeographicCoordinates(GeographicCoordinates geographicCoordinates) {
        try {
            if (!isAlreadyInDatabase(geographicCoordinates).isSuccess()) {
                geographicCoordinatesRepository.save(geographicCoordinates);
                return Result.success(geographicCoordinates);
            } else {
                return Result.error(new JPAError(JPAErrors.ALREADY_EXISTS));
            }
        } catch (Exception e) {
            log.error("Error while saving geographicCoordinates: " + e.getMessage() + " " + e.getCause());
            log.error("GeographicCoordinates: " + geographicCoordinates.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

}
