package de.whosfritz.railinsights.ui.pages;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.whosfritz.railinsights.ui.layout.MainView;

@Route(value = "ic", layout = MainView.class)
public class ICView extends VerticalLayout {
    public ICView() {
        add("ICView");
    }
}
