package de.whosfritz.railinsights.ui.pages;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.whosfritz.railinsights.ui.layout.MainView;

@Route(value = "verbindungsprognose", layout = MainView.class)
public class ConnectionPrognoseView extends VerticalLayout {
    public ConnectionPrognoseView() {
        add("Verbindungsprognose");
    }
}
