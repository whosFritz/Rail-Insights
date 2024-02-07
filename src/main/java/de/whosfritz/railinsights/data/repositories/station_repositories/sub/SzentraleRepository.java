package de.whosfritz.railinsights.data.repositories.station_repositories.sub;

import de.olech2412.adapter.dbadapter.model.station.sub.Szentrale;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link Szentrale}.
 */
@Repository
public interface SzentraleRepository extends ListCrudRepository<Szentrale, Long> {

    Optional<Szentrale> findByNameAndNumber(String name, int number);

}