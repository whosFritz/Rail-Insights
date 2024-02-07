package de.whosfritz.railinsights.data.services.station_services.sub;

import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.station.sub.StationManagement;
import de.whosfritz.railinsights.data.repositories.station_repositories.sub.StationManagementRepository;
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
public class StationManagementService {

    @Autowired
    private StationManagementRepository stationManagementRepository;

    /**
     * Deletes all {@link StationManagement} objects.
     *
     * @param stationManagements the stationManagements to delete
     */
    public void deleteAll(List<StationManagement> stationManagements) {
        stationManagementRepository.deleteAll(stationManagements);
    }

    public Result<StationManagement, JPAError> isAlreadyInDatabase(StationManagement stationManagement) {
        try {
            Optional<StationManagement> stationManagementsFromDatabase = stationManagementRepository.findByNameAndNumber(stationManagement.getName(), stationManagement.getNumber());

            return stationManagementsFromDatabase.<Result<StationManagement, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if stationManagement is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("StationManagement: " + stationManagement.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Deletes the {@link StationManagement} object with the given id.
     *
     * @param id the id of the stationManagement to delete
     */
    public Result<StationManagement, JPAError> deleteStationManagementById(Long id) {
        try {
            StationManagement stationManagement = stationManagementRepository.findById(id).orElse(null);

            if (stationManagement == null)
                return Result.error(new JPAError(JPAErrors.NOT_FOUND));

            stationManagementRepository.deleteById(id);
            return Result.success(stationManagement);
        } catch (Exception e) {
            log.error("Error while deleting stationManagement: " + e.getMessage() + " " + e.getCause());
            log.error("StationManagement id: " + id);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Returns all {@link StationManagement} objects.
     *
     * @return all stationManagements
     */
    public Iterable<StationManagement> getAllStationManagements() {
        return stationManagementRepository.findAll();
    }

    /**
     * Returns the {@link StationManagement} object with the given id.
     *
     * @param id the id of the stationManagement to return
     * @return the stationManagement with the given id
     */
    public StationManagement getStationManagementById(Long id) {
        return stationManagementRepository.findById(id).orElse(null);
    }

    /**
     * Saves the given {@link StationManagement} object.
     *
     * @param stationManagement the stationManagement to save
     */
    @Transactional
    public Result<StationManagement, JPAError> saveStationManagement(StationManagement stationManagement) {
        try {
            if (!isAlreadyInDatabase(stationManagement).isSuccess()) {
                stationManagementRepository.save(stationManagement);
                return Result.success(stationManagement);
            } else {
                return Result.error(new JPAError(JPAErrors.ALREADY_EXISTS));
            }
        } catch (Exception e) {
            log.error("Error while saving stationManagement: " + e.getMessage() + " " + e.getCause());
            log.error("StationManagement: " + stationManagement.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

}
