package de.whosfritz.RailInsights.UI.Components;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MockupPieChart extends Div {
    public MockupPieChart() {
        // tag::snippet[]
        Chart chart = new Chart(ChartType.PIE);

        Configuration conf = chart.getConfiguration();

        conf.setTitle("Daten - Statistik");

        Tooltip tooltip = new Tooltip();
        tooltip.setValueDecimals(1);
        conf.setTooltip(tooltip);

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setAllowPointSelect(true);
        plotOptions.setCursor(Cursor.POINTER);
        plotOptions.setShowInLegend(true);
        conf.setPlotOptions(plotOptions);

        DataSeries series = new DataSeries();
        DataSeriesItem chrome = new DataSeriesItem("Datensatz1", 61.41);
        chrome.setSliced(true);
        chrome.setSelected(true);
        series.add(chrome);
        series.add(new DataSeriesItem("Datensatz2", 11.84));
        series.add(new DataSeriesItem("Datensatz3", 10.85));
        series.add(new DataSeriesItem("Datensatz4", 4.67));
        series.add(new DataSeriesItem("Datensatz5", 4.18));
        series.add(new DataSeriesItem("Datensatz6", 7.05));
        conf.setSeries(series);
        chart.setVisibilityTogglingDisabled(true);
        // end::snippet[]

        add(new VerticalLayout(chart));
    }

}
