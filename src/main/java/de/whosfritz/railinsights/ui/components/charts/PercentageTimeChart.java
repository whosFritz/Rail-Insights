package de.whosfritz.railinsights.ui.components.charts;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.whosfritz.railinsights.utils.DateTimeLabelFormatsUtil;

public class PercentageTimeChart extends Div {

    public PercentageTimeChart(DataSeries onTimeSeries, DataSeries delayedSeries, DataSeries cancelledSeries) {
        Chart chart = new Chart(ChartType.AREA);

        Configuration configuration = chart.getConfiguration();
        configuration.getChart().setStyledMode(true);

        configuration.setTitle("Pünktlichkeit- / Ausfall- und Verspätungsverlauf");
        configuration.setSubTitle("Zeitlicher Verlauf der Pünktlichkeit, Ausfällen und Verspätungen in Prozent");

        Tooltip tooltip = configuration.getTooltip();
        tooltip.setDateTimeLabelFormats(DateTimeLabelFormatsUtil.getCleanedDateTimeLabelFormat());

        YAxis yAxis = configuration.getyAxis();
        yAxis.setTitle("Prozent");

        XAxis xAxis = configuration.getxAxis();
        xAxis.setType(AxisType.DATETIME);
        xAxis.setStartOfWeek(1);
        xAxis.setTitle("Zeit");

        PlotOptionsAreaspline plotOptions = new PlotOptionsAreaspline();
        plotOptions.setPointPlacement(PointPlacement.ON);
        plotOptions.setMarker(new Marker(false));
        plotOptions.setConnectNulls(true);
        plotOptions.setStacking(Stacking.NORMAL);
        configuration.addPlotOptions(plotOptions);

        configuration.getTooltip().setValueSuffix(" %");

        configuration.addSeries(onTimeSeries);
        configuration.addSeries(delayedSeries);
        configuration.addSeries(cancelledSeries);

        add(new VerticalLayout(chart));
    }
}
