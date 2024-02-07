package de.whosfritz.RailInsights.UI.Pages;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.whosfritz.RailInsights.UI.Layout.MainView;

@Route(value = "trips", layout = MainView.class)

public class Trips extends VerticalLayout {
    public Trips() {
        add("Trips");
    }
}
