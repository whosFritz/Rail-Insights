package de.whosfritz.railinsights.data.services.station_services.sub;

import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.station.sub.Ril100Identifier;
import de.whosfritz.railinsights.data.repositories.station_repositories.sub.Ril100IdentifierRepository;
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
public class Ril100IdentifierService {

    @Autowired
    private Ril100IdentifierRepository ril100IdentifierRepository;

    @Autowired
    private GeographicCoordinatesService geographicCoordinatesService;

    /**
     * Deletes all {@link Ril100Identifier} objects.
     *
     * @param ril100Identifiers the ril100Identifiers to delete
     */
    public void deleteAll(List<Ril100Identifier> ril100Identifiers) {
        ril100Identifiers.forEach(ril100Identifier -> {
            if (ril100Identifier.getGeographicCoordinates() != null) // If the geographicCoordinates are not null, delete them
                geographicCoordinatesService.deleteGeographicCoordinatesById(ril100Identifier.getGeographicCoordinates().getId());
        });

        ril100IdentifierRepository.deleteAll(ril100Identifiers);
    }

    public Result<Ril100Identifier, JPAError> isAlreadyInDatabase(Ril100Identifier ril100Identifier) {
        try {
            Optional<Ril100Identifier> ril100IdentifiersFromDatabase = ril100IdentifierRepository.findByRilIdentifier(ril100Identifier.getRilIdentifier());

            return ril100IdentifiersFromDatabase.<Result<Ril100Identifier, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if ril100Identifier is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("Ril100Identifier: " + ril100Identifier.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Deletes the {@link Ril100Identifier} object with the given id.
     *
     * @param id the id of the rile100Identifier to delete
     */
    public Result<Ril100Identifier, JPAError> deleteRil100IdentifierById(Long id) {
        try {
            Ril100Identifier ril100Identifier = ril100IdentifierRepository.findById(id).orElse(null);

            if (ril100Identifier == null)
                return Result.error(new JPAError(JPAErrors.NOT_FOUND));

            if (ril100Identifier.getGeographicCoordinates() != null) // If the geographicCoordinates are not null, delete them
                geographicCoordinatesService.deleteGeographicCoordinatesById(ril100Identifier.getGeographicCoordinates().getId());

            ril100IdentifierRepository.deleteById(id);
            return Result.success(ril100Identifier);
        } catch (Exception e) {
            log.error("Error while deleting ril100Identifier: " + e.getMessage() + " " + e.getCause());
            log.error("Ril100Identifier id: " + id);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Returns all {@link Ril100Identifier} objects.
     *
     * @return all ril100Identifiers
     */
    public Iterable<Ril100Identifier> getAllRil100Identifiers() {
        return ril100IdentifierRepository.findAll();
    }

    /**
     * Returns the {@link Ril100Identifier} object with the given id.
     *
     * @param id the id of the ril100Identifier to return
     * @return the regionalbereich with the given id
     */
    public Ril100Identifier getRil100IdentifierById(Long id) {
        return ril100IdentifierRepository.findById(id).orElse(null);
    }

    /**
     * Saves the given {@link Ril100Identifier} object.
     *
     * @param ril100Identifier the ril100Identifier to save
     */
    @Transactional
    public Result<Ril100Identifier, JPAError> saveRil100Identifier(Ril100Identifier ril100Identifier) {
        try {
            if (!isAlreadyInDatabase(ril100Identifier).isSuccess()) {
                ril100IdentifierRepository.save(ril100Identifier);
                return Result.success(ril100Identifier);
            } else {
                return Result.error(new JPAError(JPAErrors.ALREADY_EXISTS));
            }
        } catch (Exception e) {
            log.error("Error while saving ril100Identifier: " + e.getMessage() + " " + e.getCause());
            log.error("Ril100Identifier: " + ril100Identifier.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

}
