package de.whosfritz.RailMetrics.UI.Pages;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.whosfritz.RailMetrics.UI.Layout.MainView;

@Route(value = "trips", layout = MainView.class)

public class Trips extends VerticalLayout {
    public Trips() {
        add("Trips");
    }
}
