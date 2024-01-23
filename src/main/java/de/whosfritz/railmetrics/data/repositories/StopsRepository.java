package de.whosfritz.railmetrics.data.repositories;

import de.olech2412.adapter.dbadapter.model.stop.Stop;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StopsRepository extends ListCrudRepository<Stop, UUID> {
}
