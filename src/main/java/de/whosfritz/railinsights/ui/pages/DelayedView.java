package de.whosfritz.railinsights.ui.pages;


import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import de.whosfritz.railinsights.data.services.LineService;
import de.whosfritz.railinsights.data.services.trip_services.TripService;
import de.whosfritz.railinsights.ui.components.dialogs.ButtonFactory;
import de.whosfritz.railinsights.ui.factories.notification.NotificationFactory;
import de.whosfritz.railinsights.ui.factories.notification.NotificationTypes;
import de.whosfritz.railinsights.ui.layout.MainView;
import de.whosfritz.railinsights.ui.services.DataProviderService;
import de.whosfritz.railinsights.utils.PercentageUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static de.whosfritz.railinsights.ui.components.boards.StationViewDashboard.createHighlight;

@Route(value = "verspätungen", layout = MainView.class)
public class DelayedView extends VerticalLayout {

    private final LineService lineService;
    private final TripService tripService;
    private final DataProviderService dataProviderService = VaadinService.getCurrent().getInstantiator().getOrCreate(DataProviderService.class);
    VerticalLayout stats = new VerticalLayout();


    public DelayedView(LineService lineService, TripService tripService) {
        this.lineService = lineService;
        this.tripService = tripService;
        HorizontalLayout controls = new HorizontalLayout();
        controls.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        Button createStatsButton = new Button("Statistik erstellen");

        Paragraph p1 = new Paragraph("This page shows the delays of the trains.");
        Paragraph p2 = new Paragraph("You can select a time period and a start and end date to create the stats.");
        Button infoButton = ButtonFactory.createInfoButton("Informationen", p1, p2);

        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        radioButtonGroup.setLabel("Select a time period");
        radioButtonGroup.setItems("Nahverkehr", "Fernverkehr", "Alles");
        radioButtonGroup.setRequired(true);
        radioButtonGroup.setRequiredIndicatorVisible(true);
        radioButtonGroup.setValue("Nahverkehr");

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setLabel("Startdatum");
        startDatePicker.setRequired(true);
        startDatePicker.setRequiredIndicatorVisible(true);
        startDatePicker.setValue(LocalDate.now().minusMonths(1));

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setLabel("Enddatum");
        endDatePicker.setRequired(true);
        endDatePicker.setRequiredIndicatorVisible(true);
        endDatePicker.setValue(LocalDate.now());

        createStatsButton.addClickListener(event -> {
            createStats(startDatePicker.getValue(), endDatePicker.getValue(), radioButtonGroup.getValue());
        });

        controls.add(infoButton, startDatePicker, endDatePicker, radioButtonGroup, createStatsButton);

        add(controls, stats);
    }

    private void createStats(LocalDate startDate, LocalDate endDate, String trafficType) {
        if (startDate == null || endDate == null) {
            NotificationFactory.createNotification(NotificationTypes.ERROR, "Bitte ein Start- und Enddatum auswählen.").open();
            return;
        }
        if (startDate.isAfter(endDate)) {
            NotificationFactory.createNotification(NotificationTypes.ERROR, "Das Startdatum darf nicht nach dem Enddatum liegen.").open();
            return;
        }
        if (trafficType == null) {
            NotificationFactory.createNotification(NotificationTypes.ERROR, "Bitte einen Zugtyp auswählen.").open();
            return;
        }
        stats.removeAll();

        List<String> lines = null;

        if (trafficType.equals("Nahverkehr")) {
            lines = List.of("regional", "suburban", "regionalExpress");
        } else if (trafficType.equals("Fernverkehr")) {
            lines = List.of("national", "nationalexpress");
        } else {
            lines = List.of("regional", "suburban", "regionalExpress", "national", "nationalexpress");
        }
        List<String> finalLines = lines;
        CompletableFuture<Integer> futureAllStops = CompletableFuture.supplyAsync(() -> tripService.findAllStopsInThisTimeRange(startDate.atStartOfDay(), endDate.atStartOfDay(), finalLines));
        CompletableFuture<Integer> futureStopsmitallgVerspaetung = CompletableFuture.supplyAsync(() -> tripService.findAllByPlannedWhenIsAfterAndPlannedWhenIsBeforeAndLinesAndDelayIsGreaterThanOrEqualTo(startDate.atStartOfDay(), endDate.atStartOfDay(), finalLines, 360));
        CompletableFuture<Integer> futureStopsmitmehrals15minverspoetung = CompletableFuture.supplyAsync(() -> tripService.findAllByPlannedWhenIsAfterAndPlannedWhenIsBeforeAndLinesAndDelayIsGreaterThanOrEqualTo(startDate.atStartOfDay(), endDate.atStartOfDay(), finalLines, 900));
        CompletableFuture<Integer> futureStopsmitmehrals30minverspoetung = CompletableFuture.supplyAsync(() -> tripService.findAllByPlannedWhenIsAfterAndPlannedWhenIsBeforeAndLinesAndDelayIsGreaterThanOrEqualTo(startDate.atStartOfDay(), endDate.atStartOfDay(), finalLines, 1800));
        CompletableFuture<Integer> futureStopsmitmehrals60minverspoetung = CompletableFuture.supplyAsync(() -> tripService.findAllByPlannedWhenIsAfterAndPlannedWhenIsBeforeAndLinesAndDelayIsGreaterThanOrEqualTo(startDate.atStartOfDay(), endDate.atStartOfDay(), finalLines, 3600));
        CompletableFuture<Integer> futureStopsausgefallen = CompletableFuture.supplyAsync(() -> tripService.findAllAusgefallene(startDate.atStartOfDay(), endDate.atStartOfDay(), finalLines));

// Then you can get the results with the join method (this method waits for the computation to complete if necessary)
        int alleStopsInDemZeitraum = futureAllStops.join();
        double stopsmitallgVerspaetung = futureStopsmitallgVerspaetung.join();
        double stopsmitmehrals15minverspoetung = futureStopsmitmehrals15minverspoetung.join();
        double stopsmitmehrals30minverspoetung = futureStopsmitmehrals30minverspoetung.join();
        double stopsmitmehrals60minverspoetung = futureStopsmitmehrals60minverspoetung.join();
        double stopsausgefallen = futureStopsausgefallen.join();

        double stopsOnTime = alleStopsInDemZeitraum - stopsmitallgVerspaetung - stopsausgefallen;

        double percentageStopsOnTime = PercentageUtil.convertToTwoDecimalPlaces(stopsOnTime / alleStopsInDemZeitraum * 100);
        double percentageStopsmitallgVerspaetung = PercentageUtil.convertToTwoDecimalPlaces(stopsmitallgVerspaetung / alleStopsInDemZeitraum * 100);
        double percentageStopsmitmehrals15minverspoetung = PercentageUtil.convertToTwoDecimalPlaces(stopsmitmehrals15minverspoetung / alleStopsInDemZeitraum * 100);
        double percentageStopsmitmehrals30minverspoetung = PercentageUtil.convertToTwoDecimalPlaces(stopsmitmehrals30minverspoetung / alleStopsInDemZeitraum * 100);
        double percentageStopsmitmehrals60minverspoetung = PercentageUtil.convertToTwoDecimalPlaces(stopsmitmehrals60minverspoetung / alleStopsInDemZeitraum * 100);
        double percentageStopsausgefallen = PercentageUtil.convertToTwoDecimalPlaces(stopsausgefallen / alleStopsInDemZeitraum * 100);

        double globalStopsOnTime = dataProviderService.getStopsPercentageOnTime() != null ? dataProviderService.getStopsPercentageOnTime() : 0;
        double globalStopsDelayed = dataProviderService.getStopsPercentageDelayed() != null ? dataProviderService.getStopsPercentageDelayed() : 0;
        double globalStopsDelayedMoreThan15min = dataProviderService.getStopsPercentageDelayedMoreThan15min() != null ? dataProviderService.getStopsPercentageDelayedMoreThan15min() : 0;
        double globalStopsDelayedMoreThan30min = dataProviderService.getStopsPercentageDelayedMoreThan30min() != null ? dataProviderService.getStopsPercentageDelayedMoreThan30min() : 0;
        double globalStopsDelayedMoreThan60min = dataProviderService.getStopsPercentageDelayedMoreThan60min() != null ? dataProviderService.getStopsPercentageDelayedMoreThan60min() : 0;
        double globalStopsCancelled = dataProviderService.getStopsPercentageCancelled() != null ? dataProviderService.getStopsPercentageCancelled() : 0;

        double onTimeGlobalDifferencePercentage = PercentageUtil.convertToTwoDecimalPlaces(percentageStopsOnTime - globalStopsOnTime);
        double delayedGlobalDifferencePercentage = PercentageUtil.convertToTwoDecimalPlaces(percentageStopsmitallgVerspaetung - globalStopsDelayed);
        double delayedMoreThan15minGlobalDifferencePercentage = PercentageUtil.convertToTwoDecimalPlaces(percentageStopsmitmehrals15minverspoetung - globalStopsDelayedMoreThan15min);
        double delayedMoreThan30minGlobalDifferencePercentage = PercentageUtil.convertToTwoDecimalPlaces(percentageStopsmitmehrals30minverspoetung - globalStopsDelayedMoreThan30min);
        double delayedMoreThan60minGlobalDifferencePercentage = PercentageUtil.convertToTwoDecimalPlaces(percentageStopsmitmehrals60minverspoetung - globalStopsDelayedMoreThan60min);
        double cancelledGlobalDifferencePercentage = PercentageUtil.convertToTwoDecimalPlaces(percentageStopsausgefallen - globalStopsCancelled);


        Board board = new Board();
        board.addRow(
                createHighlight("Anzahl aller Halte in dem Zeitraum", String.valueOf(alleStopsInDemZeitraum)),
                createHighlight("Anzahl aller Halte die pünktlich waren", String.valueOf(percentageStopsOnTime), onTimeGlobalDifferencePercentage, "Pünktlichkeit im deutschlandweiten Vergleich", true),
                createHighlight("Anteil aller Halte mit Verspätung", percentageStopsmitallgVerspaetung + "%", delayedGlobalDifferencePercentage, "Verspätungen im deutschlandweiten Vergleich", false),
                createHighlight("Anteil aller Halte die ausgefallen sind", percentageStopsausgefallen + "%", cancelledGlobalDifferencePercentage, "Ausfälle im deutschlandweiten Vergleich", false)
        );
        board.addRow(
                createHighlight("Anteil aller Halte mit mehr als 15 Minuten Verspätung", percentageStopsmitmehrals15minverspoetung + "%", delayedMoreThan15minGlobalDifferencePercentage, "Im deutschlandweiten Vergleich", false),
                createHighlight("Anteil aller Halte mit mehr als 30 Minuten Verspätung", percentageStopsmitmehrals30minverspoetung + "%", delayedMoreThan30minGlobalDifferencePercentage, "Im deutschlandweiten Vergleich", false),
                createHighlight("Anteil aller Halte mit mehr als 60 Minuten Verspätung", percentageStopsmitmehrals60minverspoetung + "%", delayedMoreThan60minGlobalDifferencePercentage, "Im deutschlandweiten Vergleich", false)
        );
        stats.add(board);
    }
}