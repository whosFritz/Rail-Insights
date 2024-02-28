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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.olech2412.adapter.dbadapter.model.stop.sub.Line;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.whosfritz.railinsights.calculation.UniversalCalculator;
import de.whosfritz.railinsights.data.LoadFactor;
import de.whosfritz.railinsights.data.dto.TripCounts;
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
import jakarta.transaction.Transactional;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static de.whosfritz.railinsights.ui.components.boards.StationViewDashboard.createHighlight;

@Route(value = "trainmetrics", layout = MainView.class)
@Transactional
public class TrainStatsView extends VerticalLayout {
    private final UniversalCalculator universalCalculator = new UniversalCalculator();

    private final TripService tripService;

    private final LineService lineService;


    public TrainStatsView(TripService tripService, LineService lineService) {
        this.tripService = tripService;
        this.lineService = lineService;

        DataProviderService dataProviderService = VaadinService.getCurrent().getInstantiator().getOrCreate(DataProviderService.class);

        VerticalLayout tripStatsLayout = new VerticalLayout();

        HorizontalLayout searchLayout = new HorizontalLayout();
        searchLayout.addClassNames(LumoUtility.Margin.MEDIUM);
        searchLayout.setAlignItems(Alignment.BASELINE);

        Paragraph infoParagraph = new Paragraph("Hier kannst du dir die Statistiken zu Zügen anzeigen lassen.");
        Paragraph infoCalcParagraph = new Paragraph("Wähle aus der Liste einen Fernverkehrszug aus und gib den Zeitraum an, für den du die Statistiken sehen möchtest.");


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

        ComboBox<Line> fernVerkehrLinesCombobox = new ComboBox<>();
        fernVerkehrLinesCombobox.setItems(lineService.getLinesNationalOrNationalExpress().getData());
        fernVerkehrLinesCombobox.setItemLabelGenerator(Line::getName);
        fernVerkehrLinesCombobox.setLabel("Fernverkehrszug");
        fernVerkehrLinesCombobox.addClassNames(LumoUtility.Margin.Top.MEDIUM);
        fernVerkehrLinesCombobox.setPrefixComponent(LineAwesomeIcon.SUBWAY_SOLID.create());


        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setLabel("Startdatum");
        startDatePicker.setValue(LocalDate.now());
        startDatePicker.addClassNames(LumoUtility.Margin.Top.MEDIUM);
        startDatePicker.setPrefixComponent(LineAwesomeIcon.CALENDAR_ALT_SOLID.create());
        startDatePicker.setLocale(new Locale("de", "DE"));

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setLabel("Enddatum");
        endDatePicker.setValue(startDatePicker.getValue().plusDays(1));
        endDatePicker.addClassNames(LumoUtility.Margin.Top.MEDIUM);
        endDatePicker.setPrefixComponent(LineAwesomeIcon.CALENDAR_ALT_SOLID.create());
        endDatePicker.setLocale(new Locale("de", "DE"));

        fernVerkehrLinesCombobox.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                tripStatsLayout.getElement().removeAllChildren();

                LocalDateTime from = startDatePicker.getValue().atStartOfDay();
                LocalDateTime to = endDatePicker.getValue().atStartOfDay().plusDays(1);
                Notification notification = NotificationFactory.createwNotification(NotificationTypes.SUCCESS, "Du hast den Zug " + e.getValue().getName() + " ausgewählt.");
                notification.open();
                List<Trip> tripsCorrespondingToLine = tripService.findAllByPlannedWhenIsAfterAndPlannedWhenIsBeforeAndLine_FahrtNr(from, to, e.getValue().getFahrtNr()).getData();
                // first sort the trips by delay, delay could be null
                tripsCorrespondingToLine.sort(Comparator.comparing(Trip::getDelay, Comparator.nullsLast(Comparator.naturalOrder())));
                // get the middle element
                int size = tripsCorrespondingToLine.size();
                double medianDelay;
                if (size % 2 == 0) {
                    medianDelay = tripsCorrespondingToLine.get(size / 2 - 1).getDelay();
                } else {
                    medianDelay = tripsCorrespondingToLine.get(size / 2).getDelay();
                }


                // put trips into a list where delay bigger than 6 and not null
                List<Trip> tripsWithDelay = tripsCorrespondingToLine.stream()
                        .filter(trip -> Optional.ofNullable(trip.getDelay()).orElse(0) >= 6)
                        .toList();

                TripStatistics tripStatistics = universalCalculator.calculateTripStatistics(tripsCorrespondingToLine);

                int tripCount = tripsCorrespondingToLine.size();

                TripCounts tripCounts = universalCalculator.countTrips(tripsCorrespondingToLine, from, to);

                DataSeries dailyTripCountSeries = universalCalculator.buildDailyTripCountSeries(tripCounts.getDailyTripCounts());


                DataSeries dailyTripLongDistanceCountSeries = universalCalculator.buildDailyTripCountSeries(tripCounts.getDailyTripLongDistanceCounts());
                DataSeries dailyTripRegionalCountSeries = universalCalculator.buildDailyTripCountSeries(tripCounts.getDailyTripRegionalCounts());
                DataSeries hourlyTripCountSeries = universalCalculator.buildHourlyTripCountSeries(tripCounts.getHourlyTripCounts());
                DataSeries hourlyTripLongDistanceCountSeries = universalCalculator.buildHourlyTripCountSeries(tripCounts.getHourlyTripLongDistanceCounts());
                DataSeries hourlyTripRegionalCountSeries = universalCalculator.buildHourlyTripCountSeries(tripCounts.getHourlyTripRegionalCounts());


                DataSeries nationalRegionalSeries = universalCalculator.calculatePercentageTripRegioVsFernverkehr(tripsCorrespondingToLine);

                List<Trip> topDelayedTrips = universalCalculator.calculateTopDelayedTripsOrderedByDelay(tripsCorrespondingToLine, 10);


                double globalOnTimePercentage = (dataProviderService.getStopsPercentageOnTime() != null) ? dataProviderService.getStopsPercentageOnTime() : 0.0;
                double globalDelayedPercentage = (dataProviderService.getStopsPercentageDelayed() != null) ? dataProviderService.getStopsPercentageDelayed() : 0.0;
                double globalCancelledPercentage = (dataProviderService.getStopsPercentageCancelled() != null) ? dataProviderService.getStopsPercentageCancelled() : 0.0;


                double percentageOnTime = tripStatistics.getPercentageOnTime();
                double percentageDelayed = tripStatistics.getPercentageDelayed();
                double percentageCancelled = tripStatistics.getPercentageCancelled();

                double onTimeGlobalComparison = percentageOnTime - globalOnTimePercentage;
                double delayedGlobalComparison = percentageDelayed - globalDelayedPercentage;
                double cancelledGlobalComparison = percentageCancelled - globalCancelledPercentage;


                onTimeGlobalComparison = PercentageUtil.convertToTwoDecimalPlaces(onTimeGlobalComparison);
                delayedGlobalComparison = PercentageUtil.convertToTwoDecimalPlaces(delayedGlobalComparison);
                cancelledGlobalComparison = PercentageUtil.convertToTwoDecimalPlaces(cancelledGlobalComparison);

                // get each abfahrtsdirections could be null then use the province
                Set<String> abfahrtsDirections = tripsCorrespondingToLine.stream()
                        .map(trip -> Optional.ofNullable(trip.getDirection()).orElse(trip.getProvenance()))
                        .collect(Collectors.toSet());


                // Todo: Durchschnittliche Verspätung des Zuges im Zeitraum als Badge in minuten
                Board board = new Board();
                // name des zuges
                board.addRow(createHighlight("Zug: ", e.getValue().getName()));
                // wohin fährt dieser zug überhaupt?
                board.addRow(
                        createHighlight("Abfahrtsrichtungen", abfahrtsDirections),
                        createHighlight("Anzahl Fahrten", String.valueOf(tripCount)),
                        createHighlight("Median der Verspätung", UniversalCalculator.minutesToHoursAndMinutesAndSeconds(medianDelay))
                );
                Row abfahrtenStats = new Row();
                abfahrtenStats.add(createHighlight("Pünktliche Stopps", percentageOnTime + " %", onTimeGlobalComparison, "Pünktlichkeit im globalen Vergleich", true));
                abfahrtenStats.add(createHighlight("Verspätete Stopps", percentageDelayed + " %", delayedGlobalComparison, "Verspätungen im globalen Vergleich", false));
                abfahrtenStats.add(createHighlight("Ausgefallene Stopps", percentageCancelled + " %", cancelledGlobalComparison, "Ausfälle im globalen Vergleich", false));
                abfahrtenStats.addClassNames(LumoUtility.Margin.Bottom.XLARGE);
                board.addRow(abfahrtenStats);
                // Todo: Durchschnittliche Verspätung des Zuges pro Tag als Grafik
                DataSeries dailyDelaySeries = UniversalCalculator.buildDailyDelaySeries(tripsCorrespondingToLine);
                board.addRow(createDailyDelayChart(dailyDelaySeries));
                // add line chart for daily LoadFactor, use the enum, whatever is the most on that day is the days LoadFactor
                DataSeries dailyLoadFactorSeries = UniversalCalculator.buildDailyLoadFactorSeries(tripsCorrespondingToLine);
                board.addRow(createDailyLoadFactorChart(dailyLoadFactorSeries));

                tripStatsLayout.add(board);
                tripStatsLayout.setVisible(true);
            }
        });


        // Todo: Anzahl der Fahrten im Zeitraum
        // Todo: Remarks / Bemerkungen zu den Fahrten (idk yet)
        // Todo: Wohin fährt dieser zug überhaupt?

        searchLayout.add(infoButton, fernVerkehrLinesCombobox, startDatePicker, endDatePicker);

        add(searchLayout, tripStatsLayout);
        tripStatsLayout.setVisible(false);

        setSizeFull();
    }

    private Component createDailyDelayChart(DataSeries dailyDelaySeries) {
        Chart chart = new Chart(ChartType.LINE);

        chart.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.XLARGE);

        Configuration conf = chart.getConfiguration();
        conf.setTitle("Verspätung");
        conf.setSubTitle("Durchschnittliche Verspätung pro Tag");

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

        return chart;

    }

    public Component createDailyLoadFactorChart(DataSeries dailyLoadFactorSeries) {
        Chart chart = new Chart(ChartType.LINE);

        chart.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.XLARGE);

        Configuration conf = chart.getConfiguration();
        conf.setTitle("Auslastung");
        conf.setSubTitle("Median der Auslastung pro Tag");

        conf.addSeries(dailyLoadFactorSeries);
        conf.getChart().setStyledMode(true);


        Tooltip tooltip = chart.getConfiguration().getTooltip();
        tooltip.setShared(true);
        tooltip.setDateTimeLabelFormats(DateTimeLabelFormatsUtil.getCleanedDateTimeLabelFormat());

        YAxis yAxis = chart.getConfiguration().getyAxis();
        yAxis.setTitle(new AxisTitle());

        conf.setLegend(new Legend(false));

        // show corresponding enum alias on yaxis
        yAxis.setCategories(Arrays.stream(LoadFactor.values()).map(LoadFactor::getAlias).toArray(String[]::new));

        XAxis xAxis = chart.getConfiguration().getxAxis();
        xAxis.setType(AxisType.DATETIME);
        xAxis.setTitle(new AxisTitle());


        return chart;
    }

}