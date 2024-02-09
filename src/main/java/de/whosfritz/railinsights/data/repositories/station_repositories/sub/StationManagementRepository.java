package de.whosfritz.railinsights.data.repositories.station_repositories.sub;

import de.olech2412.adapter.dbadapter.model.station.sub.StationManagement;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link StationManagement}.
 */
@Repository
public interface StationManagementRepository extends ListCrudRepository<StationManagement, Long> {

    Optional<StationManagement> findByNameAndNumber(String name, int number);

}