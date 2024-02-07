package de.whosfritz.railinsights.ui.pages;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.whosfritz.railinsights.ui.layout.MainView;

@Route(value = "ice", layout = MainView.class)
public class ICEView extends VerticalLayout {
    public ICEView() {
        add("ICEView");
    }
}
