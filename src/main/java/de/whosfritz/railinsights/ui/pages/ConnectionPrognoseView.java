package de.whosfritz.railinsights.ui.pages;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.olech2412.adapter.dbadapter.APIConfiguration;
import de.olech2412.adapter.dbadapter.DB_Adapter_v6;
import de.olech2412.adapter.dbadapter.exception.Error;
import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.journey.Journey;
import de.olech2412.adapter.dbadapter.request.parameters.Parameter;
import de.olech2412.adapter.dbadapter.request.parameters.RequestParametersNames;
import de.whosfritz.railinsights.data.dto.StopDto;
import de.whosfritz.railinsights.ui.components.Ticket;
import de.whosfritz.railinsights.ui.components.TicketComponent;
import de.whosfritz.railinsights.ui.factories.notification.NotificationFactory;
import de.whosfritz.railinsights.ui.factories.notification.NotificationTypes;
import de.whosfritz.railinsights.ui.layout.MainView;
import de.whosfritz.railinsights.ui.services.DataProviderService;
import de.whosfritz.railinsights.utils.TicketUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Route(value = "verbindungsprognose", layout = MainView.class)
public class ConnectionPrognoseView extends VerticalLayout {

    private TicketComponent ticketComponent;

    public ConnectionPrognoseView(DataProviderService dataProviderService) {

        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(Alignment.BASELINE);

        DateTimePicker dateTimePicker = new DateTimePicker();
        dateTimePicker.setLabel("Wann möchtest du fahren?");
        dateTimePicker.setValue(LocalDateTime.now());
        dateTimePicker.setLocale(new Locale("de", "DE"));

        ComboBox<StopDto> stopComboBox = new ComboBox<>();
        stopComboBox.setLabel("Von");
        stopComboBox.setItems(dataProviderService.getAllNationalStopsConvertedToDto());
        stopComboBox.setItemLabelGenerator(StopDto::getStopName);

        ComboBox<StopDto> stopComboBox2 = new ComboBox<>();
        stopComboBox2.setLabel("Nach");
        stopComboBox2.setItems(dataProviderService.getAllNationalStopsConvertedToDto());
        stopComboBox2.setItemLabelGenerator(StopDto::getStopName);

        Button searchButton = new Button("Suchen");
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchButton.setIcon(VaadinIcon.SEARCH.create());
        searchButton.addClickListener(valueChangeEvent ->
                {
                    try {
                        updateTicketComponent(stopComboBox.getValue().getStopId(), stopComboBox2.getValue().getStopId(), dateTimePicker.getValue());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        header.add(stopComboBox, stopComboBox2, dateTimePicker, searchButton);
        header.setWidth("100%");
        header.setHeight("20%");

        ticketComponent = new TicketComponent();
        ticketComponent.setVisible(true);

        add(header, ticketComponent);
    }

    public void updateTicketComponent(String stopIdFrom, String stopIdTo, LocalDateTime when) throws IOException {
        if (stopIdFrom == null || stopIdTo == null) return;

        DB_Adapter_v6 db_adapter_v6 = createDBAdapter();

        //parse when to ISO8601
        String formattedDateTime = when.format(DateTimeFormatter.ISO_DATE_TIME);

        Result<Journey[], Error> journeys = db_adapter_v6.getJourney(
                new Parameter.ParameterBuilder()
                        .add(RequestParametersNames.FROM, stopIdFrom)
                        .add(RequestParametersNames.TO, stopIdTo)
                        .add(RequestParametersNames.DEPARTURE, formattedDateTime)
                        .add(RequestParametersNames.RESULTS, 20)
                        .add(RequestParametersNames.REGIONAL, false)
                        .add(RequestParametersNames.REGIONAL_EXPRESS, false)
                        .add(RequestParametersNames.SUBURBAN, false)
                        .add(RequestParametersNames.BUS, false)
                        .add(RequestParametersNames.FERRY, false)
                        .add(RequestParametersNames.SUBWAY, false)
                        .add(RequestParametersNames.TRAM, false)
                        .add(RequestParametersNames.TAXI, false)
                        .add(RequestParametersNames.STOP_OVERS, true)
                        .add(RequestParametersNames.NATIONAL, true)
                        .add(RequestParametersNames.LANGUAGE, "de")
                        .add(RequestParametersNames.NATIONAL_EXPRESS, true)
                        .build());

        if (journeys.isSuccess()) {
            if (journeys.getData() == null) {
                Notification error = NotificationFactory.createNotification(NotificationTypes.CRITICAL, "Keine Verbindungen für die angegebenen Daten gefunden");
                error.open();

                return;
            }
            List<Ticket> tickets = TicketUtil.convertJourneysToTickets(Arrays.stream(journeys.getData()).toList());
            ticketComponent.updateTickets(tickets);
        } else {
            if (journeys.getError().getCode() == 404) {
                Notification error = NotificationFactory.createNotification(NotificationTypes.CRITICAL, "Keine Verbindungen für die angegebenen Daten gefunden");
                error.open();
            } else {
                Notification error = NotificationFactory.createNotification(NotificationTypes.CRITICAL, "Ein Fehler bei der Kommunikation zum HAFAS-Server ist aufgetreten. Bitte versuche es später erneut.");
                error.open();
            }
        }
    }

    private DB_Adapter_v6 createDBAdapter() {
        APIConfiguration apiConfiguration = new APIConfiguration();
        apiConfiguration.setBaseUrl("http://localhost:3000");

        return new DB_Adapter_v6(apiConfiguration);
    }

}
