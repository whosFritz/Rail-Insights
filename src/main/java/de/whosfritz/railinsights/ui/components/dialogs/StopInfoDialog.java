package de.whosfritz.railinsights.ui.components.dialogs;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.whosfritz.railinsights.ui.components.Divider;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class StopInfoDialog extends GeneralRailInsightsDialog {

    public StopInfoDialog(Stop stop) {
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);

        VerticalLayout layout = new VerticalLayout();

        TextField stationName = new TextField("Name: ");
        stationName.setValue(stop.getName());
        stationName.setReadOnly(true);

        TextField stationId = new TextField("ID: ");
        stationId.setValue(String.valueOf(stop.getStopId()));
        stationId.setReadOnly(true);

        TextField federalState = new TextField("Bundesland: ");
        federalState.setValue(stop.getStation().getFederalState());
        federalState.setReadOnly(true);

        TextField city = new TextField("Stadt: ");
        city.setValue(stop.getStation().getAddress().getCity());
        city.setReadOnly(true);

        TextField zipcode = new TextField("Postleitzahl: ");
        zipcode.setValue(stop.getStation().getAddress().getZipcode());
        zipcode.setReadOnly(true);

        TextField street = new TextField("Straße: ");
        street.setValue(stop.getStation().getAddress().getStreet());
        street.setReadOnly(true);

        TextField ril100 = new TextField("Ril-100 Identifier: ");
        ril100.setValue(stop.getStation().getRil100());
        ril100.setReadOnly(true);

        FormLayout formLayout = new FormLayout();
        formLayout.add(stationName, stationId, federalState, city, zipcode, street, ril100);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 3));

        TextField hasWifi = new TextField("WLAN: ");
        hasWifi.setValue(stop.getStation().isHasWiFi() ? "Ja" : "Nein");
        hasWifi.setReadOnly(true);
        hasWifi.setPrefixComponent(LineAwesomeIcon.WIFI_SOLID.create());

        TextField hasBycicleParking = new TextField("Fahrradparkplätze: ");
        hasBycicleParking.setValue(stop.getStation().isHasBicycleParking() ? "Ja" : "Nein");
        hasBycicleParking.setReadOnly(true);
        hasBycicleParking.setPrefixComponent(LineAwesomeIcon.BICYCLE_SOLID.create());

        TextField hasCarRental = new TextField("Autovermietung: ");
        hasCarRental.setValue(stop.getStation().isHasCarRental() ? "Ja" : "Nein");
        hasCarRental.setReadOnly(true);
        hasCarRental.setPrefixComponent(LineAwesomeIcon.CAR_SOLID.create());

        TextField hasDBLounge = new TextField("DB Lounge: ");
        hasDBLounge.setValue(stop.getStation().isHasDBLounge() ? "Ja" : "Nein");
        hasDBLounge.setReadOnly(true);
        hasDBLounge.setPrefixComponent(LineAwesomeIcon.COFFEE_SOLID.create());

        TextField hasLocalPublicTransport = new TextField("ÖPNV: ");
        hasLocalPublicTransport.setValue(stop.getStation().isHasLocalPublicTransport() ? "Ja" : "Nein");
        hasLocalPublicTransport.setReadOnly(true);
        hasLocalPublicTransport.setPrefixComponent(LineAwesomeIcon.BUS_SOLID.create());

        TextField hasLockerSystem = new TextField("Schließfächer: ");
        hasLockerSystem.setValue(stop.getStation().isHasLockerSystem() ? "Ja" : "Nein");
        hasLockerSystem.setReadOnly(true);
        hasLockerSystem.setPrefixComponent(LineAwesomeIcon.SUITCASE_SOLID.create());

        TextField hasLostAndFound = new TextField("Fundbüro: ");
        hasLostAndFound.setValue(stop.getStation().isHasLostAndFound() ? "Ja" : "Nein");
        hasLostAndFound.setReadOnly(true);
        hasLostAndFound.setPrefixComponent(LineAwesomeIcon.SEARCH_LOCATION_SOLID.create());

        TextField hasMobilityService = new TextField("Mobilitätsservice: ");
        hasMobilityService.setValue(stop.getStation().getHasMobilityService());
        hasMobilityService.setReadOnly(true);
        hasMobilityService.setPrefixComponent(LineAwesomeIcon.WHEELCHAIR_SOLID.create());

        TextField hasParking = new TextField("Parkplätze: ");
        hasParking.setValue(stop.getStation().isHasParking() ? "Ja" : "Nein");
        hasParking.setReadOnly(true);
        hasParking.setPrefixComponent(LineAwesomeIcon.PARKING_SOLID.create());

        TextField hasPublicFacilities = new TextField("Öffentliche Einrichtungen: ");
        hasPublicFacilities.setValue(stop.getStation().isHasPublicFacilities() ? "Ja" : "Nein");
        hasPublicFacilities.setReadOnly(true);
        hasPublicFacilities.setPrefixComponent(LineAwesomeIcon.TOILET_SOLID.create());

        TextField hasRailwayMission = new TextField("Bahnhofsmission: ");
        hasRailwayMission.setValue(stop.getStation().isHasRailwayMission() ? "Ja" : "Nein");
        hasRailwayMission.setReadOnly(true);
        hasRailwayMission.setPrefixComponent(LineAwesomeIcon.HANDS_HELPING_SOLID.create());

        TextField hasSteplessAccess = new TextField("Barrierefreier Zugang: ");
        hasSteplessAccess.setValue(stop.getStation().getHasSteplessAccess());
        hasSteplessAccess.setReadOnly(true);
        hasSteplessAccess.setPrefixComponent(LineAwesomeIcon.WHEELCHAIR_SOLID.create());

        TextField hasTaxiRank = new TextField("Taxistand: ");
        hasTaxiRank.setValue(stop.getStation().isHasTaxiRank() ? "Ja" : "Nein");
        hasTaxiRank.setReadOnly(true);
        hasTaxiRank.setPrefixComponent(LineAwesomeIcon.TAXI_SOLID.create());

        TextField hasTravelCenter = new TextField("Reisezentrum: ");
        hasTravelCenter.setValue(stop.getStation().isHasTravelCenter() ? "Ja" : "Nein");
        hasTravelCenter.setReadOnly(true);
        hasTravelCenter.setPrefixComponent(LineAwesomeIcon.BUILDING_SOLID.create());

        TextField hasTravelNecessities = new TextField("Reisebedarf: ");
        hasTravelNecessities.setValue(stop.getStation().isHasTravelNecessities() ? "Ja" : "Nein");
        hasTravelNecessities.setReadOnly(true);
        hasTravelNecessities.setPrefixComponent(LineAwesomeIcon.SHOPPING_BAG_SOLID.create());

        FormLayout formLayout2 = new FormLayout();
        formLayout2.add(hasWifi, hasBycicleParking, hasCarRental, hasDBLounge, hasLocalPublicTransport, hasLockerSystem,
                hasLostAndFound, hasMobilityService, hasParking, hasPublicFacilities, hasRailwayMission, hasSteplessAccess,
                hasTaxiRank, hasTravelCenter, hasTravelNecessities);
        formLayout2.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 3));

        TextField nationalExpress = new TextField("Nationaler Express Fernverkehr (z.B. ICE): ");
        nationalExpress.setValue(stop.getProducts().isNationalExpress() ? "Ja" : "Nein");
        nationalExpress.setReadOnly(true);
        nationalExpress.setPrefixComponent(LineAwesomeIcon.TRAIN_SOLID.create());

        TextField national = new TextField("Nationaler Fernverkehr (z.B. IC): ");
        national.setValue(stop.getProducts().isNational() ? "Ja" : "Nein");
        national.setReadOnly(true);
        national.setPrefixComponent(LineAwesomeIcon.TRAIN_SOLID.create());

        TextField regionalExpress = new TextField("Regional Express (z.B. RE): ");
        regionalExpress.setValue(stop.getProducts().isRegionalExp() ? "Ja" : "Nein");
        regionalExpress.setReadOnly(true);
        regionalExpress.setPrefixComponent(LineAwesomeIcon.TRAIN_SOLID.create());

        TextField regional = new TextField("Regional (z.B. RB): ");
        regional.setValue(stop.getProducts().isRegional() ? "Ja" : "Nein");
        regional.setReadOnly(true);
        regional.setPrefixComponent(LineAwesomeIcon.TRAIN_SOLID.create());

        TextField suburban = new TextField("S-Bahn: ");
        suburban.setValue(stop.getProducts().isSuburban() ? "Ja" : "Nein");
        suburban.setReadOnly(true);
        suburban.setPrefixComponent(LineAwesomeIcon.TRAIN_SOLID.create());

        TextField bus = new TextField("Bus: ");
        bus.setValue(stop.getProducts().isBus() ? "Ja" : "Nein");
        bus.setReadOnly(true);
        bus.setPrefixComponent(LineAwesomeIcon.BUS_SOLID.create());

        TextField tram = new TextField("Straßenbahn: ");
        tram.setValue(stop.getProducts().isTram() ? "Ja" : "Nein");
        tram.setReadOnly(true);
        tram.setPrefixComponent(LineAwesomeIcon.TRAIN_SOLID.create());

        TextField ferry = new TextField("Fähre: ");
        ferry.setValue(stop.getProducts().isFerry() ? "Ja" : "Nein");
        ferry.setReadOnly(true);
        ferry.setPrefixComponent(LineAwesomeIcon.SHIP_SOLID.create());

        TextField subway = new TextField("U-Bahn: ");
        subway.setValue(stop.getProducts().isSubway() ? "Ja" : "Nein");
        subway.setReadOnly(true);
        subway.setPrefixComponent(LineAwesomeIcon.TRAIN_SOLID.create());

        TextField taxi = new TextField("Taxi: ");
        taxi.setValue(stop.getProducts().isTaxi() ? "Ja" : "Nein");
        taxi.setReadOnly(true);
        taxi.setPrefixComponent(LineAwesomeIcon.TAXI_SOLID.create());

        FormLayout formLayout3 = new FormLayout();
        formLayout3.add(nationalExpress, national, regionalExpress, regional, suburban, bus, tram, ferry, subway, taxi);
        formLayout3.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 3));

        setHeaderTitle("Informationen zu " + stop.getName());
        layout.add(new H3("Allgemeine Informationen"));
        layout.add(new Divider());
        layout.add(formLayout);
        layout.add(new H3("Informationen zu Angeboten vor Ort"));
        layout.add(new Divider());
        layout.add(formLayout2);
        layout.add(new H3("Angebundener Nah- und Fernverkehr"));
        layout.add(new Divider());
        layout.add(formLayout3);

        add(layout);
        setVisible(true);
    }

}
