package de.whosfritz.railinsights.ui.pages;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.whosfritz.railinsights.data.services.csv_export_service.CSVExporter;
import de.whosfritz.railinsights.ui.components.dialogs.GeneralRailInsightsDialog;
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

    public CsvExportView(CSVExporter csvExporter) {

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
                "Fahrten"
        );

        DateTimePicker startDateTimePicker = new DateTimePicker("Start Date");

        DateTimePicker endDateTimePicker = new DateTimePicker("End Date");
        Button searchButton = new Button("Search");
        searchButton.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchButton.addClickListener(e -> {
        });

        DynamicFileDownloader dynamicFileDownloader = new DynamicFileDownloader(out -> {
            if (tableSelection.getValue() == null || tableSelection.getValue().isEmpty()) {
                return;
            }
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
        dynamicFileDownloader.setDisableOnClick(true);
        dynamicFileDownloader.addDownloadFinishedListener(e -> new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            dynamicFileDownloader.getUI().ifPresent(ui -> ui.access(() -> dynamicFileDownloader.setEnabled(true)));
        }).start());

        UI.getCurrent().setPollInterval(500);

        dynamicFileDownloader.setEnabled(false);
        dynamicFileDownloader.asButton();
        dynamicFileDownloader.getButton().setIcon(new Icon(VaadinIcon.DOWNLOAD));

        FormLayout datumFormLayout = new FormLayout();
        datumFormLayout.setVisible(false);

        tableSelection.addValueChangeListener(event -> {
            String selectedTable = event.getValue();
            dynamicFileDownloader.setFileName(LocalDateTime.now() + "_" + selectedTable + ".csv");
            datumFormLayout.setVisible(event.getValue() != null && event.getValue().equals("Fahrten"));
            dynamicFileDownloader.setEnabled(event.getValue() != null);
        });

        comboboxLayout.add(tableSelection);

        datumFormLayout.add(startDateTimePicker, endDateTimePicker);

        Paragraph infoParagraph = new Paragraph("Diese Seite ermöglicht es, Daten aus der Datenbank als CSV-Datei herunterzuladen." +
                " Wähle dazu eine Tabelle aus und klicke auf den Download-Button. ");

        Button infoButton = new Button("Informationen");
        infoButton.setIcon(new Icon(VaadinIcon.INFO_CIRCLE));
        infoButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        infoButton.setTooltipText("Klicke hier für mehr Informationen zu dieser Seite.");
        infoButton.setAriaLabel("Informationen");
        infoButton.addClickListener(e -> {
            HorizontalLayout infoLayout = new HorizontalLayout(new VerticalLayout(infoParagraph));
            infoLayout.setWidth(100f, Unit.PERCENTAGE);
            infoLayout.setMaxWidth(100f, Unit.PERCENTAGE);

            GeneralRailInsightsDialog dialog = new GeneralRailInsightsDialog();
            dialog.setHeaderTitle("Informationen zur Seite");
            dialog.add(infoLayout);
            dialog.add();
            dialog.open();
        });

        add(infoButton);
        add(comboboxLayout);
        add(datumFormLayout);
        add(new FormLayout(dynamicFileDownloader));
        add(searchButton);
        add(grid);

    }
}
