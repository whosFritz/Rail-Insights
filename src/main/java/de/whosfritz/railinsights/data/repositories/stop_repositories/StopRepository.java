package de.whosfritz.railinsights.data.repositories.stop_repositories;

import de.olech2412.adapter.dbadapter.model.stop.Stop;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Stop}s.
 */
@Repository
public interface StopRepository extends ListCrudRepository<Stop, Long> {

    Optional<Stop> findByStopId(Long stopId);

    List<Stop> findByProducts_National(Boolean national);

    List<Stop> findByName(String name);

    Optional<List<Stop>> findByNameContainingIgnoreCase(String name);

    // not sure if this is the right way to do it, but it's the only way I found
    @Query("SELECT s FROM Stop s WHERE FUNCTION('ST_Distance_Sphere', FUNCTION('POINT', s.location.longitude, s.location.latitude), " +
            "FUNCTION('POINT', :stationLongitude, :stationLatitude)) <= :radius")
    List<Stop> findStopsNearbyStation(@Param("stationLongitude") Double stationLongitude,
                                      @Param("stationLatitude") Double stationLatitude,
                                      @Param("radius") Double radius);

}
