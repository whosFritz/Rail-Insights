package de.whosfritz.railinsights.ui.pages;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.whosfritz.railinsights.data.services.csv_export_service.CSVExporterService;
import de.whosfritz.railinsights.ui.components.dialogs.GeneralRailInsightsDialog;
import de.whosfritz.railinsights.ui.factories.notification.NotificationFactory;
import de.whosfritz.railinsights.ui.factories.notification.NotificationTypes;
import de.whosfritz.railinsights.ui.layout.MainView;
import org.vaadin.firitin.components.DynamicFileDownloader;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Route(value = "csv-export", layout = MainView.class)

public class CsvExportView extends VerticalLayout {

    public CsvExportView(CSVExporterService csvExporterService) {

        HorizontalLayout wrapper = new HorizontalLayout();

        ComboBox<String> tableToDownloadComboBox = new ComboBox<>("Tabellen");
        tableToDownloadComboBox.setItems(
                "Adressen",
                "Geographische Koordinaten",
                "Linien",
                "Betreiber",
                "Produktlinien",
                "Regionalbereiche",
                "Bemerkungen zu Fahrten",
                "Ril100Kennzeichnungen",
                "Standorte von Stationen",
                "Station Managements",
                "Stationen",
                "Standorte von Haltestellen",
                "Haltestellen",
                "Zentrale",
                "Fahrplan Büros",
                "Fahrten (Stops)"
        );

        DatePicker startDateTimePicker = new DatePicker("Start Date");
        startDateTimePicker.setValue(LocalDate.now());
        startDateTimePicker.setVisible(false);

        Dialog test = new Dialog("CSV-Export");
        add(test);

        DynamicFileDownloader dynamicFileDownloader = new DynamicFileDownloader(out -> {
            LocalDate startValue = startDateTimePicker.getValue();
            String comboBoxSelectedValue = tableToDownloadComboBox.getValue();
            if (comboBoxSelectedValue == null || comboBoxSelectedValue.isEmpty() || comboBoxSelectedValue.isBlank() || startValue == null) {
                return;
            }
            PrintWriter writer = new PrintWriter(out);
            createCsv(csvExporterService, comboBoxSelectedValue, startValue, writer);
        });
        dynamicFileDownloader.setDisableOnClick(true);
        dynamicFileDownloader.addDownloadFinishedListener(e -> {
                    new Thread(() -> {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        dynamicFileDownloader.getUI().ifPresent(ui -> ui.access(() -> dynamicFileDownloader.setEnabled(true)));
                    }
                    ).start();
                    UI.getCurrent().access(() -> {
                        NotificationFactory.createNotification(NotificationTypes.SUCCESS, "Download finished").open();
                    });
                }
        );
        dynamicFileDownloader.addDownloadFailedListener(e -> {
            NotificationFactory.createNotification(NotificationTypes.ERROR, "Download failed").open();
        });

        UI.getCurrent().setPollInterval(500);

        dynamicFileDownloader.setEnabled(false);
        dynamicFileDownloader.asButton();
        dynamicFileDownloader.getButton().setIcon(new Icon(VaadinIcon.DOWNLOAD));
        dynamicFileDownloader.getButton().addClickListener(e -> {
            NotificationFactory.createNotification(NotificationTypes.INFO, "Download started").open();
        });

        tableToDownloadComboBox.addValueChangeListener(event -> {
            String selectedTable = event.getValue();

            dynamicFileDownloader.setFileName(LocalDateTime.now() + "_" + selectedTable + ".csv");
            dynamicFileDownloader.setEnabled(event.getValue() != null);

            startDateTimePicker.setVisible(selectedTable != null && !selectedTable.isBlank() && !selectedTable.isEmpty() && event.getValue().equals("Fahrten (Stops)"));
        });


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

        Button previewButton = new Button(LineAwesomeIcon.WHEELCHAIR_SOLID.create());
        wrapper.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        tableToDownloadComboBox.setWidth(30f, Unit.PERCENTAGE);
        wrapper.setWidthFull();
        wrapper.add(infoButton, tableToDownloadComboBox, startDateTimePicker, previewButton, dynamicFileDownloader);
        add(wrapper);
    }

    private void createCsv(CSVExporterService csvExporterService, String comboBoxSelection, LocalDate startDateTimePickerValue, PrintWriter writer) {
        csvExporterService.writeCsv(comboBoxSelection, startDateTimePickerValue, writer);
    }
}
