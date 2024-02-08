package de.whosfritz.railinsights.ui.pages;

import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.whosfritz.railinsights.ui.color_scheme.ColorScheme;
import de.whosfritz.railinsights.ui.components.InfoPanel;
import de.whosfritz.railinsights.ui.components.boards.RailInsightsInfoBoard;
import de.whosfritz.railinsights.ui.components.boards.board_components.Indicator;
import de.whosfritz.railinsights.ui.components.charts.CancelledDelayedOnPointPieChart;
import de.whosfritz.railinsights.ui.components.charts.StoppsOverTimeChart;
import de.whosfritz.railinsights.ui.layout.MainView;
import de.whosfritz.railinsights.ui.services.DataProviderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = "/", layout = MainView.class)
@PageTitle("RailInsights")
public class GeneralView extends VerticalLayout {

    @Autowired
    private DataProviderService dataProviderService;

    public GeneralView(DataProviderService dataProviderService) {
        this.dataProviderService = dataProviderService;

        add(new H1("Willkommen bei RailInsights!"));
        add(new InfoPanel());
        add(initRailInsightsBoard());

    }

    private static StoppsOverTimeChart createStoppsOverTimeChart(Map<LocalDate, Integer> dailyTripCounts) {
        DataSeries dataSeries = new DataSeries();
        dataSeries.setName("Stopps");
        for (Map.Entry<LocalDate, Integer> entry : dailyTripCounts.entrySet()) {
            DataSeriesItem dataSeriesItem = new DataSeriesItem(entry.getKey().atStartOfDay(ZoneId.systemDefault()).toInstant(), entry.getValue());
            dataSeriesItem.setColor(new SolidColor(ColorScheme.INFO_LIGHT.getColor()));
            dataSeries.add(dataSeriesItem);
        }

        return new StoppsOverTimeChart(dataSeries);
    }

    private RailInsightsInfoBoard initRailInsightsBoard() {
        RailInsightsInfoBoard railInsightsInfoBoard = new RailInsightsInfoBoard();
        railInsightsInfoBoard.addRow(
                new Indicator("Erfasste Stopps insgesamt: ", String.valueOf(dataProviderService.getAllTrips().size()), "Erfasste Stopps heute: " + dataProviderService.getAllTripsForLocalDate(LocalDate.now()).size(), 1),
                new Indicator("Erfasste Haltepunkte insgesamt: ", dataProviderService.getAllStops().size() + " Regional- und Fernverkehrshaltepunkte"),
                new Indicator("Haltepunkte für die Stopps erfasst werden: ", dataProviderService.getAllStops().stream().filter(stop -> stop.getProducts().isNational()).count() + " Fernverkehrshaltepunkte")
        );

        Map<LocalDate, Integer> dailyTripCounts = getLocalDateIntegerMap();

        createStoppsOverTimeChart(dailyTripCounts);

        railInsightsInfoBoard.addRow(
                createStoppsOverTimeChart(dailyTripCounts),
                createCancelledDelayedOnPointPieChart()
        );

        return railInsightsInfoBoard;
    }

    private CancelledDelayedOnPointPieChart createCancelledDelayedOnPointPieChart() {
        List<Trip> allTrips = dataProviderService.getAllTrips();

        float percentageCancelled = (float) allTrips.stream().filter(trip -> {
            if (trip.getCancelled() != null) {
                return trip.getCancelled();
            }
            return false;
        }).count() / allTrips.size();
        float percentageDelayed = (float) allTrips.stream().filter(trip -> {
            if (trip.getCancelled() == null || !trip.getCancelled()) {
                if (trip.getDelay() != null) {
                    return trip.getDelay() >= 300;
                }
            }
            return false;
        }).count() / allTrips.size();
        float percentageOnTime = 1 - percentageCancelled - percentageDelayed;

        DataSeries dataSeries = new DataSeries();
        DataSeriesItem cancelled = new DataSeriesItem("Ausgefallen (Ersatzfahrten nicht berücksichtigt)", percentageCancelled);
        cancelled.setColor(new SolidColor(ColorScheme.ERROR.getColor()));
        DataSeriesItem delayed = new DataSeriesItem("Verspätet", percentageDelayed);
        delayed.setColor(new SolidColor(ColorScheme.WARNING_SOFT.getColor()));
        DataSeriesItem onTime = new DataSeriesItem("Pünktlich", percentageOnTime);
        onTime.setColor(new SolidColor(ColorScheme.SUCCESS.getColor()));

        dataSeries.add(cancelled);
        dataSeries.add(delayed);
        dataSeries.add(onTime);

        return new CancelledDelayedOnPointPieChart(dataSeries);
    }

    private Map<LocalDate, Integer> getLocalDateIntegerMap() {
        List<Trip> allTrips = dataProviderService.getAllTrips();

        // HashMap zur Speicherung der Summe der Trips pro Tag
        Map<LocalDate, Integer> dailyTripCounts = new HashMap<>();

        // Iteration durch alle Trips
        for (Trip trip : allTrips) {
            LocalDate tripDate;
            // Wenn trip.getWhen() vorhanden ist, verwenden wir dieses Datum, ansonsten trip.getPlannedWhen() oder trip.getCreatedAt()
            if (trip.getWhen() != null) {
                tripDate = trip.getWhen().toLocalDate();
            } else if (trip.getPlannedWhen() != null) {
                tripDate = trip.getPlannedWhen().toLocalDate();
            } else {
                tripDate = trip.getCreatedAt().toLocalDate();
            }

            // Aktuellen Wert für den Tag holen und um 1 erhöhen
            dailyTripCounts.put(tripDate, dailyTripCounts.getOrDefault(tripDate, 0) + 1);
        }
        return dailyTripCounts;
    }
}
