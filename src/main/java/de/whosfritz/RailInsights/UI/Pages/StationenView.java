package de.whosfritz.RailInsights.UI.Pages;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;
import de.whosfritz.RailInsights.UI.Layout.MainView;

@Route(value = "stations", layout = MainView.class)
public class StationenView extends VerticalLayout {
    public StationenView() {
        Select<String> select = new Select<>();
        select.setLabel("Datenübersicht");
        select.setValue("Datensatz1");
        select.setItems("Datensatz1", "Datensatz2", "Datensatz3", "Datensatz4, Datensatz5");

        add(select);
    }
}
