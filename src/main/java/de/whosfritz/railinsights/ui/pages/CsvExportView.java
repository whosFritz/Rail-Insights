package de.whosfritz.railinsights.ui.pages;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.whosfritz.railinsights.ui.layout.MainView;

@Route(value = "csv-export", layout = MainView.class)

public class CsvExportView extends VerticalLayout {
    public CsvExportView() {
        add("CSV-Export");
    }
}
