package de.whosfritz.railinsights.ui.components.boards;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.whosfritz.railinsights.ui.components.boards.board_components.Highlight;
import de.whosfritz.railinsights.utils.DateTimeLabelFormatsUtil;
import de.whosfritz.railinsights.utils.PercentageUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class StationViewDashboard extends VerticalLayout {

    private final DataSeries seriesRegional;

    private final DataSeries seriesLongDistance;

    private final DataSeries seriesTotal;

    private final DataSeries nationalRegioSeries;

    public StationViewDashboard(int stopCount, double stopsPercentageOnTime, double globalPercentageOnTime, double stopsPercentageDelayed, double globalPercentageDelayed,
                                double stopsPercentageCancelled, double globalPercentageCancelled, DataSeries seriesRegional, DataSeries seriesLongDistance,
                                DataSeries seriesTotal, DataSeries nationalRegioSeries, List<Trip> topDelayedTrips) {
        addClassName("dashboard-railinsights");

        this.seriesRegional = seriesRegional;
        this.seriesLongDistance = seriesLongDistance;
        this.seriesTotal = seriesTotal;
        this.nationalRegioSeries = nationalRegioSeries;

        double percentage = stopsPercentageOnTime - globalPercentageOnTime;
        double percentageDelayed = stopsPercentageDelayed - globalPercentageDelayed;
        double percentageCancelled = stopsPercentageCancelled - globalPercentageCancelled;

        percentage = PercentageUtil.convertToTwoDecimalPlaces(percentage);
        percentageDelayed = PercentageUtil.convertToTwoDecimalPlaces(percentageDelayed);
        percentageCancelled = PercentageUtil.convertToTwoDecimalPlaces(percentageCancelled);

        seriesRegional.setName("Regionalverkehr");
        seriesLongDistance.setName("Fernverkehr");
        seriesTotal.setName("Gesamt");

        DateTimePicker when = new DateTimePicker();
        when.setLocale(new Locale("de", "DE"));
        when.setLabel("Von");
        when.setValue(LocalDateTime.now().minusDays(1));

        DateTimePicker whenBefore = new DateTimePicker();
        whenBefore.setLocale(new Locale("de", "DE"));
        whenBefore.setLabel("Bis");
        whenBefore.setValue(LocalDateTime.now());

        Board board = new Board();
        board.addRow(createHighlight("Abfahrten/Ankünfte", String.valueOf(stopCount)),
                createHighlight("Pünktliche Stopps", stopsPercentageOnTime + " %", percentage, "Pünktlichkeit im deutschlandweiten Vergleich", true),
                createHighlight("Verspätete Stopps", stopsPercentageDelayed + " %", percentageDelayed, "Verspätungen im deutschlandweiten Vergleich", false),
                createHighlight("Ausgefallene Stopps", stopsPercentageCancelled + " %", percentageCancelled, "Ausfälle im deutschlandweiten Vergleich", false)
        );
        board.addRow(createStopsOverTimeChart());
        board.addRow(createRegionalLongDistanceChart());
        board.addRow(createDelayedTripsGrid(topDelayedTrips));
        add(board);
    }

    private Component createDelayedTripsGrid(List<Trip> topDelayedTrips) {
        Grid<Trip> grid = new Grid<>();
        grid.setItems(topDelayedTrips);
        grid.setTooltipGenerator(trip -> {
            StringBuilder tooltip = new StringBuilder("Fahrtnummer: " + trip.getTripId() + "\n");
            tooltip.append("Linie: ").append(trip.getLine().getName()).append("\n");
            if (trip.getDirection() != null) {
                tooltip.append("Richtung: ").append(trip.getDirection()).append("\n");
            }

            if (trip.getRemarks() != null && !trip.getRemarks().isEmpty()) {
                tooltip.append("Meldungen: ");
                for (int i = 0; i < trip.getRemarks().size(); i++) {
                    tooltip.append(trip.getRemarks().get(i).getText());
                    if (i < trip.getRemarks().size() - 1) {
                        tooltip.append(", ");
                    }
                }
            }
            return tooltip.toString();
        });
        grid.addColumn(trip -> trip.getLine().getName()).setHeader("Linie");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER);
        grid.addColumn(trip -> {
            if (trip.getWhen() != null) {
                return trip.getWhen().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.uuuu"));
            } else {
                return trip.getPlannedWhen().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.uuuu"));
            }
        }).setHeader("Wann").setSortable(true);
        grid.addColumn(trip -> trip.getDelay() / 60).setHeader("Verspätung (min)").setSortable(true);

        VerticalLayout layout = new VerticalLayout();
        layout.add(new H3("Top Verspätungen"));
        layout.add(grid);

        return layout;
    }

    private Component createRegionalLongDistanceChart() {
        HorizontalLayout header = createHeader("Aufteilung Regional- und Fernverkehr", "Aufgeteilt nach verschiedenen Zugkategorien");

        // Chart
        Chart chart = new Chart(ChartType.PIE);
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Regional- und Fernverkehr");
        conf.getTooltip().setValueSuffix(" %");
        conf.setExporting(true);
        conf.getChart().setStyledMode(true);

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setAllowPointSelect(true);
        plotOptions.setCursor(Cursor.POINTER);
        plotOptions.setShowInLegend(true);
        conf.setPlotOptions(plotOptions);

        conf.addSeries(nationalRegioSeries);

        // Add it all together
        VerticalLayout viewEvents = new VerticalLayout(header, chart);
        viewEvents.addClassName(LumoUtility.Padding.LARGE);
        viewEvents.setPadding(false);
        viewEvents.setSpacing(false);
        viewEvents.getElement().getThemeList().add("spacing-l");
        return viewEvents;
    }

    private Component createHighlight(String title, String value, Double percentage, String explanation, boolean inverted) {
        return new Highlight(title, value, percentage, explanation, inverted, "%");
    }

    private Component createHighlight(String title, String value) {
        return new Highlight(title, value);
    }

    private Component createStopsOverTimeChart() {
        HorizontalLayout header = createHeader("Stopps - Zeitverlauf", "Aufgeteilt nach Regional, Fern und Gesamtverkehr");

        ChartType chartType = ChartType.COLUMN;

        // Chart
        Chart chart = new Chart(chartType);
        Configuration conf = chart.getConfiguration();
        conf.getTooltip().setValueSuffix(" Stopps");
        conf.setExporting(true);
        conf.getChart().setStyledMode(true);

        Tooltip tooltip = chart.getConfiguration().getTooltip();
        tooltip.setDateTimeLabelFormats(DateTimeLabelFormatsUtil.getCleanedDateTimeLabelFormat());

        XAxis xAxis = new XAxis();
        xAxis.setType(AxisType.DATETIME);
        conf.addxAxis(xAxis);

        conf.getyAxis().setTitle("Anzahl der Stopps");

        PlotOptionsAreaspline plotOptions = new PlotOptionsAreaspline();
        plotOptions.setPointPlacement(PointPlacement.ON);
        plotOptions.setMarker(new Marker(false));
        plotOptions.setConnectNulls(false);
        plotOptions.setStacking(Stacking.NONE);
        conf.addPlotOptions(plotOptions);

        conf.addSeries(seriesLongDistance);
        conf.addSeries(seriesRegional);
        conf.addSeries(seriesTotal);

        // Add it all together
        VerticalLayout viewEvents = new VerticalLayout(header, chart);
        viewEvents.addClassName(LumoUtility.Padding.LARGE);
        viewEvents.setPadding(false);
        viewEvents.setSpacing(false);
        viewEvents.getElement().getThemeList().add("spacing-l");
        return viewEvents;
    }

    private HorizontalLayout createHeader(String title, String subtitle) {
        H2 h2 = new H2(title);
        h2.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.NONE);

        Span span = new Span(subtitle);
        span.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.XSMALL);

        VerticalLayout column = new VerticalLayout(h2, span);
        column.setPadding(false);
        column.setSpacing(false);

        HorizontalLayout header = new HorizontalLayout(column);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setSpacing(false);
        header.setWidthFull();
        return header;
    }

}