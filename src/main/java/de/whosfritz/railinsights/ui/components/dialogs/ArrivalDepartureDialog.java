package de.whosfritz.railinsights.ui.components.dialogs;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.olech2412.adapter.dbadapter.model.trip.sub.Remark;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ArrivalDepartureDialog extends GeneralRailInsightsDialog {

    Grid<Trip> grid;

    String dateTimePattern = "HH:mm dd.MM.uuuu";

    public ArrivalDepartureDialog(String stopName, TextField searchField, DateTimePicker whenAfter, DateTimePicker whenBefore) {
        VerticalLayout content = new VerticalLayout();
        HorizontalLayout searchLayout = new HorizontalLayout(searchField, whenAfter, whenBefore);
        searchLayout.setWidth("100%");
        searchLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        content.add(searchLayout);
        content.setWidth("100%");
        content.setHeight("100%");

        grid = new Grid<>();
        grid.setWidth("100%");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER);
        grid.setColumnReorderingAllowed(true);

        grid.setTooltipGenerator(trip -> {
            String tooltip = "Fahrtnummer: " + trip.getTripId() + "\n";
            tooltip += "Linie: " + trip.getLine().getName() + "\n";
            if (trip.getRemarks() != null && !trip.getRemarks().isEmpty()) {
                tooltip += "Meldungen: " + trip.getRemarks().size() + "\n";
            }
            return tooltip;
        });

        grid.addColumn(Trip::getTripId).setHeader("Fahrtnummer");
        grid.addColumn(o -> o.getLine().getName()).setHeader("Linie").setAutoWidth(true);
        grid.addColumn(Trip::getDirection).setHeader("Richtung").setAutoWidth(true);
        grid.addColumn(o -> {
            try {
                return o.getWhen().format(DateTimeFormatter.ofPattern(dateTimePattern));
            } catch (NullPointerException nullPointerException) {
                return o.getPlannedWhen().format(DateTimeFormatter.ofPattern(dateTimePattern));
            }
        }).setHeader("Wann").setAutoWidth(true);
        grid.addColumn(o -> {
            try {
                return o.getPlannedWhen().format(DateTimeFormatter.ofPattern(dateTimePattern));
            } catch (NullPointerException nullPointerException) {
                return o.getWhen().format(DateTimeFormatter.ofPattern(dateTimePattern));
            }
        }).setHeader("Geplant").setAutoWidth(true);

        grid.addColumn(o -> {
            try {
                return o.getDelay() / 60;
            } catch (NullPointerException e) {
                return 0;
            }
        }).setHeader("Verspätung (in Minuten)").setAutoWidth(true);

        grid.addColumn(o -> {
            try {
                return o.getPlatform();
            } catch (NullPointerException nullPointerException) {
                return o.getPlannedPlatform();
            }
        }).setHeader("Gleis").setAutoWidth(true);

        grid.addColumn(o -> {
            try {
                return o.getPlannedPlatform();
            } catch (NullPointerException nullPointerException) {
                return o.getPlatform();
            }
        }).setHeader("Geplantes Gleis").setAutoWidth(true);


        grid.addComponentColumn(o -> {
            try {
                return Boolean.TRUE.equals(o.getCancelled()) ? new Icon(VaadinIcon.CHECK) : new Icon(VaadinIcon.CLOSE);
            } catch (NullPointerException nullPointerException) {
                return new Icon(VaadinIcon.CHECK);
            }
        }).setHeader("Ausgefallen").setAutoWidth(true);

        grid.addComponentColumn(o -> {
            if (o.getRemarks() != null && !o.getRemarks().isEmpty()) {
                Icon icon = new Icon(VaadinIcon.INFO_CIRCLE);
                icon.getElement().getStyle().set("cursor", "pointer");
                icon.addClickListener(e -> {
                    List<Remark> remarks = o.getRemarks();
                    RemarksDialog dialog = new RemarksDialog(remarks, o);
                    dialog.open();
                });
                return icon;
            } else {
                return new Text("");
            }
        }).setHeader("Meldungen").setAutoWidth(true);

        setHeaderTitle("Ankünfte und Abfahrten für " + stopName);
        content.add(grid);


        add(content);
    }

    public void setItems(DataProvider dataProvider) {
        grid.setItems(dataProvider);
    }

}
