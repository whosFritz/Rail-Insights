package de.whosfritz.railinsights.data.services.station_services.sub;

import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.station.sub.Szentrale;
import de.whosfritz.railinsights.data.repositories.station_repositories.sub.SzentraleRepository;
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
public class SzentraleService {

    @Autowired
    private SzentraleRepository szentraleRepository;

    /**
     * Deletes all {@link Szentrale} objects.
     *
     * @param szentrales the szentrales to delete
     */
    public void deleteAll(List<Szentrale> szentrales) {
        szentraleRepository.deleteAll(szentrales);
    }

    public Result<Szentrale, JPAError> isAlreadyInDatabase(Szentrale szentrale) {
        try {
            Optional<Szentrale> szentralesFromDatabase = szentraleRepository.findByNameAndNumber(szentrale.getName(), szentrale.getNumber());

            return szentralesFromDatabase.<Result<Szentrale, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if szentrale is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("Szentrale: " + szentrale.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }


    /**
     * Deletes the {@link Szentrale} object with the given id.
     *
     * @param id the id of the szentrale to delete
     */
    public Result<Szentrale, JPAError> deleteSzentraleById(Long id) {
        try {
            Szentrale szentrale = szentraleRepository.findById(id).orElse(null);

            if (szentrale == null)
                return Result.error(new JPAError(JPAErrors.NOT_FOUND));

            szentraleRepository.deleteById(id);
            return Result.success(szentrale);
        } catch (Exception e) {
            log.error("Error while deleting szentrale: " + e.getMessage() + " " + e.getCause());
            log.error("Szentrale id: " + id);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Returns all {@link Szentrale} objects.
     *
     * @return all szentrales
     */
    public Iterable<Szentrale> getAllSzentrales() {
        return szentraleRepository.findAll();
    }

    /**
     * Returns the {@link Szentrale} object with the given id.
     *
     * @param id the id of the Szentrale to return
     * @return the Szentrale with the given id
     */
    public Szentrale getSzentraleById(Long id) {
        return szentraleRepository.findById(id).orElse(null);
    }

    /**
     * Saves the given {@link Szentrale} object.
     *
     * @param szentrale the szentrale to save
     */
    @Transactional
    public Result<Szentrale, JPAError> saveSzentrale(Szentrale szentrale) {
        try {
            if (!isAlreadyInDatabase(szentrale).isSuccess()) {
                szentraleRepository.save(szentrale);
                return Result.success(szentrale);
            } else {
                return Result.error(new JPAError(JPAErrors.ALREADY_EXISTS));
            }
        } catch (Exception e) {
            log.error("Error while saving szentrale: " + e.getMessage() + " " + e.getCause());
            log.error("Szentrale: " + szentrale.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

}
