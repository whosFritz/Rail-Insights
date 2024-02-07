package de.whosfritz.railmetrics.data.repositories.stop_repositories;

import de.olech2412.adapter.dbadapter.model.stop.Stop;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Stop}s.
 */
@Repository
public interface StopRepository extends ListCrudRepository<Stop, Long> {

    Optional<Stop> findByStopId(Long stopId);

    List<Stop> findByProducts_National(Boolean national);

    List<Stop> findByName(String name);

}
