package de.whosfritz.railmetrics.data.repositories.station_repositories.sub;

import de.olech2412.adapter.dbadapter.model.station.sub.StationLocation;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link StationLocation}.
 */
@Repository
public interface StationLocationRepository extends ListCrudRepository<StationLocation, Long> {

    Optional<StationLocation> findByLongitudeAndLatitude(Float longitude, Float latitude);

}