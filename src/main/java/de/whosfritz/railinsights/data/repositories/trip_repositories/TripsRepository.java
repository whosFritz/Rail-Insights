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

    @Query(value = "SELECT\n" +
            "    trip_date,\n" +
            "    ROUND((cancelled_trips / total_trips) * 100, 2) AS cancelled_percentage,\n" +
            "    ROUND((on_time_trips / total_trips) * 100, 2) AS on_time_percentage,\n" +
            "    ROUND((delayed_trips / total_trips) * 100, 2) AS delayed_percentage\n" +
            "FROM\n" +
            "    (\n" +
            "        SELECT\n" +
            "            DATE(trip_planned_when) AS trip_date,\n" +
            "            SUM(CASE WHEN trip_cancelled = 1 THEN 1 ELSE 0 END) AS cancelled_trips,\n" +
            "            SUM(CASE WHEN trip_delay <= 360 OR trip_delay IS NULL AND trip_cancelled IS NULL THEN 1 ELSE 0 END) AS on_time_trips,\n" +
            "            SUM(CASE WHEN trip_delay > 360 THEN 1 ELSE 0 END) AS delayed_trips,\n" +
            "            COUNT(*) AS total_trips\n" +
            "        FROM\n" +
            "            trips\n" +
            "        GROUP BY\n" +
            "            trip_date\n" +
            "    ) AS subquery\n" +
            "ORDER BY\n" +
            "    trip_date DESC;", nativeQuery = true)
    List<Object[]> getTripPercentages();


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

    Optional<List<Trip>> findAllByLineNameAndTripIdContains(String lineName, String tripId);

    int countAllByStopAndPlannedWhenAfterAndWhenBefore(Stop stop, LocalDateTime whenAfter, LocalDateTime whenBefore);

}
