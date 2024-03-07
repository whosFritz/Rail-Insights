package de.whosfritz.railinsights.ui.pages;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.router.Route;
import de.olech2412.adapter.dbadapter.model.stop.sub.Line;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.whosfritz.railinsights.data.services.LineService;
import de.whosfritz.railinsights.data.services.trip_services.TripService;
import de.whosfritz.railinsights.ui.components.dialogs.ButtonFactory;
import de.whosfritz.railinsights.ui.factories.notification.NotificationFactory;
import de.whosfritz.railinsights.ui.factories.notification.NotificationTypes;
import de.whosfritz.railinsights.ui.layout.MainView;

import java.time.LocalDate;
import java.util.List;

@Route(value = "verspätungen", layout = MainView.class)
public class DelayedView extends VerticalLayout {

    private final LineService lineService;
    private final TripService tripService;

    public DelayedView(LineService lineService, TripService tripService) {
        this.lineService = lineService;
        this.tripService = tripService;
        HorizontalLayout controls = new HorizontalLayout();
        controls.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        Button createStatsButton = new Button("Create Stats");

        Paragraph p1 = new Paragraph("This page shows the delays of the trains.");
        Paragraph p2 = new Paragraph("You can select a time period and a start and end date to create the stats.");
        Button infoButton = ButtonFactory.createInfoButton("Informationen", p1, p2);

        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        radioButtonGroup.setLabel("Select a time period");
        radioButtonGroup.setItems("Nahverkehr", "Fernverkehr");
        radioButtonGroup.setRequired(true);
        radioButtonGroup.setRequiredIndicatorVisible(true);
        radioButtonGroup.setValue("Nahverkehr");

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setLabel("Startdatum");
        startDatePicker.setRequired(true);
        startDatePicker.setRequiredIndicatorVisible(true);
        startDatePicker.setValue(LocalDate.now().minusMonths(1));

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setLabel("Enddatum");
        endDatePicker.setRequired(true);
        endDatePicker.setRequiredIndicatorVisible(true);
        endDatePicker.setValue(LocalDate.now());

        createStatsButton.addClickListener(event -> {
            if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
                NotificationFactory.createNotification(NotificationTypes.ERROR, "Bitte ein Start- und Enddatum auswählen.").open();
                return;
            }
            if (startDatePicker.getValue().isAfter(endDatePicker.getValue())) {
                NotificationFactory.createNotification(NotificationTypes.ERROR, "Das Startdatum darf nicht nach dem Enddatum liegen.").open();
                return;
            }
            if (radioButtonGroup.getValue() == null) {
                NotificationFactory.createNotification(NotificationTypes.ERROR, "Bitte einen Zugtyp auswählen.").open();
                return;
            }
            createStats(startDatePicker.getValue(), endDatePicker.getValue(), radioButtonGroup.getValue());
        });

        controls.add(infoButton, startDatePicker, endDatePicker, radioButtonGroup, createStatsButton);

        VerticalLayout stats = new VerticalLayout();
        add(controls, stats);

    }

    private void createStats(LocalDate startDate, LocalDate endDate, String trafficType) {
        List<Line> lines = null;
        List<Trip> tripsCorrespondingToLines = null;
        if (trafficType.equals("Nahverkehr")) {
            lines = lineService.getLinesByProducts(List.of("regional", "suburban", "regionalExpress")).getData();

        } else {
            lines = lineService.getLinesByProducts(List.of("national", "nationalexpress")).getData();
            tripsCorrespondingToLines = tripService.findAllByPlannedWhenIsAfterAndPlannedWhenIsBeforeAndLinesAndDelayIsGreaterThanOrEqualTo(startDate.atStartOfDay(), endDate.atStartOfDay(), lines, 360).getData();
        }

        System.out.println("Before breakpoint");
    }
}
