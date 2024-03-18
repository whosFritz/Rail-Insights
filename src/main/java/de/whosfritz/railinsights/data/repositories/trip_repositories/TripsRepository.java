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

    @Query(value = "SELECT DATE(t.trip_planned_when) AS trip_date, AVG(t.trip_delay) AS avg_delay " +
            "FROM trips t " +
            "WHERE t.trip_delay >= 360 AND t.trip_planned_when BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(t.trip_planned_when) " +
            "ORDER BY trip_date", nativeQuery = true)
    List<Object[]> getAverageDelayByDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT DATE(t.trip_planned_when) AS trip_date, AVG(t.trip_delay) AS avg_delay " +
            "FROM trips t " +
            "JOIN line l ON t.line_id = l.id " +
            "WHERE t.trip_delay >= 360 AND t.trip_planned_when BETWEEN :startDate AND :endDate AND (l.stop_line_product = 'regional' or l.stop_line_product = 'suburban' or l.stop_line_product = 'regionalExpress')" +
            "GROUP BY DATE(t.trip_planned_when) " +
            "ORDER BY trip_date", nativeQuery = true)
    List<Object[]> getAverageDelayByDateNah(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT DATE(t.trip_planned_when) AS trip_date, AVG(t.trip_delay) AS avg_delay " +
            "FROM trips t " +
            "JOIN line l ON t.line_id = l.id " +
            "WHERE t.trip_delay >= 360 AND t.trip_planned_when BETWEEN :startDate AND :endDate AND (l.stop_line_product = 'national' or l.stop_line_product = 'nationalExpress')" +
            "GROUP BY DATE(t.trip_planned_when) " +
            "ORDER BY trip_date", nativeQuery = true)
    List<Object[]> getAverageDelayByDateFern(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT SUM(t.trip_delay) " +
            "FROM trips t " +
            "WHERE t.trip_delay >= 360 AND t.trip_planned_when BETWEEN :startDate AND :endDate", nativeQuery = true)
    int sumOfTripsDelayedMoreThanSixMinutes(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT SUM(t.trip_delay) " +
            "FROM trips t " +
            "JOIN line l ON t.line_id = l.id " +
            "WHERE t.trip_delay >= :delay AND t.trip_planned_when BETWEEN :startDate AND :endDate AND (l.stop_line_product = 'regional' or l.stop_line_product = 'suburban' or l.stop_line_product = 'regionalExpress')", nativeQuery = true)
    int sumOfTripsDelayedMoreThanSixMinutesNah(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("delay") int delay);


    @Query(value = "SELECT SUM(t.trip_delay) " +
            "FROM trips t " +
            "JOIN line l ON t.line_id = l.id " +
            "WHERE t.trip_delay >= :delay AND t.trip_planned_when BETWEEN :startDate AND :endDate AND (l.stop_line_product = 'national' or l.stop_line_product = 'nationalExpress')", nativeQuery = true)
    int sumOfTripsDelayedMoreThanSixMinutesFern(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("delay") int delay);

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

    @Query(value = "SELECT COUNT(*) " +
            "FROM trips t " +
            "WHERE t.trip_delay >= :delay AND t.trip_planned_when BETWEEN :plannedWhenAfter AND :plannedWhenBefore", nativeQuery = true)
    int countDatumDazwischenDelay(
            LocalDateTime plannedWhenAfter,
            LocalDateTime plannedWhenBefore,
            int delay);

    @Query(value = "SELECT COUNT(*) " +
            "FROM trips t " +
            "JOIN line l ON t.line_id = l.id " +
            "WHERE t.trip_delay >= :delay AND t.trip_planned_when BETWEEN :plannedWhenAfter AND :plannedWhenBefore AND (l.stop_line_product = 'regional' or l.stop_line_product = 'suburban' or l.stop_line_product = 'regionalExpress')", nativeQuery = true)
    int countDatumDazwischenDelayNah(
            @Param("plannedWhenAfter") LocalDateTime plannedWhenAfter,
            @Param("plannedWhenBefore") LocalDateTime plannedWhenBefore,
            @Param("delay") int delay);

    @Query(value = "SELECT COUNT(*) " +
            "FROM trips t " +
            "JOIN line l ON t.line_id = l.id " +
            "WHERE t.trip_delay >= :delay AND t.trip_planned_when BETWEEN :plannedWhenAfter AND :plannedWhenBefore AND (l.stop_line_product = 'national' or l.stop_line_product = 'nationalExpress')", nativeQuery = true)
    int countDatumDazwischenDelayFern(
            @Param("plannedWhenAfter") LocalDateTime plannedWhenAfter,
            @Param("plannedWhenBefore") LocalDateTime plannedWhenBefore,
            @Param("delay") int delay);

    @Query(value = "SELECT COUNT(*) " +
            "FROM trips t " +
            "WHERE t.trip_planned_when BETWEEN :plannedWhenAfter AND :plannedWhenBefore", nativeQuery = true)
    int countAlleStopsInDiesemZeitraum(@Param("plannedWhenAfter") LocalDateTime plannedWhenAfter, @Param("plannedWhenBefore") LocalDateTime plannedWhenBefore);

    @Query(value = "SELECT COUNT(*) " +
            "FROM trips t " +
            "JOIN line l ON t.line_id = l.id " +
            "WHERE t.trip_planned_when BETWEEN :plannedWhenAfter AND :plannedWhenBefore AND (l.stop_line_product = 'regional' OR l.stop_line_product = 'suburban' OR l.stop_line_product = 'regionalExpress')", nativeQuery = true)
    int countAlleStopsInDiesemZeitraumNah(
            @Param("plannedWhenAfter") LocalDateTime plannedWhenAfter,
            @Param("plannedWhenBefore") LocalDateTime plannedWhenBefore);

    @Query(value = "SELECT COUNT(*) " +
            "FROM trips t " +
            "JOIN line l ON t.line_id = l.id " +
            "WHERE t.trip_planned_when BETWEEN :plannedWhenAfter AND :plannedWhenBefore AND (l.stop_line_product = 'national' OR l.stop_line_product = 'nationalExpress')", nativeQuery = true)
    int countAlleStopsInDiesemZeitraumFern(
            @Param("plannedWhenAfter") LocalDateTime plannedWhenAfter,
            @Param("plannedWhenBefore") LocalDateTime plannedWhenBefore);

    @Query(value = "SELECT COUNT(*) " +
            "FROM trips t " +
            "WHERE t.trip_planned_when BETWEEN :plannedWhenAfter AND :plannedWhenBefore AND t.trip_cancelled = true", nativeQuery = true)
    int countAusfaelle(@Param("plannedWhenAfter") LocalDateTime plannedWhenAfter,
                       @Param("plannedWhenBefore") LocalDateTime plannedWhenBefore);

    @Query(value = "SELECT COUNT(*) " +
            "FROM trips t " +
            "JOIN line l ON t.line_id = l.id " +
            "WHERE t.trip_planned_when BETWEEN :plannedWhenAfter AND :plannedWhenBefore AND (l.stop_line_product = 'regional' or l.stop_line_product = 'suburban' or l.stop_line_product = 'regionalExpress') and  t.trip_cancelled = true", nativeQuery = true)
    int countAusfaelleNah(@Param("plannedWhenAfter") LocalDateTime plannedWhenAfter,
                          @Param("plannedWhenBefore") LocalDateTime plannedWhenBefore);

    @Query(value = "SELECT COUNT(*) " +
            "FROM trips t " +
            "JOIN line l ON t.line_id = l.id " +
            "WHERE t.trip_planned_when BETWEEN :plannedWhenAfter AND :plannedWhenBefore AND (l.stop_line_product = 'national' or l.stop_line_product = 'nationalExpress') and  t.trip_cancelled = true", nativeQuery = true)
    int countAusfaelleFern(@Param("plannedWhenAfter") LocalDateTime plannedWhenAfter,
                           @Param("plannedWhenBefore") LocalDateTime plannedWhenBefore);

    Optional<List<Trip>> findAllByPlannedWhenAfterAndPlannedWhenBefore(LocalDateTime whenAfter, LocalDateTime whenBefore);

    Optional<List<Trip>> findAllByLineNameAndTripIdContains(String lineName, String tripId);

    int countAllByStopAndPlannedWhenAfterAndWhenBefore(Stop stop, LocalDateTime whenAfter, LocalDateTime whenBefore);
}

