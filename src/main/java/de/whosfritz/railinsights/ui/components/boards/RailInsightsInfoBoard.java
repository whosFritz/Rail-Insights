package de.whosfritz.railinsights.ui.components.boards;

import com.vaadin.flow.component.Tag;

@Tag("rail-insights-info-board")
public class RailInsightsInfoBoard extends GeneralBoard {

    public RailInsightsInfoBoard() {
        addClassName("dashboard-railinsights");
        setWidth("100%");
        setHeight("100%");
    }
}
