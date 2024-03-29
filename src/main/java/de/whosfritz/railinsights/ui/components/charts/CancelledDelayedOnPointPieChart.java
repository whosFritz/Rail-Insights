package de.whosfritz.railinsights.ui.components.charts;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CancelledDelayedOnPointPieChart extends Div {
    public CancelledDelayedOnPointPieChart(DataSeries dataSeries) {
        Chart chart = new Chart(ChartType.PIE);

        Configuration conf = chart.getConfiguration();
        conf.getChart().setStyledMode(true);

        conf.setTitle("Übersicht ausgefallener, verspäteter und pünktlicher Stopps");
        conf.setSubTitle("Statistik umfasst alle bisher erfassten Stopps");

        Tooltip tooltip = new Tooltip();
        tooltip.setValueDecimals(1);
        tooltip.setValueSuffix(" %");
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
