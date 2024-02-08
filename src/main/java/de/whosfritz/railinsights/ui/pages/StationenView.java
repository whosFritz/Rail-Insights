package de.whosfritz.railinsights.ui.pages;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;
import de.whosfritz.railinsights.ui.components.charts.MockupTableChart;
import de.whosfritz.railinsights.ui.layout.MainView;

@Route(value = "stations", layout = MainView.class)
public class StationenView extends VerticalLayout {
    public StationenView() {
        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.setWidthFull();
        wrapper.setAlignItems(Alignment.BASELINE);

        Select<String> select = new Select<>();
        select.setLabel("Datenübersicht");
        select.setItems("Datensatz1", "Datensatz2", "Datensatz3", "Datensatz4", "Datensatz5");
        select.setValue("Datensatz1");

        wrapper.add(select, new Button(VaadinIcon.REFRESH.create()));
        add(wrapper);
        MockupTableChart mockupTableChart = new MockupTableChart();
        mockupTableChart.setWidthFull();
        mockupTableChart.setHeightFull();
        add(mockupTableChart);
    }
}
