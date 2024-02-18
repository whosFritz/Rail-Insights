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

    // ... rest of the class


    public Iterable<?> getItems(String selectedTable) {
        if (selectedTable.equals("Address")) {
            return addressService.getAllAddresses();
        } else if (selectedTable.equals("GeographicCoordinates")) {
            return geographicCoordinatesService.getAllGeographicCoordinates();
        } else if (selectedTable.equals("Line")) {
            return lineService.getAllLines();
        } else if (selectedTable.equals("Operator")) {
            System.out.println("OperatorService.getAllOperators()");
            Iterable<?> allOperators = operatorService.getAllOperators();
            return allOperators;
        } else if (selectedTable.equals("ProductLine")) {
            return productLineService.getAllProductLines();
        } else if (selectedTable.equals("Regionalbereich")) {
            return regionalbereichService.getAllRegionalbereiches();
        } else if (selectedTable.equals("Remark")) {
            return remarkService.getAllRemarks();
        } else if (selectedTable.equals("Ril100Identifier")) {
            return ril100IdentifierService.getAllRil100Identifiers();
        } else if (selectedTable.equals("StationLocation")) {
            return stationLocationService.getAllStationLocations();
        } else if (selectedTable.equals("StationManagement")) {
            return stationManagementService.getAllStationManagements();
        } else if (selectedTable.equals("Station")) {
            return stationService.getAllStations();
        } else if (selectedTable.equals("StopLocation")) {
            return stopLocationService.getAllStopLocations();
        } else if (selectedTable.equals("Stop")) {
            return stopService.getAllStops();
        } else if (selectedTable.equals("Szentrale")) {
            return szentraleService.getAllSzentrales();
        } else if (selectedTable.equals("TimeTableOffice")) {
            return timeTableOfficeService.getAllTimeTableOffices();
        } else if (selectedTable.equals("Trip")) {
            return tripService.getAllTrips();
        }
        return null;
    }

}
