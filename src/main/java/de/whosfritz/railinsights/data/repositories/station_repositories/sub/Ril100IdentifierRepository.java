package de.whosfritz.railinsights.data.repositories.station_repositories.sub;

import de.olech2412.adapter.dbadapter.model.station.sub.Ril100Identifier;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link Ril100Identifier}.
 */
@Repository
public interface Ril100IdentifierRepository extends ListCrudRepository<Ril100Identifier, Long> {

    Optional<Ril100Identifier> findByRilIdentifier(String rilIdentifier);

}