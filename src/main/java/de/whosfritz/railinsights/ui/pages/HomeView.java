package de.whosfritz.railinsights.ui.pages;

import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.whosfritz.railinsights.ui.color_scheme.ColorScheme;
import de.whosfritz.railinsights.ui.components.InfoPanel;
import de.whosfritz.railinsights.ui.components.boards.RailInsightsInfoBoard;
import de.whosfritz.railinsights.ui.components.boards.board_components.Indicator;
import de.whosfritz.railinsights.ui.components.charts.CancelledDelayedOnPointPieChart;
import de.whosfritz.railinsights.ui.components.charts.StoppsOverTimeChart;
import de.whosfritz.railinsights.ui.layout.MainView;
import de.whosfritz.railinsights.ui.services.DataProviderService;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "/", layout = MainView.class)
@PageTitle("RailInsights")
public class HomeView extends VerticalLayout {

    @Autowired
    private DataProviderService dataProviderService;

    public HomeView(DataProviderService dataProviderService) {
        this.dataProviderService = dataProviderService;

        add(new H1("Willkommen bei RailInsights!"));
        add(new InfoPanel());
        add(initRailInsightsBoard());

    }

    private StoppsOverTimeChart createStoppsOverTimeChart() {
        DataSeries dataSeries = dataProviderService.getStoppsOverTimeDataSeries();
        dataSeries.setName("Stopps");

        return new StoppsOverTimeChart(dataSeries);
    }

    private RailInsightsInfoBoard initRailInsightsBoard() {
        RailInsightsInfoBoard railInsightsInfoBoard = new RailInsightsInfoBoard();
        railInsightsInfoBoard.addRow(
                new Indicator("Erfasste Stopps insgesamt: ", String.valueOf(dataProviderService.getTotalTrips()), "Erfasste Stopps heute: " + dataProviderService.getTotalTripsToday(), 1),
                new Indicator("Erfasste Haltepunkte insgesamt: ", dataProviderService.getTotalStops() + " Regional- und Fernverkehrshaltepunkte"),
                new Indicator("Haltepunkte für die Stopps erfasst werden: ", dataProviderService.getNationalStops() + " Fernverkehrshaltepunkte")
        );

        railInsightsInfoBoard.addRow(
                createStoppsOverTimeChart(),
                createCancelledDelayedOnPointPieChart()
        );

        return railInsightsInfoBoard;
    }

    private CancelledDelayedOnPointPieChart createCancelledDelayedOnPointPieChart() {
        DataSeries dataSeries = new DataSeries();
        DataSeriesItem cancelled = new DataSeriesItem("Ausgefallen (Ersatzfahrten nicht berücksichtigt)", dataProviderService.getStopsPercentageCancelled());
        cancelled.setColor(new SolidColor(ColorScheme.ERROR.getColor()));
        DataSeriesItem delayed = new DataSeriesItem("Verspätet", dataProviderService.getStopsPercentageDelayed());
        delayed.setColor(new SolidColor(ColorScheme.WARNING_SOFT.getColor()));
        DataSeriesItem onTime = new DataSeriesItem("Pünktlich", dataProviderService.getStopsPercentageOnTime());
        onTime.setColor(new SolidColor(ColorScheme.SUCCESS.getColor()));

        dataSeries.add(cancelled);
        dataSeries.add(delayed);
        dataSeries.add(onTime);

        return new CancelledDelayedOnPointPieChart(dataSeries);
    }
}
