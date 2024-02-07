package de.whosfritz.railmetrics.data.repositories.station_repositories;

import de.olech2412.adapter.dbadapter.model.station.Station;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link Station}s.
 */
@Repository
public interface StationRepository extends ListCrudRepository<Station, Long> {

    Optional<Station> findByStationId(Long stationId);

}
