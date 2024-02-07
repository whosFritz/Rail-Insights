package de.whosfritz.railinsights.data.services.station_services.sub;

import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.station.sub.Regionalbereich;
import de.whosfritz.railinsights.data.repositories.station_repositories.sub.RegionalbereichRepository;
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
public class RegionalbereichService {

    @Autowired
    private RegionalbereichRepository regionalbereichRepository;

    /**
     * Deletes all {@link Regionalbereich} objects.
     *
     * @param regionalbereiches the productLines to delete
     */
    public void deleteAll(List<Regionalbereich> regionalbereiches) {
        regionalbereichRepository.deleteAll(regionalbereiches);
    }

    public Result<Regionalbereich, JPAError> isAlreadyInDatabase(Regionalbereich regionalbereich) {
        try {
            Optional<Regionalbereich> regionalbereichesFromDatabase = regionalbereichRepository.findByNameAndNumber(regionalbereich.getName(), regionalbereich.getNumber());

            return regionalbereichesFromDatabase.<Result<Regionalbereich, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if regionalbereich is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("Regionalbereich: " + regionalbereich.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Deletes the {@link Regionalbereich} object with the given id.
     *
     * @param id the id of the regionalbereich to delete
     */
    public Result<Regionalbereich, JPAError> deleteRegionalbereichById(Long id) {
        try {
            Regionalbereich regionalbereich = regionalbereichRepository.findById(id).orElse(null);

            if (regionalbereich == null)
                return Result.error(new JPAError(JPAErrors.NOT_FOUND));

            regionalbereichRepository.deleteById(id);
            return Result.success(regionalbereich);
        } catch (Exception e) {
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Returns all {@link Regionalbereich} objects.
     *
     * @return all regionalbereiches
     */
    public Iterable<Regionalbereich> getAllRegionalbereiches() {
        return regionalbereichRepository.findAll();
    }

    /**
     * Returns the {@link Regionalbereich} object with the given id.
     *
     * @param id the id of the regionalbereich to return
     * @return the regionalbereich with the given id
     */
    public Regionalbereich getRegionalbereichById(Long id) {
        return regionalbereichRepository.findById(id).orElse(null);
    }

    /**
     * Saves the given {@link Regionalbereich} object.
     *
     * @param regionalbereich the regionalbereich to save
     */
    @Transactional
    public Result<Regionalbereich, JPAError> saveRegionalbereich(Regionalbereich regionalbereich) {
        try {
            if (!isAlreadyInDatabase(regionalbereich).isSuccess()) {
                regionalbereichRepository.save(regionalbereich);
                return Result.success(regionalbereich);
            } else {
                return Result.error(new JPAError(JPAErrors.ALREADY_EXISTS));
            }
        } catch (Exception e) {
            log.error("Error while saving regionalbereich: " + e.getMessage() + " " + e.getCause());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

}
