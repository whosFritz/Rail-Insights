package de.whosfritz.railmetrics.data.repositories;

import de.olech2412.adapter.dbadapter.model.station.Station;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StationRepository extends ListCrudRepository<Station, UUID> {
}
