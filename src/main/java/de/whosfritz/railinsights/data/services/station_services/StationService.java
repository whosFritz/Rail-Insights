package de.whosfritz.railinsights.data.services.station_services;

import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.station.Station;
import de.olech2412.adapter.dbadapter.model.station.sub.*;
import de.whosfritz.railinsights.data.repositories.station_repositories.StationRepository;
import de.whosfritz.railinsights.data.services.OperatorService;
import de.whosfritz.railinsights.data.services.station_services.sub.*;
import de.whosfritz.railinsights.jpa.JPAError;
import de.whosfritz.railinsights.jpa.JPAErrors;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class StationService {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ProductLineService productLineService;

    @Autowired
    private RegionalbereichService regionalbereichService;

    @Autowired
    private StationLocationService stationLocationService;

    @Autowired
    private StationManagementService stationManagementService;

    @Autowired
    private SzentraleService szentraleService;

    @Autowired
    private TimeTableOfficeService timeTableOfficeService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private Ril100IdentifierService ril100IdentifierService;

    /**
     * Deletes all {@link Station} objects.
     *
     * @param stations the stations to delete
     */
    public void deleteAll(List<Station> stations) {
        stationRepository.deleteAll(stations);
    }

    public Result<Station, JPAError> isAlreadyInDatabase(Station station) {
        try {
            Optional<Station> stationOptional = stationRepository.findByStationId(station.getStationId());

            return stationOptional.<Result<Station, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if station is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("Station: " + station.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Deletes the {@link Station} object with the given id.
     *
     * @param id the id of the station to delete
     */
    public Result<Station, JPAError> deleteStationById(Long id) {
        try {
            Station station = stationRepository.findById(id).orElse(null);

            if (station == null)
                return Result.error(new JPAError(JPAErrors.NOT_FOUND));

            stationRepository.deleteById(id);
            return Result.success(station);
        } catch (Exception e) {
            log.error("Error while deleting station: " + e.getMessage() + " " + e.getCause());
            log.error("Station id: " + id);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Returns all {@link Station} objects.
     *
     * @return all stations
     */
    public Iterable<Station> getAllStations() {
        return stationRepository.findAll();
    }

    /**
     * Returns the {@link Station} object with the given id.
     *
     * @param id the id of the station to return
     * @return the station with the given id
     */
    public Station getStationById(Long id) {
        return stationRepository.findById(id).orElse(null);
    }

    /**
     * Saves the given {@link Station} object.
     *
     * @param station the address to save
     */
    @Transactional
    public Result<Station, JPAError> saveStation(Station station) {
        try {
            if (!isAlreadyInDatabase(station).isSuccess()) {
                if (!addressService.saveAddress(station.getAddress()).isSuccess()) {
                    Address address = addressService.isAlreadyInDatabase(station.getAddress()).getData();
                    station.setAddress(address);
                }
                if (!productLineService.saveProductLine(station.getProductLine()).isSuccess()) {
                    ProductLine productLine = productLineService.isAlreadyInDatabase(station.getProductLine()).getData();
                    station.setProductLine(productLine);
                }
                if (!regionalbereichService.saveRegionalbereich(station.getRegionalbereich()).isSuccess()) {
                    Regionalbereich regionalbereich = regionalbereichService.isAlreadyInDatabase(station.getRegionalbereich()).getData();
                    station.setRegionalbereich(regionalbereich);
                }
                if (!stationLocationService.saveStationLocation(station.getLocation()).isSuccess()) {
                    StationLocation stationLocation = stationLocationService.isAlreadyInDatabase(station.getLocation()).getData();
                    station.setLocation(stationLocation);
                }
                if (!stationManagementService.saveStationManagement(station.getStationManagement()).isSuccess()) {
                    StationManagement stationManagement = stationManagementService.isAlreadyInDatabase(station.getStationManagement()).getData();
                    station.setStationManagement(stationManagement);
                }
                if (!szentraleService.saveSzentrale(station.getSzentrale()).isSuccess()) {
                    Szentrale szentrale = szentraleService.isAlreadyInDatabase(station.getSzentrale()).getData();
                    station.setSzentrale(szentrale);
                }
                if (!timeTableOfficeService.saveTimeTableOffice(station.getTimeTableOffice()).isSuccess()) {
                    TimeTableOffice timeTableOffice = timeTableOfficeService.isAlreadyInDatabase(station.getTimeTableOffice()).getData();
                    station.setTimeTableOffice(timeTableOffice);
                }
                if (!operatorService.saveOperator(station.getOperator()).isSuccess()) {
                    Operator operator = operatorService.isAlreadyInDatabase(station.getOperator()).getData();
                    station.setOperator(operator);
                }

                List<Result<Ril100Identifier, JPAError>> ril100IdentifierResults = new ArrayList<>();
                for (int i = 0; i < station.getRil100Identifiers().size(); i++) {
                    Result<Ril100Identifier, JPAError> result = ril100IdentifierService.saveRil100Identifier(station.getRil100Identifiers().get(i));
                    ril100IdentifierResults.add(result);
                }

                station.setRil100Identifiers(ril100IdentifierResults.stream().map(Result::getData).collect(Collectors.toList()));

                stationRepository.save(station);
            } else {
                return Result.error(new JPAError(JPAErrors.ALREADY_EXISTS));
            }
            return Result.success(station);
        } catch (Exception e) {
            log.error("Error while saving station: " + e.getMessage() + " " + e.getCause());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Returns the {@link Station} object with the given stationId.
     *
     * @param stationId the stationId of the station to return
     * @return the station with the given stationId
     */
    public Result<Station, JPAError> findStationByStationId(Long stationId) {
        try {
            Optional<Station> stationOptional = stationRepository.findByStationId(stationId);

            return stationOptional.<Result<Station, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if station is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("Station: " + stationId);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

}
