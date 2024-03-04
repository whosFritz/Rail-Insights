package de.whosfritz.railinsights.data.repositories.trip_repositories;

import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value = "SELECT DATE(trip_planned_when) AS trip_date, COUNT(*) AS trip_count FROM trips GROUP BY DATE(trip_planned_when) ORDER BY trip_date", nativeQuery = true)
    List<Object[]> countTripsByDate();

    int countAllByCancelled(boolean cancelled);

    int countAllByDelayIsGreaterThanEqual(int delay);

    Optional<List<Trip>> findAllByTripIdAndStopAndCreatedAtAfter(
            @Param("tripId") String tripId,
            @Param("stop") Stop stop,
            @Param("createdAt") LocalDateTime createdAt,
            Pageable pageable);

    Optional<List<Trip>> findAllByLineFahrtNrAndStopAndCreatedAtAfterAndTripIdContains(
            @Param("fahrtNr") String fahrtNr,
            @Param("stop") Stop stop,
            @Param("createdAt") LocalDateTime createdAt,
            @Param("tripId") String tripId);

    Optional<Trip> findByTripIdAndStop(String tripId, Stop stop);

    Page<List<Trip>> findAllByLineName(String lineName, Pageable pageable);

    Optional<List<Trip>> findAllByStopIdAndWhenIsAfter(Long stopId, LocalDateTime when);

    Optional<List<Trip>> findAllByTripId(String tripId);

    Optional<List<Trip>> findAllByStop(Stop stop);

    Optional<List<Trip>> findAllByStopAndPlannedWhenAfterAndPlannedWhenBefore(Stop stop, LocalDateTime whenAfter, LocalDateTime whenBefore);

    Optional<List<Trip>> findAllByLineLineId(String lineId);

    Optional<List<Trip>> findAllByPlannedWhenIsAfterAndPlannedWhenIsBeforeAndLineName(
            LocalDateTime plannedWhenAfter,
            LocalDateTime plannedWhenBefore,
            String name);

    Optional<List<Trip>> findAllByPlannedWhenAfterAndPlannedWhenBefore(LocalDateTime whenAfter, LocalDateTime whenBefore);

    int countAllByStopAndPlannedWhenAfterAndWhenBefore(Stop stop, LocalDateTime whenAfter, LocalDateTime whenBefore);

}
