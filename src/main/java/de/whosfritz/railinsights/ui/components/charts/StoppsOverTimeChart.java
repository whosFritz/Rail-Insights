package de.whosfritz.railinsights.ui.components.charts;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.whosfritz.railinsights.ui.color_scheme.ColorScheme;

public class StoppsOverTimeChart extends Div {
    public StoppsOverTimeChart(DataSeries dataSeries) {
        Chart chart = new Chart(ChartType.AREA);

        Configuration configuration = chart.getConfiguration();
        configuration.getChart().setStyledMode(true);

        configuration.setTitle("Stopps - Zeitverlauf");
        configuration.setSubTitle("Zeitlicher Verlauf der erfassten Stopps");

        YAxis yAxis = configuration.getyAxis();
        yAxis.setTitle("Anzahl Stopps");

        XAxis xAxis = configuration.getxAxis();
        xAxis.setType(AxisType.DATETIME);
        xAxis.setTitle("Zeit");
        xAxis.addUnit(new TimeUnitMultiples(TimeUnit.DAY, 1));

        configuration.getTooltip().setValueSuffix(" Stopps");

        PlotOptionsSeries plotOptionsSeries = new PlotOptionsSeries();
        plotOptionsSeries.setColor(new SolidColor(ColorScheme.INFO.getColor()));
        configuration.setPlotOptions(plotOptionsSeries);

        configuration.addSeries(dataSeries);

        add(new VerticalLayout(chart));
    }

}
