package de.whosfritz.railinsights.data.repositories.station_repositories.sub;

import de.olech2412.adapter.dbadapter.model.station.sub.GeographicCoordinates;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link GeographicCoordinates}.
 */
@Repository
public interface GeographicCoordinatesRepository extends ListCrudRepository<GeographicCoordinates, Long> {

    Optional<GeographicCoordinates> findByLatitudeAndLongitude(Float latitude, Float longitude);

}