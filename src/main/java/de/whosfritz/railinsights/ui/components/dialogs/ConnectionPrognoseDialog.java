package de.whosfritz.railinsights.ui.components.dialogs;

import com.vaadin.flow.component.dialog.Dialog;
import de.olech2412.adapter.dbadapter.model.journey.Journey;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.whosfritz.railinsights.data.dataprovider.JourneyDataProvider;

import java.util.HashMap;
import java.util.List;

public class ConnectionPrognoseDialog extends Dialog {

    public ConnectionPrognoseDialog(Journey ticket) {
        JourneyDataProvider journeyDataProvider = new JourneyDataProvider();
        HashMap<Integer, List<Trip>> tripsByJourney = journeyDataProvider.findTripsByJourney(ticket);

        if (tripsByJourney.size() > 0) {
            // Do something
        }
    }
}
