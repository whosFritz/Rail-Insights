package de.whosfritz.railinsights.ui.pages;


import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.whosfritz.railinsights.calculation.UniversalCalculator;
import de.whosfritz.railinsights.data.services.trip_services.TripService;
import de.whosfritz.railinsights.ui.components.charts.DailyDelayChart;
import de.whosfritz.railinsights.ui.factories.ButtonFactory;
import de.whosfritz.railinsights.ui.factories.notification.NotificationFactory;
import de.whosfritz.railinsights.ui.factories.notification.NotificationTypes;
import de.whosfritz.railinsights.ui.layout.MainView;
import de.whosfritz.railinsights.ui.services.DataProviderService;
import de.whosfritz.railinsights.utils.PercentageUtil;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import static de.whosfritz.railinsights.ui.components.boards.StationViewDashboard.createHighlight;

@Route(value = "insight", layout = MainView.class)
public class DelayedView extends VerticalLayout {

    private final DataProviderService dataProviderService = VaadinService.getCurrent().getInstantiator().getOrCreate(DataProviderService.class);
    private final TripService tripService;
    VerticalLayout stats = new VerticalLayout();


    public DelayedView(TripService tripService) {
        this.tripService = tripService;
        HorizontalLayout controls = new HorizontalLayout();
        controls.setAlignItems(Alignment.BASELINE);
        controls.addClassNames(LumoUtility.Margin.MEDIUM);


        Button createStatsButton = new Button("Statistik erstellen");
        createStatsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Paragraph p1 = new Paragraph("Hier können Sie sich Statistiken zu Verspätungen und Ausfällen von Zügen anzeigen lassen.");
        Paragraph p2 = new Paragraph("Wählen Sie dazu ein Start- und Enddatum und den Verkehrstyp aus.");

        Button infoButton = ButtonFactory.createInfoButton("Informationen", p1, p2);


        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setLabel("Startdatum");
        startDatePicker.setRequired(true);
        startDatePicker.setRequiredIndicatorVisible(true);
        startDatePicker.setValue(LocalDate.now().minusMonths(1));
        startDatePicker.setLocale(new Locale("de", "DE"));


        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setLabel("Enddatum");
        endDatePicker.setRequired(true);
        endDatePicker.setRequiredIndicatorVisible(true);
        endDatePicker.setValue(LocalDate.now());
        endDatePicker.setLocale(new Locale("de", "DE"));

        createStatsButton.addClickListener(event -> createStats(startDatePicker.getValue(), endDatePicker.getValue(), "Alles"));

        controls.add(infoButton, startDatePicker, endDatePicker, createStatsButton);

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
            NotificationFactory.createNotification(NotificationTypes.ERROR, "Bitte einen Verkehrstyp auswählen.").open();
            return;
        }
        stats.removeAll();
        long startTime = System.currentTimeMillis();

        int allStopsInTimeRange = 0;
        double stopsDelayed = 0;
        double stopsDelayed15min = 0;
        double stopsDelayed30min = 0;
        double stopsDelayed60min = 0;
        double stopsCancelled = 0;
        double stopsOnTime = 0;
        double avgDelayInSeconds = 0; // = tripService.sumOfTripsDelayedMoreThanSixMinutes(startDate.atStartOfDay(), endDate.atStartOfDay(), finalLines, 360) / stopsDelayed;
        List<Object[]> results = null;

        if (trafficType.equals("Nahverkehr")) {
            allStopsInTimeRange = tripService.countAlleStopsInDiesemZeitraumNah(startDate.atStartOfDay(), endDate.atStartOfDay());
            stopsDelayed = tripService.countDatumDazwischenDelayNah(startDate.atStartOfDay(), endDate.atStartOfDay(), 360);
            stopsDelayed15min = tripService.countDatumDazwischenDelayNah(startDate.atStartOfDay(), endDate.atStartOfDay(), 900);
            stopsDelayed30min = tripService.countDatumDazwischenDelayNah(startDate.atStartOfDay(), endDate.atStartOfDay(), 1800);
            stopsDelayed60min = tripService.countDatumDazwischenDelayNah(startDate.atStartOfDay(), endDate.atStartOfDay(), 3600);
            stopsCancelled = tripService.countAusfaelleNah(startDate.atStartOfDay(), endDate.atStartOfDay());
            stopsOnTime = allStopsInTimeRange - stopsDelayed - stopsCancelled;
            avgDelayInSeconds = tripService.sumOfTripsDelayedMoreThanSixMinutesNah(startDate.atStartOfDay(), endDate.atStartOfDay(), 360) / stopsDelayed;
            results = tripService.getAverageDelayByDateNah(startDate.atStartOfDay(), endDate.atStartOfDay()).getData();
        } else if (trafficType.equals("Fernverkehr")) {
            allStopsInTimeRange = tripService.countAlleStopsInDiesemZeitraumFern(startDate.atStartOfDay(), endDate.atStartOfDay());
            stopsDelayed = tripService.countDatumDazwischenDelayFern(startDate.atStartOfDay(), endDate.atStartOfDay(), 360);
            stopsDelayed15min = tripService.countDatumDazwischenDelayFern(startDate.atStartOfDay(), endDate.atStartOfDay(), 900);
            stopsDelayed30min = tripService.countDatumDazwischenDelayFern(startDate.atStartOfDay(), endDate.atStartOfDay(), 1800);
            stopsDelayed60min = tripService.countDatumDazwischenDelayFern(startDate.atStartOfDay(), endDate.atStartOfDay(), 3600);
            stopsCancelled = tripService.countAusfaelleFern(startDate.atStartOfDay(), endDate.atStartOfDay());
            stopsOnTime = allStopsInTimeRange - stopsDelayed - stopsCancelled;
            avgDelayInSeconds = tripService.sumOfTripsDelayedMoreThanSixMinutesFern(startDate.atStartOfDay(), endDate.atStartOfDay(), 360) / stopsDelayed;
            results = tripService.getAverageDelayByDateFern(startDate.atStartOfDay(), endDate.atStartOfDay()).getData();

        } else {
            allStopsInTimeRange = tripService.countAlleStopsInDiesemZeitraum(startDate.atStartOfDay(), endDate.atStartOfDay());
            stopsDelayed = tripService.countDatumDazwischenDelay(startDate.atStartOfDay(), endDate.atStartOfDay(), 360);
            stopsDelayed15min = tripService.countDatumDazwischenDelay(startDate.atStartOfDay(), endDate.atStartOfDay(), 900);
            stopsDelayed30min = tripService.countDatumDazwischenDelay(startDate.atStartOfDay(), endDate.atStartOfDay(), 1800);
            stopsDelayed60min = tripService.countDatumDazwischenDelay(startDate.atStartOfDay(), endDate.atStartOfDay(), 3600);
            stopsCancelled = tripService.countAusfaelle(startDate.atStartOfDay(), endDate.atStartOfDay());
            stopsOnTime = allStopsInTimeRange - stopsDelayed - stopsCancelled;
            avgDelayInSeconds = tripService.sumOfTripsDelayedMoreThanSixMinutes(startDate.atStartOfDay(), endDate.atStartOfDay()) / stopsDelayed;
            results = tripService.getAverageDelayByDate(startDate.atStartOfDay(), endDate.atStartOfDay()).getData();
        }

        if (allStopsInTimeRange == 0) {
            NotificationFactory.createNotification(NotificationTypes.ERROR, "Es wurden keine Halte in dem Zeitraum gefunden.").open();
            return;
        }


        double percentageStopsOnTime = PercentageUtil.convertToTwoDecimalPlaces(stopsOnTime / allStopsInTimeRange * 100);
        double percentageStopsDelayed = PercentageUtil.convertToTwoDecimalPlaces(stopsDelayed / allStopsInTimeRange * 100);
        double percentageStopsDelayed15min = PercentageUtil.convertToTwoDecimalPlaces(stopsDelayed15min / allStopsInTimeRange * 100);
        double percentageStopsDelayed30min = PercentageUtil.convertToTwoDecimalPlaces(stopsDelayed30min / allStopsInTimeRange * 100);
        double percentageStopsDelayed60min = PercentageUtil.convertToTwoDecimalPlaces(stopsDelayed60min / allStopsInTimeRange * 100);
        double percentageCancelled = PercentageUtil.convertToTwoDecimalPlaces(stopsCancelled / allStopsInTimeRange * 100);

        double globalStopsOnTime = dataProviderService.getStopsPercentageOnTime() != null ? dataProviderService.getStopsPercentageOnTime() : 0;
        double globalStopsDelayed = dataProviderService.getStopsPercentageDelayed() != null ? dataProviderService.getStopsPercentageDelayed() : 0;
        double globalStopsDelayedMoreThan15min = dataProviderService.getStopsPercentageDelayedMoreThan15min() != null ? dataProviderService.getStopsPercentageDelayedMoreThan15min() : 0;
        double globalStopsDelayedMoreThan30min = dataProviderService.getStopsPercentageDelayedMoreThan30min() != null ? dataProviderService.getStopsPercentageDelayedMoreThan30min() : 0;
        double globalStopsDelayedMoreThan60min = dataProviderService.getStopsPercentageDelayedMoreThan60min() != null ? dataProviderService.getStopsPercentageDelayedMoreThan60min() : 0;
        double globalStopsCancelled = dataProviderService.getStopsPercentageCancelled() != null ? dataProviderService.getStopsPercentageCancelled() : 0;

        double onTimeGlobalDifferencePercentage = PercentageUtil.convertToTwoDecimalPlaces(percentageStopsOnTime - globalStopsOnTime);
        double delayedGlobalDifferencePercentage = PercentageUtil.convertToTwoDecimalPlaces(percentageStopsDelayed - globalStopsDelayed);
        double delayedMoreThan15minGlobalDifferencePercentage = PercentageUtil.convertToTwoDecimalPlaces(percentageStopsDelayed15min - globalStopsDelayedMoreThan15min);
        double delayedMoreThan30minGlobalDifferencePercentage = PercentageUtil.convertToTwoDecimalPlaces(percentageStopsDelayed30min - globalStopsDelayedMoreThan30min);
        double delayedMoreThan60minGlobalDifferencePercentage = PercentageUtil.convertToTwoDecimalPlaces(percentageStopsDelayed60min - globalStopsDelayedMoreThan60min);
        double cancelledGlobalDifferencePercentage = PercentageUtil.convertToTwoDecimalPlaces(percentageCancelled - globalStopsCancelled);


        TreeMap<LocalDate, Double> tripsCorrespondingToProducts = new TreeMap<>();
        assert results != null;
        for (Object[] result : results) {
            LocalDate tripDate = ((Date) result[0]).toLocalDate();
            BigDecimal avgDelayDecimal = (BigDecimal) result[1];
            Double avgDelay = avgDelayDecimal.doubleValue();
            tripsCorrespondingToProducts.put(tripDate, avgDelay);
        }

        DataSeries dailyDelaySeries = UniversalCalculator.createDailyDelaySeriesForInsight(tripsCorrespondingToProducts);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        NotificationFactory.createNotification(NotificationTypes.SUCCESS, "Statistiken erstellt in " + duration / 1000 + " Sekunden").open();

        Board board = new Board();
        board.addRow(
                createHighlight("Anzahl aller Halte in dem Zeitraum", String.valueOf(allStopsInTimeRange)),
                createHighlight("Pünktlichen Halte", percentageStopsOnTime + "%", onTimeGlobalDifferencePercentage, "Pünktlichkeit im deutschlandweiten Vergleich", true),
                createHighlight("Ausgefallene Halte", percentageCancelled + "%", cancelledGlobalDifferencePercentage, "Ausfälle im deutschlandweiten Vergleich", false),
                createHighlight("Durchschnittliche Verspätung", UniversalCalculator.secondsToHoursMinutesAndSeconds(avgDelayInSeconds))
        );
        board.addRow(
                createHighlight("Halte mit Verspätung", percentageStopsDelayed + "%", delayedGlobalDifferencePercentage, "Verspätungen im deutschlandweiten Vergleich", false),
                createHighlight("Halte mit mehr als 15 Minuten Verspätung", percentageStopsDelayed15min + "%", delayedMoreThan15minGlobalDifferencePercentage, "Im deutschlandweiten Vergleich", false),
                createHighlight("Halte mit mehr als 30 Minuten Verspätung", percentageStopsDelayed30min + "%", delayedMoreThan30minGlobalDifferencePercentage, "Im deutschlandweiten Vergleich", false),
                createHighlight("Halte mit mehr als 60 Minuten Verspätung", percentageStopsDelayed60min + "%", delayedMoreThan60minGlobalDifferencePercentage, "Im deutschlandweiten Vergleich", false)
        );
        board.addRow(new DailyDelayChart(dailyDelaySeries));
        stats.add(board);
    }
}

