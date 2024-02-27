package de.whosfritz.railinsights.ui.pages;


import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.olech2412.adapter.dbadapter.model.stop.sub.Line;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.whosfritz.railinsights.data.services.LineService;
import de.whosfritz.railinsights.data.services.trip_services.TripService;
import de.whosfritz.railinsights.ui.components.dialogs.GeneralRailInsightsDialog;
import de.whosfritz.railinsights.ui.factories.notification.NotificationFactory;
import de.whosfritz.railinsights.ui.factories.notification.NotificationTypes;
import de.whosfritz.railinsights.ui.layout.MainView;
import jakarta.transaction.Transactional;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Route(value = "trainmetrics", layout = MainView.class)
@Transactional
public class TrainStatsView extends VerticalLayout {

    private final TripService tripService;

    private final LineService lineService;


    public TrainStatsView(TripService tripService, LineService lineService) {
        this.tripService = tripService;
        this.lineService = lineService;


        HorizontalLayout searchLayout = new HorizontalLayout();
        searchLayout.addClassNames(LumoUtility.Margin.MEDIUM);
        searchLayout.setAlignItems(Alignment.BASELINE);

        Paragraph infoParagraph = new Paragraph("Hier kannst du dir die Statistiken zu Zügen anzeigen lassen.");
        Paragraph infoCalcParagraph = new Paragraph("Wähle aus der Liste einen Fernverkehrszug aus und gib den Zeitraum an, für den du die Statistiken sehen möchtest.");


        Button infoButton = new Button("Informationen");
        infoButton.setIcon(new Icon(VaadinIcon.INFO_CIRCLE));
        infoButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        infoButton.setAriaLabel("Informationen");
        infoButton.addClickListener(e -> {
            HorizontalLayout infoLayout = new HorizontalLayout(new VerticalLayout(infoParagraph, infoCalcParagraph));
            infoLayout.setWidth(100f, Unit.PERCENTAGE);
            infoLayout.setMaxWidth(100f, Unit.PERCENTAGE);

            GeneralRailInsightsDialog dialog = new GeneralRailInsightsDialog();
            dialog.setHeaderTitle("Informationen zur Seite");
            dialog.add(infoLayout);
            dialog.open();
        });

        ComboBox<Line> fernVerkehrLinesCombobox = new ComboBox<>();
        fernVerkehrLinesCombobox.setItems(lineService.getLinesNationalOrNationalExpress().getData());
        fernVerkehrLinesCombobox.setItemLabelGenerator(Line::getName);
        fernVerkehrLinesCombobox.setLabel("Fernverkehrszug");
        fernVerkehrLinesCombobox.addClassNames(LumoUtility.Margin.Top.MEDIUM);
        fernVerkehrLinesCombobox.setPrefixComponent(LineAwesomeIcon.SUBWAY_SOLID.create());


        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setLabel("Startdatum");
        startDatePicker.setValue(LocalDate.now());
        startDatePicker.addClassNames(LumoUtility.Margin.Top.MEDIUM);
        startDatePicker.setPrefixComponent(LineAwesomeIcon.CALENDAR_ALT_SOLID.create());
        startDatePicker.setLocale(new Locale("de", "DE"));

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setLabel("Enddatum");
        endDatePicker.setValue(startDatePicker.getValue().plusDays(1));
        endDatePicker.addClassNames(LumoUtility.Margin.Top.MEDIUM);
        endDatePicker.setPrefixComponent(LineAwesomeIcon.CALENDAR_ALT_SOLID.create());
        endDatePicker.setLocale(new Locale("de", "DE"));

        fernVerkehrLinesCombobox.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                Notification notification = NotificationFactory.createwNotification(NotificationTypes.SUCCESS, "Du hast den Zug " + e.getValue().getName() + " ausgewählt.");
                notification.open();
                System.out.println("Start: " + startDatePicker.getValue().atStartOfDay());
                System.out.println("End: " + endDatePicker.getValue().atStartOfDay());
                System.out.println("FahrtNr: " + e.getValue().getFahrtNr());
                List<Trip> tripsCorrespondingToLine = tripService.findAllByPlannedWhenIsAfterAndPlannedWhenIsBeforeAndLine_FahrtNr(startDatePicker.getValue().atStartOfDay(), endDatePicker.getValue().atStartOfDay().plusDays(1), e.getValue().getFahrtNr()).getData();
                System.out.println("Trips: " + tripsCorrespondingToLine);
            }
        });

        searchLayout.add(infoButton, fernVerkehrLinesCombobox, startDatePicker, endDatePicker);

        add(searchLayout);

        setSizeFull();
    }
}