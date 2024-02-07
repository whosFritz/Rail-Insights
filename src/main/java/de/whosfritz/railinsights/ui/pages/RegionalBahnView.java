package de.whosfritz.railinsights.ui.pages;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.whosfritz.railinsights.ui.layout.MainView;

@Route(value = "regionalbahn", layout = MainView.class)
public class RegionalBahnView extends VerticalLayout {
    public RegionalBahnView() {
        add("RegionalBahnView");
    }
}
