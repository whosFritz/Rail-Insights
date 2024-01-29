package de.whosfritz.RailInsights.UI.Pages;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "datenschutzerklaerung")
public class DatenSchutzerklaerung extends VerticalLayout {

    public DatenSchutzerklaerung() {
        add(new Paragraph("DatenSchutzerklaerung"));
    }
}
