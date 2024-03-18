package de.whosfritz.railinsights.ui.components.charts;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.whosfritz.railinsights.utils.DateTimeLabelFormatsUtil;

public class DailyDelayChart extends Div {

    public DailyDelayChart(DataSeries dailyDelaySeries) {
        Chart chart = new Chart(ChartType.AREA);

        chart.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.XLARGE);

        Configuration conf = chart.getConfiguration();
        conf.setTitle("Verspätung");
        conf.setSubTitle("Durchschnittliche Verspätung pro Tag in Minuten");

        conf.setExporting(true);

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
        plotOptions.setStacking(Stacking.NORMAL);
        conf.setPlotOptions(plotOptions);

        add(chart);
    }
}
