package de.whosfritz.railinsights.data.services.csv_export_service;

import de.olech2412.adapter.dbadapter.model.station.Station;
import de.olech2412.adapter.dbadapter.model.station.sub.*;
import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.olech2412.adapter.dbadapter.model.stop.sub.Line;
import de.olech2412.adapter.dbadapter.model.stop.sub.StopLocation;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.olech2412.adapter.dbadapter.model.trip.sub.Remark;
import de.whosfritz.railinsights.data.repositories.trip_repositories.TripsRepository;
import de.whosfritz.railinsights.data.services.LineService;
import de.whosfritz.railinsights.data.services.OperatorService;
import de.whosfritz.railinsights.data.services.station_services.StationService;
import de.whosfritz.railinsights.data.services.station_services.sub.*;
import de.whosfritz.railinsights.data.services.stop_services.StopService;
import de.whosfritz.railinsights.data.services.stop_services.sub.StopLocationService;
import de.whosfritz.railinsights.data.services.trip_services.sub.RemarkService;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class CSVExporterService {

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
    private final TripsRepository tripRepository;

    public CSVExporterService(OperatorService operatorService,
                              AddressService addressService,
                              GeographicCoordinatesService geographicCoordinatesService,
                              LineService lineService,
                              ProductLineService productLineService,
                              RegionalbereichService regionalbereichService,
                              RemarkService remarkService,
                              Ril100IdentifierService ril100IdentifierService,
                              StationLocationService stationLocationService,
                              StationManagementService stationManagementService,
                              StationService stationService,
                              StopLocationService stopLocationService,
                              StopService stopService,
                              SzentraleService szentraleService,
                              TimeTableOfficeService timeTableOfficeService,
                              TripsRepository tripRepository) {
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
        this.tripRepository = tripRepository;
    }


    public void writeCsv(String comboBoxSelection, LocalDate startDatePickerValue, PrintWriter writer) {
        switch (comboBoxSelection) {
            case "Adressen":
                writeAddressCsv(writer);
                break;
            case "Geographische Koordinaten":
                writeGeographicCoordinatesCsv(writer);
                break;
            case "Linien":
                writeLineCsv(writer);
                break;
            case "Betreiber":
                writeOperatorCsv(writer);
                break;
            case "Produktlinien":
                writeProductLineCsv(writer);
                break;
            case "Regionalbereiche":
                writeRegionalbereichCsv(writer);
                break;
            case "Bemerkungen zu Fahrten":
                writeRemarkCsv(writer);
                break;
            case "Ril100Kennzeichnungen":
                writeRil100IdentifierCsv(writer);
                break;
            case "Standorte von Stationen":
                writeStationLocationCsv(writer);
                break;
            case "Station Managements":
                writeStationManagementCsv(writer);
                break;
            case "Stationen":
                writeStationCsv(writer);
                break;
            case "Standorte von Haltestellen":
                writeStopLocationCsv(writer);
                break;
            case "Haltestellen":
                writeStopCsv(writer);
                break;
            case "Zentrale":
                writeZentraleCsv(writer);
                break;
            case "Fahrplan Büros":
                writeTimeTableOfficeCsv(writer);
                break;
            case "Fahrten (Stops)":
                writeTripCsv(writer, startDatePickerValue);
                break;
            default:
                return;

        }
        writer.close();
    }


    private void writeAddressCsv(PrintWriter writer) {
        Iterable<Address> items = addressService.getAllAddresses();
        if (items == null) {
            return;
        }
        Class<?> clazz = items.iterator().next().getClass();
        Field[] fields = clazz.getDeclaredFields();
        writer.println(Arrays.stream(fields).map(Field::getName).filter(s -> !s.equals("stations")).reduce((s, s2) -> s + ";" + s2).get());
        for (Address address : items) {
            writer.println(address.getId() + ";" + address.getCity() + ";" + address.getZipcode() + ";" + address.getStreet());
        }
    }

    private void writeGeographicCoordinatesCsv(PrintWriter writer) {
        Iterable<GeographicCoordinates> items = geographicCoordinatesService.getAllGeographicCoordinates();
        if (items == null) {
            return;
        }
        Class<?> clazz = items.iterator().next().getClass();
        Field[] fields = clazz.getDeclaredFields();
        writer.println(Arrays.stream(fields).map(Field::getName).reduce((s, s2) -> s + ";" + s2).get());
        for (GeographicCoordinates geographicCoordinates : items) {
            writer.println(
                    geographicCoordinates.getId() + ";" +
                            geographicCoordinates.getType() + ";" +
                            geographicCoordinates.getLatitude() + ";" +
                            geographicCoordinates.getLongitude());
        }
    }

    private void writeLineCsv(PrintWriter writer) {
        Iterable<Line> items = lineService.getAllLines();
        if (items == null) {
            return;
        }
        Class<?> clazz = items.iterator().next().getClass();
        Field[] fields = clazz.getDeclaredFields();
        writer.println(Arrays.stream(fields).map(Field::getName).filter(s -> !s.equals("trips")).reduce((s, s2) -> s + ";" + s2).get());
        for (Line line : items) {
            writer.println(
                    (line.getOperator() == null ? "null" : line.getOperator().getId()) + ";" +
                            line.getType() + ";" +
                            line.getLineId() + ";" +
                            line.getFahrtNr() + ";" +
                            line.getName() + ";" +
                            line.isLinePublic() + ";" +
                            line.getProductName() + ";" +
                            line.getLineId() + ";" +
                            line.getProduct() + ";" +
                            line.getMode() + ";" +
                            line.getCreatedAt()
            );
        }
    }

    private void writeOperatorCsv(PrintWriter writer) {
        Iterable<Operator> items = operatorService.getAllOperators();
        if (items == null) {
            return;
        }
        Class<?> clazz = items.iterator().next().getClass();
        Field[] fields = clazz.getDeclaredFields();
        writer.println(Arrays.stream(fields).map(Field::getName).filter(s -> !s.equals("lines") && !s.equals("stations")).reduce((s, s2) -> s + ";" + s2).get());
        for (Operator operator : items) {
            writer.println(
                    operator.getId() + ";" +
                            operator.getType() + ";" +
                            operator.getOperatorId() + ";" +
                            operator.getName()
            );
        }
    }

    private void writeProductLineCsv(PrintWriter writer) {
        Iterable<ProductLine> items = productLineService.getAllProductLines();
        if (items == null) {
            return;
        }
        Class<?> clazz = items.iterator().next().getClass();
        Field[] fields = clazz.getDeclaredFields();
        writer.println(Arrays.stream(fields).map(Field::getName).filter(s -> !s.equals("stations")).reduce((s, s2) -> s + ";" + s2).get());
        for (ProductLine productLine : items) {
            writer.println(productLine.getId() + ";" + productLine.getProductLine() + ";" + productLine.getSegment());
        }
    }

    private void writeRegionalbereichCsv(PrintWriter writer) {
        Iterable<Regionalbereich> items = regionalbereichService.getAllRegionalbereiches();
        if (items == null) {
            return;
        }
        Class<?> clazz = items.iterator().next().getClass();
        Field[] fields = clazz.getDeclaredFields();
        writer.println(Arrays.stream(fields).map(Field::getName).filter(s -> !s.equals("stations")).reduce((s, s2) -> s + ";" + s2).get());
        for (Regionalbereich regionalbereich : items) {
            writer.println(regionalbereich.getId() + ";" + regionalbereich.getNumber() + ";" + regionalbereich.getName() + ";" + regionalbereich.getShortName());
        }
    }

    private void writeRemarkCsv(PrintWriter writer) {
        Iterable<Remark> items = remarkService.getAllRemarks();
        if (items == null) {
            return;
        }
        Class<?> clazz = items.iterator().next().getClass();
        Field[] fields = clazz.getDeclaredFields();
        writer.println(Arrays.stream(fields).map(Field::getName).filter(s -> !s.equals("trips")).reduce((s, s2) -> s + ";" + s2).get());
        for (Remark remark : items) {
            writer.println(remark.getId() + ";" +
                    remark.getType() + ";" +
                    remark.getCode() + ";" +
                    remark.getText() + ";" +
                    remark.getCreatedAt());
        }
    }

    private void writeRil100IdentifierCsv(PrintWriter writer) {
        Iterable<Ril100Identifier> items = ril100IdentifierService.getAllRil100Identifiers();
        if (items == null) {
            return;
        }
        Class<?> clazz = items.iterator().next().getClass();
        Field[] fields = clazz.getDeclaredFields();
        writer.println(Arrays.stream(fields).map(Field::getName).filter(s -> !s.equals("stations")).reduce((s, s2) -> s + ";" + s2).get());
        for (Ril100Identifier ril100Identifier : items) {
            writer.println(
                    ril100Identifier.getId() + ";" +
                            ril100Identifier.getRilIdentifier() + ";" +
                            ril100Identifier.isMain() + ";" +
                            ril100Identifier.isHasSteamPermission() + ";" +
                            (ril100Identifier.getGeographicCoordinates() == null ? "null" : ril100Identifier.getGeographicCoordinates().getId()) + ";" +
                            (ril100Identifier.getStation() == null ? "null" : ril100Identifier.getStation().getId())
            );
        }
    }

    private void writeStationLocationCsv(PrintWriter writer) {
        Iterable<StationLocation> items = stationLocationService.getAllStationLocations();
        if (items == null) {
            return;
        }
        Class<?> clazz = items.iterator().next().getClass();
        Field[] fields = clazz.getDeclaredFields();
        writer.println(Arrays.stream(fields).map(Field::getName).filter(s -> !s.equals("stopList")).reduce((s, s2) -> s + ";" + s2).get());
        for (StationLocation stationLocation : items) {
            writer.println(
                    stationLocation.getId() + ";" +
                            stationLocation.getLatitude() + ";" +
                            stationLocation.getLongitude()
            );
        }
    }

    private void writeStationManagementCsv(PrintWriter writer) {
        Iterable<StationManagement> items = stationManagementService.getAllStationManagements();
        if (items == null) {
            return;
        }
        Class<?> clazz = items.iterator().next().getClass();
        Field[] fields = clazz.getDeclaredFields();
        writer.println(Arrays.stream(fields).map(Field::getName).filter(s -> !s.equals("stations")).reduce((s, s2) -> s + ";" + s2).get());
        for (StationManagement stationManagement : items) {
            writer.println(
                    stationManagement.getId() + ";" +
                            stationManagement.getNumber() + ";" +
                            stationManagement.getName()
            );
        }
    }

    private void writeStationCsv(PrintWriter writer) {
        Iterable<Station> items = stationService.getAllStations();
        if (items == null) {
            return;
        }
        Class<?> clazz = items.iterator().next().getClass();
        Field[] fields = clazz.getDeclaredFields();
        writer.println(Arrays.stream(fields).map(Field::getName).filter(s -> !s.equals("ril100Identifiers") && !s.equals("stop")).reduce((s, s2) -> s + ";" + s2).get());
        for (Station station : items) {
            writer.println(
                    station.getAddress().getId() + ";" +
                            station.getLocation().getId() + ";" +
                            station.getOperator().getId() + ";" +
                            station.getTimeTableOffice().getId() + ";" +
                            station.getRegionalbereich().getId() + ";" +
                            station.getStationManagement().getId() + ";" +
                            station.getSzentrale().getId() + ";" +
                            station.getId() + ";" +
                            station.getStationId() + ";" +
                            station.getRelevance() + ";" +
                            station.getScore() + ";" +
                            station.getWeight() + ";" +
                            station.getType() + ";" +
                            station.getRil100() + ";" +
                            station.getNr() + ";" +
                            station.getName() + ";" +
                            station.getCategory() + ";" +
                            station.getPriceCategory() + ";" +
                            station.isHasParking() + ";" +
                            station.isHasBicycleParking() + ";" +
                            station.isHasLocalPublicTransport() + ";" +
                            station.isHasPublicFacilities() + ";" +
                            station.isHasLockerSystem() + ";" +
                            station.isHasTaxiRank() + ";" +
                            station.isHasTravelNecessities() + ";" +
                            station.getHasSteplessAccess() + ";" +
                            station.getHasMobilityService() + ";" +
                            station.isHasWiFi() + ";" +
                            station.isHasTravelCenter() + ";" +
                            station.isHasRailwayMission() + ";" +
                            station.isHasDBLounge() + ";" +
                            station.isHasLostAndFound() + ";" +
                            station.isHasCarRental() + ";" +
                            station.getFederalState() + ";" +
                            station.getCreatedAt() + ";" +
                            station.getProductLine().getId()
            );
        }
    }

    private void writeStopLocationCsv(PrintWriter writer) {
        Iterable<StopLocation> items = stopLocationService.getAllStopLocations();
        if (items == null) {
            return;
        }
        Class<?> clazz = items.iterator().next().getClass();
        Field[] fields = clazz.getDeclaredFields();
        writer.println(Arrays.stream(fields).map(Field::getName).filter(s -> !s.equals("stopList")).reduce((s, s2) -> s + ";" + s2).get());
        for (StopLocation stopLocation : items) {
            writer.println(
                    stopLocation.getId() + ";" +
                            stopLocation.getLatitude() + ";" +
                            stopLocation.getLongitude()
            );
        }
    }

    private void writeStopCsv(PrintWriter writer) {
        Iterable<Stop> items = stopService.getAllStops();
        if (items == null) {
            return;
        }
        Class<?> clazz = items.iterator().next().getClass();
        Field[] fields = clazz.getDeclaredFields();
        writer.println(Arrays.stream(fields).map(Field::getName).reduce((s, s2) -> s + ";" + s2).get());
        for (Stop stop : items) {
            writer.println(
                    stop.getId() + ";" +
                            stop.getType() + ";" +
                            stop.getStopId() + ";" +
                            stop.getName() + ";" +
                            stop.getLocation().getId() + ";" +
                            stop.getProducts() + ";" +
                            stop.getCreatedAt() + ";" +
                            (stop.getStation() == null ? "null" : stop.getStation().getStationId())
            );
        }
    }

    private void writeZentraleCsv(PrintWriter writer) {
        Iterable<Szentrale> items = szentraleService.getAllSzentrales();
        if (items == null) {
            return;
        }
        Class<?> clazz = items.iterator().next().getClass();
        Field[] fields = clazz.getDeclaredFields();
        writer.println(Arrays.stream(fields).map(Field::getName).filter(s -> !s.equals("stations")).reduce((s, s2) -> s + ";" + s2).get());
        for (Szentrale szentrale : items) {
            writer.println(
                    szentrale.getId() + ";" +
                            szentrale.getNumber() + ";" +
                            szentrale.getPublicPhoneNumber() + ";" +
                            szentrale.getName()
            );
        }
    }

    private void writeTimeTableOfficeCsv(PrintWriter writer) {
        Iterable<TimeTableOffice> items = timeTableOfficeService.getAllTimeTableOffices();
        if (items == null) {
            return;
        }
        Class<?> clazz = items.iterator().next().getClass();
        Field[] fields = clazz.getDeclaredFields();
        writer.println(Arrays.stream(fields).map(Field::getName).filter(s -> !s.equals("stations")).reduce((s, s2) -> s + ";" + s2).get());
        for (TimeTableOffice timeTableOffice : items) {
            writer.println(
                    timeTableOffice.getId() + ";" +
                            timeTableOffice.getEmail() + ";" +
                            timeTableOffice.getName()
            );
        }
    }

    public void writeTripCsv(PrintWriter writer, LocalDate startDatePickerValue) {
        LocalDateTime startLocalDateTime = startDatePickerValue.atStartOfDay();
        LocalDateTime endLocalDateTime = startLocalDateTime.plusDays(3);
        Iterable<Trip> items = tripRepository.findAllByPlannedWhenAfterAndPlannedWhenBefore(startLocalDateTime, endLocalDateTime).get();
        // get the trip class
        Field[] fields = Trip.class.getDeclaredFields();
        writer.println(Arrays.stream(fields).map(Field::getName).filter(s -> !s.equals("remarks")).reduce((s, s2) -> s + ";" + s2).get());
        for (Trip trip : items) {
            writer.println(
                    (trip.getId() == null ? "null" : trip.getId()) + ";" +
                            (trip.getTripId() == null ? "null" : trip.getTripId()) + ";" +
                            (trip.getStop() == null ? "null" : trip.getStop().getId()) + ";" +
                            (trip.getWhen() == null ? "null" : trip.getWhen()) + ";" +
                            (trip.getPlannedWhen() == null ? "null" : trip.getPlannedWhen()) + ";" +
                            (trip.getPrognosedWhen() == null ? "null" : trip.getPrognosedWhen()) + ";" +
                            (trip.getDelay() == null ? "null" : trip.getDelay()) + ";" +
                            (trip.getPlatform() == null ? "null" : trip.getPlatform()) + ";" +
                            (trip.getPlannedPlatform() == null ? "null" : trip.getPlannedPlatform()) + ";" +
                            (trip.getPrognosedPlatform() == null ? "null" : trip.getPrognosedPlatform()) + ";" +
                            (trip.getPrognosisType() == null ? "null" : trip.getPrognosisType()) + ";" +
                            (trip.getDirection() == null ? "null" : trip.getDirection()) + ";" +
                            (trip.getProvenance() == null ? "null" : trip.getProvenance()) + ";" +
                            (trip.getLine() == null ? "null" : trip.getLine().getId()) + ";" +
                            (trip.getOrigin() == null ? "null" : trip.getOrigin().getId()) + ";" +
                            (trip.getDestination() == null ? "null" : trip.getDestination().getId()) + ";" +
                            (trip.getCancelled() == null ? "null" : trip.getCancelled()) + ";" +
                            (trip.getLoadFactor() == null ? "null" : trip.getLoadFactor()) + ";" +
                            (trip.getCreatedAt() == null ? "null" : trip.getCreatedAt())
            );

        }
    }

}
