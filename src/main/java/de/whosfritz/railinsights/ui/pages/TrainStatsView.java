package de.whosfritz.railinsights.ui.pages;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.olech2412.adapter.dbadapter.model.stop.sub.Line;
import de.whosfritz.railinsights.data.services.train_services.TrainStatsService;
import de.whosfritz.railinsights.ui.layout.MainView;

import java.util.List;

@Route(value = "trainmetrics", layout = MainView.class)
public class TrainStatsView extends VerticalLayout {

    private final TrainStatsService trainStatsService;

    private ComboBox<String> cb1;
    private ComboBox<String> cb2;
    private ComboBox<String> cb3;
    private ComboBox<Line> cb4;

    public TrainStatsView(TrainStatsService trainStatsService) {
        this.trainStatsService = trainStatsService;


        setupLayout();
    }

    private void setupLayout() {
        cb1 = new ComboBox<>("Kategorie auswählen");
        cb1.setItems("Ausfälle", "Verspätung", "Auslastung", "Aktuelle Nachrichten");
        cb1.addValueChangeListener(event -> {
            handleCb1Selection(event.getValue());
        });

        cb2 = new ComboBox<>("n");
        cb2.setVisible(false); // Initially hidden, will be made visible based on cb1's selection
        cb2.addValueChangeListener(event -> {
            handleCb2Selection(event.getValue());
        });

        cb3 = new ComboBox<>("");
        cb3.setVisible(false); // Initially hidden, will be made visible based on cb2's selection
        cb3.addValueChangeListener(event -> {
            handleCb3Selection(event.getValue());
        });

        cb4 = new ComboBox<>("");
        cb4.setItemLabelGenerator(Line::getProductName);
        cb4.setVisible(false); // Initially hidden, will be made visible based on cb3's selection


        add(cb1, cb2, cb3, cb4);
    }


    private void handleCb1Selection(String value) {
        if (value.equals("Ausfälle")) {
            cb2.setLabel("Öffentlicher Verkehrstype auswählen");
            cb2.setItems("Nahverkehr", "Fernverkehr", "Alle");
            cb2.setVisible(true);
        }
        if (value.equals("Verspätung")) {
            cb2.setLabel("Öffentlicher Verkehrstype auswählen");
            cb2.setItems("Nahverkehr", "Fernverkehr", "Alle");
            cb2.setVisible(true);
        }
        if (value.equals("Auslastung")) {
            cb2.setLabel("Öffentlicher Verkehrstype auswählen");
            cb2.setItems("Nahverkehr", "Fernverkehr", "Alle");
            cb2.setVisible(true);
        }
        if (value.equals("Aktuelle Nachrichten")) {
            cb2.setLabel("Öffentlicher Verkehrstype auswählen");
            cb2.setItems("Nahverkehr", "Fernverkehr", "Alle");
            cb2.setVisible(true);
        }
    }

    private void handleCb2Selection(String value) {

        /*
        regional
        suburban
        nationalExpress
        national
        regionalExpress
         */
        cb3.setLabel("Zug auswählen");
        if (value.equals("Nahverkehr")) {
            cb3.setItems("suburban", "regional", "regionalExpress");
            cb3.setVisible(true);
        }
        if (value.equals("Fernverkehr")) {
            cb3.setItems("national", "nationalExpress");
            cb3.setVisible(true);
        }
        if (value.equals("Alle")) {
            cb3.setItems("Suche Starten");
            cb3.setVisible(true);
        }
    }

    private void handleCb3Selection(String product) {
        List<Line> lineListe = trainStatsService.getListOfTrainsByProductName(product).get();
        cb4.setItems(lineListe);
        cb4.setVisible(true);
    }
}
