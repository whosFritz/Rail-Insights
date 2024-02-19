package de.whosfritz.railinsights.ui.pages;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.whosfritz.railinsights.data.services.csv_export_service.CSVExporter;
import de.whosfritz.railinsights.ui.layout.MainView;
import org.vaadin.firitin.components.DynamicFileDownloader;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Route(value = "csv-export", layout = MainView.class)

public class CsvExportView extends VerticalLayout {
    private final CSVExporter csvExporter;

    public CsvExportView(CSVExporter csvExporter) {
        this.csvExporter = csvExporter;

        FormLayout comboboxLayout = new FormLayout();

        Grid<Object> grid = new Grid<>();
        grid.setSizeFull();
        ComboBox<String> tableSelection = new ComboBox<>("Tabellen");
        tableSelection.setItems(
                "Adressen",
                "Geographische Koordinaten",
                "Linie",
                "Betreiber",
                "Produktlinie",
                "Regionalbereich",
                "Bemerkung",
                "Ril100Kennzeichnung",
                "Standort Station",
                "Station Management",
                "Station",
                "Standort Haltestelle",
                "Haltestelle",
                "Zentrale",
                "Fahrplanbüro",
                "Reisen"
        );

        DateTimePicker startDateTimePicker = new DateTimePicker("Start Date");

        DateTimePicker endDateTimePicker = new DateTimePicker("End Date");
        Button searchButton = new Button("Search");
        searchButton.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchButton.addClickListener(e -> {
        });

        DynamicFileDownloader dynamicFileDownloader = new DynamicFileDownloader(out -> {
            PrintWriter writer = new PrintWriter(out);
            Class<?> clazz = null;
            Set<String> excludedFields = csvExporter.getExcludedFields(tableSelection.getValue());
            for (Object x : csvExporter.getItems(tableSelection.getValue())) {
                if (clazz == null) {
                    clazz = x.getClass();
                    Field[] fields = clazz.getDeclaredFields();
                    String fieldNames = Arrays.stream(fields)
                            .map(Field::getName)
                            .filter(name -> !excludedFields.contains(name)) // Exclude fields
                            .collect(Collectors.joining(","));
                    writer.println(fieldNames);
                }
                String fieldValues = Arrays.stream(clazz.getDeclaredFields())
                        .filter(field -> !excludedFields.contains(field.getName())) // Exclude fields
                        .map(field -> {
                            try {
                                field.setAccessible(true);
                                return String.valueOf(field.get(x));
                            } catch (IllegalAccessException e) {
                                return ""; // Handle exception appropriately
                            }
                        })
                        .collect(Collectors.joining(","));
                writer.println(fieldValues);
            }
            writer.close();
        });
        dynamicFileDownloader.setText("Download CSV");
        dynamicFileDownloader.asButton();

        FormLayout datumFormLayout = new FormLayout();

        tableSelection.addValueChangeListener(event -> {
            String selectedTable = event.getValue();
            dynamicFileDownloader.setFileName(LocalDateTime.now() + "_" + selectedTable + ".csv");
            datumFormLayout.setVisible(event.getValue().equals("Reisen"));
        });

        comboboxLayout.add(tableSelection);

        datumFormLayout.add(startDateTimePicker, endDateTimePicker);


        add(comboboxLayout);
        add(datumFormLayout);
        add(new FormLayout(dynamicFileDownloader));
        add(searchButton);
        add(grid);

    }
}
