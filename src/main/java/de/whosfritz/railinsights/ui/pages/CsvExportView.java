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
import de.olech2412.adapter.dbadapter.model.station.Station;
import de.olech2412.adapter.dbadapter.model.station.sub.*;
import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.olech2412.adapter.dbadapter.model.stop.sub.Line;
import de.olech2412.adapter.dbadapter.model.stop.sub.StopLocation;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.olech2412.adapter.dbadapter.model.trip.sub.Remark;
import de.whosfritz.railinsights.data.services.csv_export_service.CSVExporter;
import de.whosfritz.railinsights.ui.layout.MainView;
import org.vaadin.firitin.components.DynamicFileDownloader;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Route(value = "csv-export", layout = MainView.class)

public class CsvExportView extends VerticalLayout {
    private final CSVExporter csvExporter;

    public CsvExportView(CSVExporter csvExporter) {
        this.csvExporter = csvExporter;

        FormLayout formLayout = new FormLayout();
        Button downloadButton = new Button("Download as CSV");

        Grid<Object> grid = new Grid<>();
        grid.setSizeFull();
        ComboBox<String> tableSelection;


        tableSelection = new ComboBox<>("Table");
        tableSelection.setItems(
                "Address",
                "GeographicCoordinates",
                "Line",
                "Operator",
                "ProductLine",
                "Regionalbereich",
                "Remark",
                "Ril100Identifier",
                "StationLocation",
                "StationManagement",
                "Station",
                "StopLocation",
                "Stop",
                "Szentrale",
                "TimeTableOffice",
                "Trip"
        );

        DateTimePicker startDateTimePicker = new DateTimePicker("Start Date");

        DateTimePicker endDateTimePicker = new DateTimePicker("End Date");
        Button searchButton = new Button("Search");
        searchButton.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchButton.addClickListener(e -> {
            System.out.println("Searching");
            grid.setItems(csvExporter.getItems(tableSelection.getValue()));

        });

        ArrayList<Object> selectedItems = new ArrayList<>();
        selectedItems.add(1);
        selectedItems.add(2);
        selectedItems.add(3);
        selectedItems.add(4);
        selectedItems.add(5);
        selectedItems.add(6);


        // Build CSV content using opencsv (replace with your preferred library)
        add();
        formLayout.add(tableSelection, startDateTimePicker, endDateTimePicker, downloadButton, searchButton, new Button(
                new DynamicFileDownloader("Download as CSV...", LocalDateTime.now() + "_" + tableSelection.getValue() + ".csv", out -> {

                    PrintWriter writer = new PrintWriter(out);
                    // writer println the attrbiutes of the selected class
                    Class<?> clazz = getClassForName(tableSelection.getValue());
                    Field[] fields = clazz.getDeclaredFields();
                    String fieldNames = Arrays.stream(fields)
                            .map(Field::getName)
                            .collect(Collectors.joining(","));
                    writer.println(fieldNames);

                    csvExporter.getItems(tableSelection.getValue()).forEach(x -> {
                        String fieldValues = Arrays.stream(x.getClass().getDeclaredFields())
                                .map(field -> {
                                    field.setAccessible(true);
                                    try {
                                        return field.get(x).toString();
                                    } catch (IllegalAccessException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                                .collect(Collectors.joining(","));
                        writer.println(fieldValues);
                    });
                    writer.close();

                })));
        downloadButton.addClickListener(e -> startDownload());
        add(formLayout);
        add(grid);
    }

    private void startDownload() {
        try {


            // Get selected items (customize based on your data provider)
            // Iterable<?> selectedItems = csvExporter.getItems(tableSelection.getValue());
            // create list of iterable

        } catch (Exception e) {
            // Handle exceptions gracefully, e.g., display error message to user
        }
    }

    private Class<?> getClassForName(String className) {
        switch (className) {
            case "Address":
                return Address.class;
            case "GeographicCoordinates":
                return GeographicCoordinates.class;
            case "Operator":
                return Operator.class;
            case "Line":
                return Line.class;
            case "ProductLine":
                return ProductLine.class;
            case "Regionalbereich":
                return Regionalbereich.class;
            case "Remark":
                return Remark.class;
            case "Ril100Identifier":
                return Ril100Identifier.class;
            case "StationLocation":
                return StationLocation.class;
            case "StationManagement":
                return StationManagement.class;
            case "Station":
                return Station.class;
            case "StopLocation":
                return StopLocation.class;
            case "Stop":
                return Stop.class;
            case "Szentrale":
                return Szentrale.class;
            case "TimeTableOffice":
                return TimeTableOffice.class;
            case "Trip":
                return Trip.class;

            default:
                throw new IllegalArgumentException("Unknown class: " + className);
        }
    }
}
