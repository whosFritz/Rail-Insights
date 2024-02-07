package de.whosfritz.railmetrics.data.repositories.station_repositories.sub;

import de.olech2412.adapter.dbadapter.model.station.sub.Regionalbereich;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link Regionalbereich}.
 */
@Repository
public interface RegionalbereichRepository extends ListCrudRepository<Regionalbereich, Long> {

    Optional<Regionalbereich> findByNameAndNumber(String name, int number);

}