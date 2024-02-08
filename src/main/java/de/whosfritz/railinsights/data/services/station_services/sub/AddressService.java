package de.whosfritz.railinsights.data.services.station_services.sub;

import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.station.sub.Address;
import de.whosfritz.railinsights.data.repositories.station_repositories.sub.AddressRepository;
import de.whosfritz.railinsights.exception.JPAError;
import de.whosfritz.railinsights.exception.JPAErrors;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for {@link Address}.
 *
 * @see AddressRepository
 */
@Service
@Log4j2
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    /**
     * Deletes all {@link Address} objects.
     *
     * @param addresses the addresses to delete
     */
    public void deleteAll(List<Address> addresses) {
        addressRepository.deleteAll(addresses);
    }

    public Result<Address, JPAError> isAlreadyInDatabase(Address address) {
        try {
            Optional<Address> addressFromDatabase = addressRepository.findByCityAndStreetAndZipcode(address.getCity(), address.getStreet(), address.getZipcode());

            return addressFromDatabase.<Result<Address, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));

        } catch (Exception e) {
            log.error("Error while checking if address is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("Address: " + address.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Deletes the {@link Address} object with the given id.
     *
     * @param id the id of the address to delete
     */
    public Result<Address, JPAError> deleteAddressById(Long id) {
        try {
            Address address = addressRepository.findById(id).orElse(null);

            if (address == null)
                return Result.error(new JPAError(JPAErrors.NOT_FOUND));

            addressRepository.deleteById(id);
            return Result.success(address);
        } catch (Exception e) {
            log.error("Error while deleting address: " + e.getMessage() + " " + e.getCause());
            log.error("Address id: " + id);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Returns all {@link Address} objects.
     *
     * @return all addresses
     */
    public Iterable<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    /**
     * Returns the {@link Address} object with the given id.
     *
     * @param id the id of the address to return
     * @return the address with the given id
     */
    public Address getAddressById(Long id) {
        return addressRepository.findById(id).orElse(null);
    }

    /**
     * Saves the given {@link Address} object.
     *
     * @param address the address to save
     */
    @Transactional
    public Result<Address, JPAError> saveAddress(Address address) {
        try {
            if (!isAlreadyInDatabase(address).isSuccess()) {
                return Result.success(addressRepository.save(address));
            } else {
                return Result.error(new JPAError(JPAErrors.ALREADY_EXISTS));
            }
        } catch (Exception e) {
            log.error("Error while saving address: " + e.getMessage() + " " + e.getCause());
            log.error("Address: " + address.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

}
