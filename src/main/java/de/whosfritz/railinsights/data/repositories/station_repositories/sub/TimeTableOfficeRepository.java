package de.whosfritz.railinsights.data.repositories.station_repositories.sub;

import de.olech2412.adapter.dbadapter.model.station.sub.TimeTableOffice;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

/**
 * Repository for {@link TimeTableOffice}.
 */
public interface TimeTableOfficeRepository extends ListCrudRepository<TimeTableOffice, Long> {

    Optional<TimeTableOffice> findByEmailAndName(String email, String name);

}