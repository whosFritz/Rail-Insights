package de.whosfritz.railinsights.data.repositories.trip_repositories;

import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Trip}s.
 */
@Repository
public interface TripsRepository extends ListCrudRepository<Trip, Long> {

    Optional<List<Trip>> findAllByTripIdAndStopAndCreatedAtAfter(
            @Param("tripId") String tripId,
            @Param("stop") Stop stop,
            @Param("createdAt") LocalDateTime createdAt,
            Pageable pageable);

    Optional<Trip> findByTripIdAndStop(String tripId, Stop stop);

    Page<List<Trip>> findAllByLineName(String lineName, Pageable pageable);

    Optional<List<Trip>> findAllByStopIdAndWhenIsAfter(Long stopId, LocalDateTime when);

    Optional<List<Trip>> findAllByTripId(String tripId);

}
