package de.whosfritz.RailInsights.UI.Pages;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "impressum")
public class ImpressumPage extends VerticalLayout {

    public ImpressumPage() {
        add(new Text("Impressum"));
    }
}
