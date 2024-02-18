package de.whosfritz.railinsights.ui.components.charts;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.whosfritz.railinsights.utils.DateTimeLabelFormatsUtil;

public class StoppsOverTimeChart extends Div {
    public StoppsOverTimeChart(DataSeries dataSeries) {
        Chart chart = new Chart(ChartType.AREA);

        Configuration configuration = chart.getConfiguration();
        configuration.setTitle("Stopps - Zeitverlauf");
        configuration.setSubTitle("Zeitlicher Verlauf der erfassten Stopps");

        Tooltip tooltip = configuration.getTooltip();
        tooltip.setDateTimeLabelFormats(DateTimeLabelFormatsUtil.getCleanedDateTimeLabelFormat());

        YAxis yAxis = configuration.getyAxis();
        yAxis.setTitle("Anzahl Stopps");

        XAxis xAxis = configuration.getxAxis();
        xAxis.setType(AxisType.DATETIME);
        xAxis.setTitle("Zeit");

        configuration.getTooltip().setValueSuffix(" Stopps");

        configuration.addSeries(dataSeries);

        add(new VerticalLayout(chart));
    }

}
