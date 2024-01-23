package de.whosfritz.railmetrics.data.repositories;

import de.olech2412.adapter.dbadapter.model.trip.Trip;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TripsRepository extends ListCrudRepository<Trip, UUID> {
}
