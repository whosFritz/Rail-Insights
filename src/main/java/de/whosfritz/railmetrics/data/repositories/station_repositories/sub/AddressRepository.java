package de.whosfritz.railmetrics.data.repositories.station_repositories.sub;

import de.olech2412.adapter.dbadapter.model.station.sub.Address;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link Address}.
 */
@Repository
public interface AddressRepository extends ListCrudRepository<Address, Long> {

    Optional<Address> findByCityAndStreetAndZipcode(String city, String street, String zipcode);

}