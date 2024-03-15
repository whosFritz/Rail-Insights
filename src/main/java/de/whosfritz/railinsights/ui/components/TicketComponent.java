package de.whosfritz.railinsights.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import de.olech2412.adapter.dbadapter.model.journey.Journey;
import de.whosfritz.railinsights.data.Ticket;
import de.whosfritz.railinsights.ui.components.dialogs.ConnectionPrognoseDialog;
import lombok.Getter;
import lombok.Setter;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.ArrayList;
import java.util.List;

public class TicketComponent extends VerticalLayout {

    @Getter
    private final Grid<Ticket> grid = new Grid<>(Ticket.class);

    // Create a list of sample tickets
    private final List<Ticket> tickets = new ArrayList<>();

    @Getter
    @Setter
    private List<Journey> journeys = new ArrayList<>();

    public TicketComponent() {
        // Set the data provider for the grid
        grid.setDataProvider(new ListDataProvider<>(tickets));

        // Configure the columns of the grid
        grid.setColumns("departure", "arrival", "duration", "transfers", "price");
        grid.getColumnByKey("departure").setHeader("Abfahrt am Start");
        grid.getColumnByKey("arrival").setHeader("Ankunft am Ziel");
        grid.getColumnByKey("duration").setHeader("Dauer der Reise");
        grid.getColumnByKey("transfers").setHeader("Umstiege");
        grid.getColumnByKey("price").setHeader("Preis");
        grid.addComponentColumn(ticket -> {
            Button toggleDetails = new Button(VaadinIcon.ARROW_DOWN.create());
            toggleDetails.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
            toggleDetails.addClickListener(event -> grid.setDetailsVisible(ticket, !grid.isDetailsVisible(ticket)));
            return toggleDetails;
        }).setHeader("Details anzeigen");

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);

        // Add a button column for continuing the purchase
        grid.addComponentColumn(ticket -> {
            Button button = new Button("Zur Prognose");
            button.setIcon(LineAwesomeIcon.FLASK_SOLID.create());
            button.addClickListener(event -> {
                ConnectionPrognoseDialog dialog = new ConnectionPrognoseDialog(ticket.getJourney());
                dialog.open();
            });
            return button;
        }).setHeader("Zur Prognose");

        // Add a details row for showing the train details
        grid.setItemDetailsRenderer(new ComponentRenderer<>(ticket -> {
            // Create a div to display the train details
            Div div = new Div();
            div.addClassName("train-details");

            // Loop through the trains and create a div for each one
            for (String train : ticket.getTrainDetails()) {
                // Create a div for the train
                Div trainDiv = new Div();
                trainDiv.addClassName("train");
                trainDiv.getStyle().set("padding", "5px 0");
                trainDiv.getStyle().set("display", "flex");
                trainDiv.getStyle().set("margin", "5px 0");
                trainDiv.getStyle().set("border-radius", "5px");

                // Set the background color according to the train type
                if (train.contains("ICE")) {
                    trainDiv.getStyle().set("background-color", "black");
                } else {
                    trainDiv.getStyle().set("background-color", "gray");
                }


                // Set the text content to the train number
                trainDiv.setText(train);

                // Add the train div to the details div
                div.add(trainDiv);
            }

            // Return the details div
            return div;
        }));

        // Add the grid to the layout
        add(grid);
    }

    /**
     * Update the tickets displayed in the grid
     *
     * @param newTickets The new list of tickets
     */
    public void updateTickets(List<Ticket> newTickets) {
        // Clear the existing tickets
        tickets.clear();

        // Add the new tickets
        tickets.addAll(newTickets);

        // Refresh the grid
        grid.getDataProvider().refreshAll();
    }

}