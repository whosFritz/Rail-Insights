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

import java.time.LocalDateTime;
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

    public Iterable<?> getItems(String selectedTable, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return switch (selectedTable) {
            case "Adressen" -> addressService.getAllAddresses();
            case "Geographische Koordinaten" -> geographicCoordinatesService.getAllGeographicCoordinates();
            case "Linien" -> lineService.getAllLines();
            case "Betreiber" -> operatorService.getAllOperators();
            case "Produktlinien" -> productLineService.getAllProductLines();
            case "Regionalbereiche" -> regionalbereichService.getAllRegionalbereiches();
            case "Bemerkungen zu Fahrten" -> remarkService.getAllRemarks();
            case "Ril100Kennzeichnungen" -> ril100IdentifierService.getAllRil100Identifiers();
            case "Standort Stationen" -> stationLocationService.getAllStationLocations();
            case "Station Managements" -> stationManagementService.getAllStationManagements();
            case "Stationen" -> stationService.getAllStations();
            case "Standort Haltestellen" -> stopLocationService.getAllStopLocations();
            case "Haltestellen" -> stopService.getAllStops();
            case "Zentrale" -> szentraleService.getAllSzentrales();
            case "Fahrplan Büros" -> timeTableOfficeService.getAllTimeTableOffices();
            case "Fahrten (Stops)" ->
                    tripService.findAllByPlannedWhenAfterAndPlannedWhenBefore(startDateTime, endDateTime).getData();
            default -> null;
        };
    }

    public Set<String> getExcludedFields(String tableName) {
        Map<String, Set<String>> tableExclusions = new HashMap<>();
        tableExclusions.put("Betreiber", new HashSet<>(Arrays.asList("lines", "stations")));
        tableExclusions.put("Adressen", new HashSet<>(Arrays.asList("stations")));
        tableExclusions.put("Linien", new HashSet<>(Arrays.asList("trips")));
        tableExclusions.put("Produktlinien", new HashSet<>(Arrays.asList("stations")));
        tableExclusions.put("Standort Haltestellen", new HashSet<>(Arrays.asList("stopList")));
        tableExclusions.put("Regionalbereiche", new HashSet<>(Arrays.asList("stations")));
        tableExclusions.put("Bemerkungen zu Fahrten", new HashSet<>(Arrays.asList("trips")));
        tableExclusions.put("Ril100Kennzeichnungen", new HashSet<>(Arrays.asList("geographicCoordinates")));
        tableExclusions.put("Station Managements", new HashSet<>(Arrays.asList("stations")));
        tableExclusions.put("Stationen", new HashSet<>(Arrays.asList("ril100Identifiers", "stop")));
        tableExclusions.put("Zentrale", new HashSet<>(Arrays.asList("stations")));
        tableExclusions.put("Fahrplan Büros", new HashSet<>(Arrays.asList("stations")));
        return tableExclusions.getOrDefault(tableName, new HashSet<>());
    }

}
