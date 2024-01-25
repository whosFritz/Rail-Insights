package de.whosfritz.RailInsights.UI.Pages;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.whosfritz.RailInsights.UI.Layout.MainView;

@Route(value = "ice", layout = MainView.class)
public class ICEView extends VerticalLayout {
    public ICEView() {
        add("ICEView");
    }
}
