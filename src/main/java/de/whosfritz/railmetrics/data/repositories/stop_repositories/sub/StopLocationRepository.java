package de.whosfritz.railmetrics.data.repositories.stop_repositories.sub;

import de.olech2412.adapter.dbadapter.model.stop.sub.StopLocation;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link StopLocation}.
 */
@Repository
public interface StopLocationRepository extends ListCrudRepository<StopLocation, Long> {

    Optional<StopLocation> findByLongitudeAndLatitude(Double longitude, Double latitude);

}