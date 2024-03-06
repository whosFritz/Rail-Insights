package de.whosfritz.railinsights.ui.components.dialogs;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.olech2412.adapter.dbadapter.model.trip.sub.Remark;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TripHistoryDialog extends GeneralRailInsightsDialog {

    String dateTimePattern = "HH:mm dd.MM.uuuu";

    public TripHistoryDialog(List<Trip> trip) {
        if (trip == null) {
            return;
        }
        if (trip.get(0).getLine() != null) {
            setHeaderTitle("Fahrtverlauf für Fahrt " + trip.get(0).getLine().getName());
        } else {
            setHeaderTitle("Fahrtverlauf für Fahrt " + trip.get(0).getTripId());
        }

        Grid<Trip> grid = new Grid<>();
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER);
        grid.setColumnReorderingAllowed(true);

        grid.setTooltipGenerator(o -> {
            String tooltip = "Fahrtnummer: " + o.getTripId() + "\n";
            tooltip += "Linie: " + o.getLine().getName() + "\n";
            if (o.getRemarks() != null && !o.getRemarks().isEmpty()) {
                tooltip += "Meldungen: " + o.getRemarks().size() + "\n";
            }
            return tooltip;
        });

        grid.addColumn(o -> o.getStop().getName()).setHeader("Halt").setAutoWidth(true);
        grid.addColumn(o -> o.getLine().getName()).setHeader("Linie").setAutoWidth(true);
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

        // ensure that the status has the width that is needed
        grid.addComponentColumn(trips -> {
            Span span = new Span();
            try {
                if (Boolean.TRUE.equals(trips.getCancelled())) {
                    span.setText("Ausgefallen");
                    span.getElement().getThemeList().add("badge primary");
                    return span;
                }
            } catch (NullPointerException ignored) {
            }
            if (trips.getDelay() == null || trips.getDelay() == 0 || trips.getDelay() <= 360) {
                span.setText("Pünktlich");
                span.getElement().getThemeList().add("badge success primary");
            } else if(trips.getDelay() > 360 && trips.getDelay() <= 600) {
                span.setText("Leichte Verspätung");
                span.getElement().getThemeList().add("badge warn primary");
            } else {
                span.setText("Hohe Verspätung");
                span.getElement().getThemeList().add("badge primary");
            }
            return span;
        }).setHeader("Status").setFlexGrow(0).setWidth("12%");

        grid.setItems(trip);


        add(grid);

    }

}
