package de.whosfritz.railinsights.ui.dataprovider;

import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.whosfritz.railinsights.ui.components.boards.board_components.Indicator;

import java.util.ArrayList;
import java.util.List;

public class RailInsightsInfoBoardDataProvider {

    public List<Indicator> indicators = new ArrayList<>();

    public void setData(List<Trip> trips, List<Trip> tripsToday, List<Stop> stops) {
        int tripsCount = tripsToday.size();
        if (tripsCount == 0) {
            indicators.add(new Indicator("Erfasste Stopps insgesamt: ", String.valueOf(trips.size()), "Erfasste Stopps heute: " + tripsCount, 0));
        } else {
            indicators.add(new Indicator("Erfasste Stopps insgesamt: ", String.valueOf(trips.size()), "Erfasste Stopps heute: " + tripsCount, 1));
        }

        indicators.add(new Indicator("Erfasste Haltepunkte insgesamt: ", String.valueOf(stops.size())));
        indicators.add(new Indicator("Haltepunkte für die Stopps erfasst werden: ", String.valueOf(stops.stream().filter(stop -> stop.getProducts().isNational()).count())));
    }

}
