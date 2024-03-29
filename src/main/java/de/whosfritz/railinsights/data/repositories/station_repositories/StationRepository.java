package de.whosfritz.railinsights.data.repositories.station_repositories;

import de.olech2412.adapter.dbadapter.model.station.Station;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Station}s.
 */
@Repository
public interface StationRepository extends ListCrudRepository<Station, Long> {

    Optional<Station> findByStationId(Long stationId);

    Optional<List<Station>> findByNameContainingIgnoreCase(String name);

}
