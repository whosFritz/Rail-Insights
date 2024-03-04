package de.whosfritz.railinsights.ui.pages;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.olech2412.adapter.dbadapter.model.stop.sub.Line;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.whosfritz.railinsights.calculation.UniversalCalculator;
import de.whosfritz.railinsights.data.LoadFactor;
import de.whosfritz.railinsights.data.dto.TripStatistics;
import de.whosfritz.railinsights.data.services.LineService;
import de.whosfritz.railinsights.data.services.trip_services.TripService;
import de.whosfritz.railinsights.ui.components.dialogs.GeneralRailInsightsDialog;
import de.whosfritz.railinsights.ui.factories.notification.NotificationFactory;
import de.whosfritz.railinsights.ui.factories.notification.NotificationTypes;
import de.whosfritz.railinsights.ui.layout.MainView;
import de.whosfritz.railinsights.ui.services.DataProviderService;
import de.whosfritz.railinsights.utils.DateTimeLabelFormatsUtil;
import de.whosfritz.railinsights.utils.PercentageUtil;
import de.whosfritz.railinsights.utils.TripUtil;
import jakarta.transaction.Transactional;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static de.whosfritz.railinsights.ui.components.boards.StationViewDashboard.createHighlight;

@Route(value = "trainmetrics", layout = MainView.class)
@Transactional
public class TrainStatsView extends VerticalLayout {
    private final UniversalCalculator universalCalculator = new UniversalCalculator();

    private final TripService tripService;
    private final ComboBox<Line> fernVerkehrLinesCombobox = new ComboBox<>();
    private final DataProviderService dataProviderService = VaadinService.getCurrent().getInstantiator().getOrCreate(DataProviderService.class);

    private final VerticalLayout tripStatsLayout = new VerticalLayout();
    private final DatePicker startDatePicker = new DatePicker();
    private final DatePicker endDatePicker = new DatePicker();


    public TrainStatsView(TripService tripService, LineService lineService) {
        this.tripService = tripService;


        HorizontalLayout inputsLayout = new HorizontalLayout();
        inputsLayout.addClassNames(LumoUtility.Margin.MEDIUM);
        inputsLayout.setAlignItems(Alignment.BASELINE);

        Paragraph infoParagraph = new Paragraph("Hier kannst du dir die Statistiken zu Zügen anzeigen lassen.");
        Paragraph infoCalcParagraph = new Paragraph("Wähle aus der Liste einen Fernverkehrszug aus und gib den Zeitraum an, für den du die Statistiken sehen möchtest. z.B. 2.3.2024 - 4.3.2024 wird dir die Daten von 2.3.2024 0 Uhr bis zum 5.3.2024 0 Uhr anzeigen, ergo den vollen ausgewählten Endtag.");


        Button infoButton = new Button("Informationen");
        infoButton.setIcon(new Icon(VaadinIcon.INFO_CIRCLE));
        infoButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        infoButton.setAriaLabel("Informationen");
        infoButton.addClickListener(e -> {
            HorizontalLayout infoLayout = new HorizontalLayout(new VerticalLayout(infoParagraph, infoCalcParagraph));
            infoLayout.setWidth(100f, Unit.PERCENTAGE);
            infoLayout.setMaxWidth(100f, Unit.PERCENTAGE);

            GeneralRailInsightsDialog dialog = new GeneralRailInsightsDialog();
            dialog.setHeaderTitle("Informationen zur Seite");
            dialog.add(infoLayout);
            dialog.open();
        });

        fernVerkehrLinesCombobox.setItems(lineService.getLinesNationalOrNationalExpress().getData());
        fernVerkehrLinesCombobox.setItemLabelGenerator(Line::getName);
        fernVerkehrLinesCombobox.setLabel("Fernverkehrszug");
        fernVerkehrLinesCombobox.addClassNames(LumoUtility.Margin.Top.MEDIUM);
        fernVerkehrLinesCombobox.setPrefixComponent(LineAwesomeIcon.SUBWAY_SOLID.create());
        fernVerkehrLinesCombobox.setClearButtonVisible(true);

        startDatePicker.setLabel("Startdatum");
        startDatePicker.setValue(LocalDate.now().minusMonths(1));
        startDatePicker.addClassNames(LumoUtility.Margin.Top.MEDIUM);
        startDatePicker.setLocale(new Locale("de", "DE"));

        endDatePicker.setLabel("Enddatum");
        endDatePicker.setValue(LocalDate.now());
        endDatePicker.addClassNames(LumoUtility.Margin.Top.MEDIUM);
        endDatePicker.setLocale(new Locale("de", "DE"));

        Button calculateStatsButton = new Button("Statistiken berechnen");
        calculateStatsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        calculateStatsButton.addClickListener(e -> createStats());

        Button clearButton = new Button("Zurücksetzen");
        clearButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        clearButton.addClickListener(e -> {
            tripStatsLayout.removeAll();
            fernVerkehrLinesCombobox.clear();
        });


        // Todo: Remarks / Bemerkungen zu den Fahrten (idk yet)

        inputsLayout.add(infoButton, startDatePicker, endDatePicker, fernVerkehrLinesCombobox, calculateStatsButton, clearButton);

        add(inputsLayout, tripStatsLayout);
        tripStatsLayout.setVisible(false);

        setSizeFull();
    }


    private void createStats() {
        Line comboboxValue = fernVerkehrLinesCombobox.getValue();
        if (fernVerkehrLinesCombobox.getValue() == null) return;
        tripStatsLayout.getElement().removeAllChildren();

        LocalDateTime from = startDatePicker.getValue().atStartOfDay();
        LocalDateTime to = endDatePicker.getValue().atStartOfDay().plusDays(1);
        if (from.isAfter(to)) {
            NotificationFactory.createNotification(NotificationTypes.ERROR, "Bitte einen validen Zeitraum wählen").open();
            return;
        }

        List<Trip> tripsCorrespondingToLine = tripService.findAllByPlannedWhenIsAfterAndPlannedWhenIsBeforeAndLine_FahrtNr(from, to, comboboxValue.getName()).getData();
        // sort by plannedwhen ascending
        tripsCorrespondingToLine.sort(Comparator.comparing(Trip::getPlannedWhen));
        // when null notifcation
        if (tripsCorrespondingToLine.isEmpty()) {
            NotificationFactory.createNotification(NotificationTypes.WARNING, "Der Zeitraum ist zu kurz gewählt. Es wurden keine Daten gefunden").open();
            return;
        }
        tripsCorrespondingToLine = TripUtil.removeDuplicates(tripsCorrespondingToLine);
        tripsCorrespondingToLine.sort(Comparator.comparing(Trip::getPlannedWhen));

        int fahrtenCount = tripsCorrespondingToLine.stream().filter(trip -> trip.getDirection() == null && trip.getCancelled() == null).mapToInt(trip -> 1).sum();
        long tripsDelaysMoreThan0 = tripsCorrespondingToLine.stream().filter(trip -> trip.getDelay() != null && trip.getDelay() >= 0).count();
        int sumOfTripsDelayedMoreThanSixMinutes = tripsCorrespondingToLine.stream().filter(trip -> trip.getDelay() != null && trip.getDelay() >= 360).mapToInt(Trip::getDelay).sum();
        double avgDelayInSeconds = (double) sumOfTripsDelayedMoreThanSixMinutes / tripsDelaysMoreThan0;

        TripStatistics tripStatistics = universalCalculator.calculateTripStatistics(tripsCorrespondingToLine);


        double globalOnTimePercentage = (dataProviderService.getStopsPercentageOnTime() != null) ? dataProviderService.getStopsPercentageOnTime() : 0.0;
        double globalDelayedPercentage = (dataProviderService.getStopsPercentageDelayed() != null) ? dataProviderService.getStopsPercentageDelayed() : 0.0;
        double globalCancelledPercentage = (dataProviderService.getStopsPercentageCancelled() != null) ? dataProviderService.getStopsPercentageCancelled() : 0.0;
        double globalDelayedMoreThan6min = (dataProviderService.getStopsPercentageDelayedMoreThan6min() != null) ? dataProviderService.getStopsPercentageDelayedMoreThan6min() : 0.0;
        double globalDelayedMoreThan15min = (dataProviderService.getStopsPercentageDelayedMoreThan15min() != null) ? dataProviderService.getStopsPercentageDelayedMoreThan15min() : 0.0;
        double globalDelayedMoreThan60min = (dataProviderService.getStopsPercentageDelayedMoreThan60min() != null) ? dataProviderService.getStopsPercentageDelayedMoreThan60min() : 0.0;


        double percentageOnTime = tripStatistics.getPercentageOnTime();
        double percentageDelayed = tripStatistics.getPercentageDelayed();
        double percentageCancelled = tripStatistics.getPercentageCancelled();
        double delayMoreThan6min = tripStatistics.getDelayMoreThan6min();
        double delayMoreThan15min = tripStatistics.getDelayMoreThan15min();
        double delayMoreThan60min = tripStatistics.getDelayMoreThan60min();


        double onTimeGlobalComparison = percentageOnTime - globalOnTimePercentage;
        double delayedGlobalComparison = percentageDelayed - globalDelayedPercentage;
        double cancelledGlobalComparison = percentageCancelled - globalCancelledPercentage;
        double delayedMoreThan6minGlobalComparison = delayMoreThan6min - globalDelayedMoreThan6min;
        double delayedMoreThan15minGlobalComparison = delayMoreThan15min - globalDelayedMoreThan15min;
        double delayedMoreThan60minGlobalComparison = delayMoreThan60min - globalDelayedMoreThan60min;


        onTimeGlobalComparison = PercentageUtil.convertToTwoDecimalPlaces(onTimeGlobalComparison);
        delayedGlobalComparison = PercentageUtil.convertToTwoDecimalPlaces(delayedGlobalComparison);
        cancelledGlobalComparison = PercentageUtil.convertToTwoDecimalPlaces(cancelledGlobalComparison);
        delayedMoreThan6minGlobalComparison = PercentageUtil.convertToTwoDecimalPlaces(delayedMoreThan6minGlobalComparison);
        delayedMoreThan15minGlobalComparison = PercentageUtil.convertToTwoDecimalPlaces(delayedMoreThan15minGlobalComparison);
        delayedMoreThan60minGlobalComparison = PercentageUtil.convertToTwoDecimalPlaces(delayedMoreThan60minGlobalComparison);

        String zielBahnhof = tripsCorrespondingToLine.stream().filter(trip -> trip.getDirection() != null).findFirst().map(Trip::getDirection).orElse("Unbekannt");

        Board board = new Board();
        board.addRow(
                createHighlight("Zug: ", comboboxValue.getName()),
                createHighlight("Anzahl der Fahrten", String.valueOf(fahrtenCount))
        );
        board.addRow(
                createHighlight("Richtung", zielBahnhof),
                createHighlight("Durchschnittliche Verspätung", UniversalCalculator.minutesToHoursAndMinutesAndSeconds(avgDelayInSeconds / 60.0))
        );
        Row abfahrtenStats1 = new Row();
        abfahrtenStats1.add(createHighlight("Pünktliche Stopps", percentageOnTime + " %", onTimeGlobalComparison, "Pünktlichkeit im globalen Vergleich", true));
        abfahrtenStats1.add(createHighlight("Verspätete Stopps", percentageDelayed + " %", delayedGlobalComparison, "Verspätungen im globalen Vergleich", false));
        abfahrtenStats1.add(createHighlight("Ausgefallene Stopps", percentageCancelled + " %", cancelledGlobalComparison, "Ausfälle im globalen Vergleich", false));

        Row abfahrtenStats2 = new Row();
        abfahrtenStats2.add(createHighlight("Verspätung über 6 min", delayMoreThan6min + " %", delayedMoreThan6minGlobalComparison, "Verspätungen im globalen Vergleich", false));
        abfahrtenStats2.add(createHighlight("Verspätung über 15 min", delayMoreThan15min + " %", delayedMoreThan15minGlobalComparison, "Verspätungen im globalen Vergleich", false));
        abfahrtenStats2.add(createHighlight("Verspätung über 60 min", delayMoreThan60min + " %", delayedMoreThan60minGlobalComparison, "Verspätungen im globalen Vergleich", false));

        abfahrtenStats2.addClassNames(LumoUtility.Margin.Bottom.XLARGE);
        board.addRow(abfahrtenStats1);
        board.addRow(abfahrtenStats2);
        DataSeries dailyDelaySeries = UniversalCalculator.buildDailyDelaySeries(tripsCorrespondingToLine);
        board.addRow(createDailyDelayChart(dailyDelaySeries));
        // nicht median, sondern höchste und geringste auslastung
        DataSeries dailyLowestLoadFactorSeries = UniversalCalculator.buildDailyLowestLoadFactorSeries(tripsCorrespondingToLine);
        DataSeries dailyHighestLoadFactorSeries = UniversalCalculator.buildDailyHighestLoadFactorSeries(tripsCorrespondingToLine);
        board.addRow(createDailyLoadFactorChart(List.of(dailyLowestLoadFactorSeries, dailyHighestLoadFactorSeries)));

        // Todo: Remarks Feed or something

        tripStatsLayout.add(board);
        tripStatsLayout.setVisible(true);

    }

    private Component createDailyDelayChart(DataSeries dailyDelaySeries) {
        Chart chart = new Chart(ChartType.LINE);

        chart.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.XLARGE);

        Configuration conf = chart.getConfiguration();
        conf.setTitle("Verspätung");
        conf.setSubTitle("Durchschnittliche Verspätung pro Tag in Minuten");

        conf.addSeries(dailyDelaySeries);
        conf.getChart().setStyledMode(true);

        conf.setLegend(new Legend(false));

        Tooltip tooltip = chart.getConfiguration().getTooltip();
        tooltip.setShared(true);
        tooltip.setDateTimeLabelFormats(DateTimeLabelFormatsUtil.getCleanedDateTimeLabelFormat());

        YAxis yAxis = chart.getConfiguration().getyAxis();
        yAxis.setTitle(new AxisTitle());


        XAxis xAxis = chart.getConfiguration().getxAxis();
        xAxis.setType(AxisType.DATETIME);
        xAxis.setTitle(new AxisTitle());

        PlotOptionsLine plotOptions = new PlotOptionsLine();
        plotOptions.setEnableMouseTracking(true);
        plotOptions.setDataLabels(new DataLabels(true));
        conf.setPlotOptions(plotOptions);

        return chart;
    }

    public Component createDailyLoadFactorChart(List<DataSeries> dailyLoadFactorSeries) {
        Chart chart = new Chart(ChartType.LINE);

        chart.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.XLARGE);

        Configuration conf = chart.getConfiguration();
        conf.setTitle("Auslastung");
        conf.setSubTitle("Höchste und niedrigste Auslastung pro Tag");

        dailyLoadFactorSeries.forEach(conf::addSeries);
        conf.getChart().setStyledMode(true);

        YAxis yAxis = chart.getConfiguration().getyAxis();
        yAxis.setTitle(new AxisTitle());


        // show corresponding enum alias on yaxis
        yAxis.setCategories(Arrays.stream(LoadFactor.values()).map(LoadFactor::getAlias).toArray(String[]::new));

        XAxis xAxis = chart.getConfiguration().getxAxis();
        xAxis.setType(AxisType.DATETIME);
        xAxis.setTitle(new AxisTitle());

        PlotOptionsLine plotOptions = new PlotOptionsLine();
        plotOptions.setEnableMouseTracking(true);
        conf.setPlotOptions(plotOptions);

        conf.setLegend(new Legend(true));

        return chart;
    }
}