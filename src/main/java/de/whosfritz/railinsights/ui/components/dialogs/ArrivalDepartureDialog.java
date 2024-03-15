package de.whosfritz.railinsights.ui.components.dialogs;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.server.VaadinService;
import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.olech2412.adapter.dbadapter.model.trip.sub.Remark;
import de.whosfritz.railinsights.data.services.trip_services.TripService;
import de.whosfritz.railinsights.exception.JPAError;
import de.whosfritz.railinsights.ui.factories.notification.NotificationFactory;
import de.whosfritz.railinsights.ui.factories.notification.NotificationTypes;
import de.whosfritz.railinsights.utils.TripUtil;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ArrivalDepartureDialog extends GeneralRailInsightsDialog {

    Grid<Trip> grid;

    String dateTimePattern = "HH:mm dd.MM.uuuu";

    public ArrivalDepartureDialog(String stopName, TextField searchField, DateTimePicker whenAfter, DateTimePicker whenBefore) {
        VerticalLayout content = new VerticalLayout();
        Paragraph infoParagraph = new Paragraph("Hier findest du alle Ankünfte bzw. Abfahrten für den eingestellten " +
                "Zeitraum. Durch rechtsklick (auf mobilen Geräten durch langes drücken) kannst du den Fahrtverlauf des Zuges aufrufen.");
        HorizontalLayout searchLayout = new HorizontalLayout(searchField, whenAfter, whenBefore);
        searchLayout.setWidth("100%");
        searchLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        content.add(infoParagraph, searchLayout);
        content.setWidth("100%");
        content.setHeight("100%");

        whenAfter.setLocale(new Locale("de", "DE"));
        whenBefore.setLocale(new Locale("de", "DE"));

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

        grid.addColumn(Trip::getTripId).setHeader("Fahrt ID");
        grid.addColumn(o -> o.getLine().getName()).setHeader("Linie").setAutoWidth(true);
        grid.addColumn(Trip::getDirection).setHeader("Richtung").setAutoWidth(true);

        grid.addColumn(o -> {
            try {
                return o.getPlannedWhen().format(DateTimeFormatter.ofPattern(dateTimePattern));
            } catch (NullPointerException nullPointerException) {
                return o.getWhen().format(DateTimeFormatter.ofPattern(dateTimePattern));
            }
        }).setHeader("Geplant").setAutoWidth(true);

        grid.addColumn(o -> {
            try {
                return o.getWhen().format(DateTimeFormatter.ofPattern(dateTimePattern));
            } catch (NullPointerException nullPointerException) {
                return o.getPlannedWhen().format(DateTimeFormatter.ofPattern(dateTimePattern));
            }
        }).setHeader("Wann").setAutoWidth(true);

        grid.addColumn(o -> {
            try {
                return o.getDelay() / 60;
            } catch (NullPointerException e) {
                return 0;
            }
        }).setHeader("Verspätung (min)").setAutoWidth(true);

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
        grid.addComponentColumn(trip -> {
            Span span = new Span();
            try {
                if (Boolean.TRUE.equals(trip.getCancelled())) {
                    span.setText("Ausgefallen");
                    span.getElement().getThemeList().add("badge primary");
                    return span;
                }
            } catch (NullPointerException ignored) {
            }
            if (trip.getDelay() == null || trip.getDelay() == 0 || trip.getDelay() <= 360) {
                span.setText("Pünktlich");
                span.getElement().getThemeList().add("badge success primary");
            } else if (trip.getDelay() > 360 && trip.getDelay() <= 600) {
                span.setText("Leichte Verspätung");
                span.getElement().getThemeList().add("badge warn primary");
            } else {
                span.setText("Hohe Verspätung");
                span.getElement().getThemeList().add("badge primary");
            }
            return span;
        }).setHeader("Status").setFlexGrow(0).setWidth("12%");

        GridContextMenu<Trip> contextMenu = grid.addContextMenu();
        contextMenu.addItem("Fahrtverlauf", e -> {
            Optional<Trip> trip = e.getItem();
            if (trip.isPresent()) {
                // if trip is at least a national trip (not a local trip) we can show the trip history
                if (trip.get().getLine().getProduct().contains("national")) {
                    TripService tripService = VaadinService.getCurrent().getInstantiator().getOrCreate(TripService.class);
                    Result<List<Trip>, JPAError> result = tripService.findAllByLineNameAndTripIdContains(trip.get().getLine().getName(), TripUtil.getPartOfTripIdByLocalDate(trip.get().getPlannedWhen().toLocalDate()));
                    if (result.isSuccess()) {
                        List<Trip> trips = result.getData();
                        trips = TripUtil.removeDuplicates(trips);
                        TripHistoryDialog dialog = new TripHistoryDialog(trips);
                        dialog.setWidth("80%");
                        dialog.setHeight("80%");
                        dialog.open();
                    } else {
                        Notification error = NotificationFactory.createNotification(NotificationTypes.ERROR, "Fahrtverlauf konnte nicht geladen werden");
                        error.open();
                    }
                } else {
                    Notification error = NotificationFactory.createNotification(NotificationTypes.ERROR, "Der Fahrtverlauf ist nur für Fahrten des Fernverkehrs abrufbar");
                    error.open();
                }
            }
        });

        setHeaderTitle("Ankünfte und Abfahrten für " + stopName);

        content.add(grid);


        add(content);
    }

    public void setItems(DataProvider dataProvider) {
        grid.setItems(dataProvider);
    }

}
