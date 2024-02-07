package de.whosfritz.railinsights.ui.components;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MockupLineChart extends Div {
    public MockupLineChart() {
        Chart chart = new Chart(ChartType.LINE);

        Configuration configuration = chart.getConfiguration();

        configuration.setTitle("Statistik Titel");
        configuration.setSubTitle("Quelle: Datenbank");
        // configuration.getChart().setBackgroundColor(new SolidColor(255, 255, 255, 0));

        YAxis yAxis = configuration.getyAxis();
        yAxis.setTitle("Count");

        Legend legend = configuration.getLegend();
        legend.setLayout(LayoutDirection.VERTICAL);
        legend.setVerticalAlign(VerticalAlign.MIDDLE);
        legend.setAlign(HorizontalAlign.RIGHT);

        PlotOptionsSeries plotOptionsSeries = new PlotOptionsSeries();
        plotOptionsSeries.setPointStart(2010);
        configuration.setPlotOptions(plotOptionsSeries);

        configuration.addSeries(new ListSeries("Datensatz1", 43934, 52503, 57177, 69658, 97031, 119931, 137133, 154175));
        configuration.addSeries(new ListSeries("Datenpunk2", 24916, 24064, 29742, 29851, 32490, 30282, 38121, 40434));
        configuration.addSeries(new ListSeries("Datenpunk3", 11744, 17722, 16005, 19771, 20185, 24377, 32147, 39387));
        configuration.addSeries(new ListSeries("Datenpunk4", 11744, 17722, 7988, 12169, 15112, 22452, 34400, 34227));
        configuration.addSeries(new ListSeries("Datenpunk5", 12908, 5948, 8105, 11248, 8989, 11816, 18274, 18111));
        // end::snippet[]

        add(new VerticalLayout(chart));
    }

}
