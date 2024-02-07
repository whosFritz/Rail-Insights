package de.whosfritz.railmetrics.data.repositories.trip_repositories;

import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Trip}s.
 */
@Repository
public interface TripsRepository extends ListCrudRepository<Trip, Long> {

    Optional<List<Trip>> findAllByTripIdAndStopAndCreatedAtAfter(String tripId, Stop stop, LocalDateTime createdAt);

    Optional<Trip> findByTripIdAndStop(String tripId, Stop stop);

}
