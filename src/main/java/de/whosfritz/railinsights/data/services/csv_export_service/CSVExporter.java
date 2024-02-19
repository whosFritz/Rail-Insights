package de.whosfritz.railinsights.data.services.csv_export_service;

import de.whosfritz.railinsights.data.services.LineService;
import de.whosfritz.railinsights.data.services.OperatorService;
import de.whosfritz.railinsights.data.services.station_services.StationService;
import de.whosfritz.railinsights.data.services.station_services.sub.*;
import de.whosfritz.railinsights.data.services.stop_services.StopService;
import de.whosfritz.railinsights.data.services.stop_services.sub.StopLocationService;
import de.whosfritz.railinsights.data.services.trip_services.TripService;
import de.whosfritz.railinsights.data.services.trip_services.sub.RemarkService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CSVExporter {

    private final AddressService addressService;
    private final GeographicCoordinatesService geographicCoordinatesService;
    private final LineService lineService;
    private final OperatorService operatorService;
    private final ProductLineService productLineService;
    private final RegionalbereichService regionalbereichService;
    private final RemarkService remarkService;
    private final Ril100IdentifierService ril100IdentifierService;
    private final StationLocationService stationLocationService;
    private final StationManagementService stationManagementService;
    private final StationService stationService;
    private final StopLocationService stopLocationService;
    private final StopService stopService;
    private final SzentraleService szentraleService;
    private final TimeTableOfficeService timeTableOfficeService;
    private final TripService tripService;

    public CSVExporter(OperatorService operatorService, AddressService addressService, GeographicCoordinatesService geographicCoordinatesService, LineService lineService, ProductLineService productLineService, RegionalbereichService regionalbereichService, RemarkService remarkService, Ril100IdentifierService ril100IdentifierService, StationLocationService stationLocationService, StationManagementService stationManagementService, StationService stationService, StopLocationService stopLocationService, StopService stopService, SzentraleService szentraleService, TimeTableOfficeService timeTableOfficeService, TripService tripService) {
        this.operatorService = operatorService;
        this.addressService = addressService;
        this.geographicCoordinatesService = geographicCoordinatesService;
        this.lineService = lineService;
        this.productLineService = productLineService;
        this.regionalbereichService = regionalbereichService;
        this.remarkService = remarkService;
        this.ril100IdentifierService = ril100IdentifierService;
        this.stationLocationService = stationLocationService;
        this.stationManagementService = stationManagementService;
        this.stationService = stationService;
        this.stopLocationService = stopLocationService;
        this.stopService = stopService;
        this.szentraleService = szentraleService;
        this.timeTableOfficeService = timeTableOfficeService;
        this.tripService = tripService;
    }

    public Iterable<?> getItems(String selectedTable) {
        if (selectedTable.equals("Adressen")) {
            return addressService.getAllAddresses();
        } else if (selectedTable.equals("Geographische Koordinaten")) {
            return geographicCoordinatesService.getAllGeographicCoordinates();
        } else if (selectedTable.equals("Linie")) {
            return lineService.getAllLines();
        } else if (selectedTable.equals("Betreiber")) {
            return operatorService.getAllOperators();
        } else if (selectedTable.equals("Produktlinie")) {
            return productLineService.getAllProductLines();
        } else if (selectedTable.equals("Regionalbereich")) {
            return regionalbereichService.getAllRegionalbereiches();
        } else if (selectedTable.equals("Bemerkung")) {
            return remarkService.getAllRemarks();
        } else if (selectedTable.equals("Ril100Kennzeichnung")) {
            return ril100IdentifierService.getAllRil100Identifiers();
        } else if (selectedTable.equals("Standort Station")) {
            return stationLocationService.getAllStationLocations();
        } else if (selectedTable.equals("Station Management")) {
            return stationManagementService.getAllStationManagements();
        } else if (selectedTable.equals("Station")) {
            return stationService.getAllStations();
        } else if (selectedTable.equals("Standort Haltestelle")) {
            return stopLocationService.getAllStopLocations();
        } else if (selectedTable.equals("Haltestelle")) {
            return stopService.getAllStops();
        } else if (selectedTable.equals("Zentrale")) {
            return szentraleService.getAllSzentrales();
        } else if (selectedTable.equals("Fahrplanbüro")) {
            return timeTableOfficeService.getAllTimeTableOffices();
        } else if (selectedTable.equals("Fahrten")) {
            return tripService.getAllTrips();
        }
        return null;
    }

    public Set<String> getExcludedFields(String tableName) {
        Map<String, Set<String>> tableExclusions = new HashMap<>();
        tableExclusions.put("Betreiber", new HashSet<>(Arrays.asList("lines", "stations")));
        tableExclusions.put("Adressen", new HashSet<>(Arrays.asList("stations")));
        tableExclusions.put("Linie", new HashSet<>(Arrays.asList("trips")));
        tableExclusions.put("Produktlinie", new HashSet<>(Arrays.asList("stations")));
        tableExclusions.put("Standort Haltestelle", new HashSet<>(Arrays.asList("stopList")));
        tableExclusions.put("Regionalbereich", new HashSet<>(Arrays.asList("stations")));
        tableExclusions.put("Bemerkung", new HashSet<>(Arrays.asList("trips")));
        tableExclusions.put("Ril100Kennzeichnung", new HashSet<>(Arrays.asList("geographicCoordinates")));
        tableExclusions.put("Station Management", new HashSet<>(Arrays.asList("stations")));
        tableExclusions.put("Station", new HashSet<>(Arrays.asList("ril100Identifiers", "stop")));
        tableExclusions.put("Zentrale", new HashSet<>(Arrays.asList("stations")));
        tableExclusions.put("Fahrplanbüro", new HashSet<>(Arrays.asList("stations")));


        return tableExclusions.getOrDefault(tableName, new HashSet<>());
    }

}
