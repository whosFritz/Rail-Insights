package de.whosfritz.railinsights.ui.components.dialogs;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.olech2412.adapter.dbadapter.model.trip.sub.Remark;
import de.whosfritz.railinsights.ui.components.Divider;

import java.util.List;

/**
 * Dialog to show remarks for a trip.
 */
public class RemarksDialog extends GeneralRailInsightsDialog {

    /**
     * Constructor for the remarks dialog.
     *
     * @param remarks list of remarks for the trip
     * @param trip    the trip which the remarks are for
     */
    public RemarksDialog(List<Remark> remarks, Trip trip) {
        super();

        setHeaderTitle("Informationen für die Linie " + trip.getLine().getName() + " an der Haltestelle " + trip.getStop().getName());
        setWidth("50%");
        setHeight("50%");

        VerticalLayout content = new VerticalLayout();
        content.setWidth("100%");
        content.setHeight("100%");

        content.add(new H3("Bahnhof:"));
        content.add(new Divider());

        TextField station = new TextField();
        station.setValue(trip.getStop().getName());
        station.setLabel("Bahnhof");
        station.setPrefixComponent(new Icon(VaadinIcon.MAP_MARKER));
        station.setReadOnly(true);
        content.add(station);

        content.add(new H3("Meldungen:"));
        content.add(new Divider());

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("100%");
        formLayout.setHeight("100%");
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        remarks.forEach(remark -> {
            TextArea textField = new TextArea();
            textField.setLabel("Meldung " + (remarks.indexOf(remark) + 1) + " von " + remarks.size());
            textField.setValue(remark.getText());
            textField.setReadOnly(true);
            formLayout.add(textField);
        });

        content.add(formLayout);
        add(content);
    }

}
