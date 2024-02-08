package de.whosfritz.railinsights.ui.components.charts;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CancelledDelayedOnPointPieChart extends Div {
    public CancelledDelayedOnPointPieChart(DataSeries dataSeries) {
        // tag::snippet[]
        Chart chart = new Chart(ChartType.PIE);

        Configuration conf = chart.getConfiguration();

        conf.setTitle("Übersicht ausgefallener, verspäteter und pünktlicher Stopps");
        conf.setSubTitle("Statistik umfasst alle bisher erfassten Stopps");

        Tooltip tooltip = new Tooltip();
        tooltip.setValueDecimals(1);
        conf.setTooltip(tooltip);

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setAllowPointSelect(true);
        plotOptions.setCursor(Cursor.POINTER);
        plotOptions.setShowInLegend(true);
        conf.setPlotOptions(plotOptions);
        chart.setVisibilityTogglingDisabled(true);

        conf.addSeries(dataSeries);

        add(new VerticalLayout(chart));
    }

}
