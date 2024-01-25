package de.whosfritz.RailInsights.UI.Pages;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.whosfritz.RailInsights.UI.Layout.MainView;

@Route(value = "ic", layout = MainView.class)
public class ICView extends VerticalLayout {
    public ICView() {
        add("ICView");
    }
}
