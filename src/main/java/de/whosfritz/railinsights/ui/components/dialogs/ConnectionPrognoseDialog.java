package de.whosfritz.railinsights.ui.components.dialogs;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.olech2412.adapter.dbadapter.model.journey.Journey;
import de.olech2412.adapter.dbadapter.model.journey.sub.Leg;
import de.olech2412.adapter.dbadapter.model.journey.sub.Stopover;
import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.whosfritz.railinsights.data.PrognoseDTO;
import de.whosfritz.railinsights.data.PrognoseStateEnum;
import de.whosfritz.railinsights.data.dataprovider.JourneyDataProvider;
import de.whosfritz.railinsights.ui.factories.notification.NotificationFactory;
import de.whosfritz.railinsights.ui.factories.notification.NotificationTypes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.MINUTES;

public class ConnectionPrognoseDialog extends GeneralRailInsightsDialog {

    String dateTimePattern = "HH:mm dd.MM.uuuu";

    public ConnectionPrognoseDialog(Journey ticket) {
        setHeaderTitle("Prognose für deine Verbindung");
        setWidth("80%");
        setHeight("80%");
        JourneyDataProvider journeyDataProvider = new JourneyDataProvider();
        HashMap<Integer, List<Trip>> tripsByJourney = journeyDataProvider.findTripsByJourney(ticket);

        if (tripsByJourney.isEmpty()) {
            Notification errorNotification = NotificationFactory.createNotification(NotificationTypes.ERROR, "Für diese Verbindung kann leider keine Prognose erstellt werden.");
            errorNotification.open();
        }

        List<PrognoseDTO> prognoseDTOs = createPrognose(tripsByJourney, ticket);
        if (prognoseDTOs.isEmpty()) {
            Notification errorNotification = NotificationFactory.createNotification(NotificationTypes.ERROR, "Für diese Verbindung kann leider keine Prognose erstellt werden, da unser Datenbestand nicht ausreicht.");
            errorNotification.open();
            return;
        }

        // if the first item does not contain the correct start stop or the last item does not contain the correct end stop throw a notification
        if (!prognoseDTOs.get(0).getStop().equals(ticket.getLegs().get(0).getOrigin().getName()) || !prognoseDTOs.get(prognoseDTOs.size() - 1).getStop().equals(ticket.getLegs().get(ticket.getLegs().size() - 1).getDestination().getName())) {
            Notification errorNotification = NotificationFactory.createNotification(NotificationTypes.ERROR, "Für diese Verbindung kann leider keine Prognose erstellt werden, da unser Datenbestand nicht ausreicht.");
            errorNotification.open();
            return;
        }

        Grid<PrognoseDTO> grid = new Grid<>(PrognoseDTO.class);
        grid.setItems(createPrognose(tripsByJourney, ticket));
        grid.addClassName("prognose-grid");
        grid.setColumns("line", "stop", "calculatedDelayInMinutes");
        grid.getColumnByKey("line").setHeader("Linie").setAutoWidth(true);
        grid.getColumnByKey("stop").setHeader("Haltestelle").setAutoWidth(true);
        grid.addColumn(o -> {
            try {
                return o.getPlannedTime().format(DateTimeFormatter.ofPattern(dateTimePattern));
            } catch (NullPointerException nullPointerException) {
                return "";
            }
        }).setHeader("Geplante Abfahrt").setAutoWidth(true);
        grid.addColumn(o -> {
            try {
                return o.getPredictedTime().format(DateTimeFormatter.ofPattern(dateTimePattern));
            } catch (NullPointerException nullPointerException) {
                return "";
            }
        }).setHeader("Voraussichtliche Abfahrt").setAutoWidth(true);
        grid.getColumnByKey("calculatedDelayInMinutes").setHeader("Prognostizierte Verspätung")
                .setAutoWidth(true).setFlexGrow(0);
        grid.addComponentColumn(prognoseDTO -> {
            Span span = new Span();
            if (prognoseDTO.getState().equals(PrognoseStateEnum.DELAYED)) {
                span.setText("Verspätet");
                span.getElement().getThemeList().add("badge warn primary");
            } else if (prognoseDTO.getState().equals(PrognoseStateEnum.ON_TIME)) {
                span.setText("Pünktlich");
                span.getElement().getThemeList().add("badge success primary");
            } else if (prognoseDTO.getState().equals(PrognoseStateEnum.PROBABLY_NOT_REACHABLE)) {
                span.setText("Umstieg wahrscheinlich nicht möglich");
                span.getElement().getThemeList().add("badge primary");
            } else if (prognoseDTO.getState().equals(PrognoseStateEnum.LOW_TRANSFER_TIME)) {
                span.setText("Kurze Umsteigezeit");
                span.getElement().getThemeList().add("badge contrast primary");
            } else if (prognoseDTO.getState().equals(PrognoseStateEnum.OK_TRANSFER_TIME)) {
                span.setText("Ausreichende Umsteigezeit");
                span.getElement().getThemeList().add("badge success primary");
            }
            return span;
        }).setHeader("Status").setAutoWidth(true);

        grid.setPartNameGenerator(prognoseDTO -> {
            if (prognoseDTO.getLine().equals("Umstieg")) {
                if (prognoseDTO.getState().equals(PrognoseStateEnum.OK_TRANSFER_TIME)) {
                    return "validTransfer";
                } else if (prognoseDTO.getState().equals(PrognoseStateEnum.LOW_TRANSFER_TIME)) {
                    return "lowTransfer";
                } else {
                    return "invalidTransfer";
                }
            }
            return "";
        });

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();

        VerticalLayout infoLayout = new VerticalLayout();

        Paragraph infoParagraph = new Paragraph("Wie funktionierts? Die Prognose zeigt dir auf Grundlage von " +
                "vergangen Fahrten am selben Wochentag und zur selben Uhrzeit, wie wahrscheinlich es ist, dass deine " +
                "Verbindung pünktlich ist und ob du deine Anschlüsse erreichst.");
        Paragraph infoParagraph2 = new Paragraph("Für die dauer eines Umstiegs werden 5 Minuten eingeplant. " +
                "Wenn die Umsteigezeit kürzer als 5 Minuten ist, wird dies als kurze Umsteigezeit angezeigt. " +
                "Wenn die Umstiegszeit länger ist, wird dies als ausreichende Umsteigezeit angezeigt." +
                "Liegt die Zeit des gepanten Umstiegs unter der prognostizierten Abfahrtszeit des nächsten Zuges, " +
                "wird dies als wahrscheinlich nicht erreichbar angezeigt.");
        Paragraph infoParagraph3 = new Paragraph("Diese Prognose ist eine grobe Orientierungshilfe und kann nicht " +
                "garantieren, dass deine Verbindung pünktlich ist. Die tatsächliche Verspätung kann von der Prognose abweichen." +
                " Bei deiner Reiseplanung solltest du dich auf die offiziellen Fahrpläne und Echtzeitdaten verlassen.");

        infoLayout.add(infoParagraph, infoParagraph2, infoParagraph3);

        add(infoLayout, grid);

    }


    private List<PrognoseDTO> createPrognose(HashMap<Integer, List<Trip>> tripsByJourney, Journey ticket) {
        // Create a list of PrognoseDTOs
        List<PrognoseDTO> prognoseDTOs = new ArrayList<>();
        for (Map.Entry<Integer, List<Trip>> entry : tripsByJourney.entrySet()) {
            // for each different stop in the triplist calculate the average delay and add it to the list
            List<Stop> stops = new ArrayList<>();
            for (Trip trip : entry.getValue()) {
                if (!stops.contains(trip.getStop())) {
                    stops.add(trip.getStop());
                }
            }
            for (Stop stop : stops) {
                List<Trip> tripsAtStop = entry.getValue().parallelStream().filter(trip -> trip.getStop().equals(stop)).toList();
                int averageDelayAtStop = calculateAverageDelay(tripsAtStop);
                PrognoseDTO prognoseDTO = new PrognoseDTO();
                prognoseDTO.setStop(stop.getName());
                prognoseDTO.setCalculatedDelayInMinutes(averageDelayAtStop);
                try {
                    prognoseDTO.setLine(tripsAtStop.get(0).getLine().getName());
                } catch (NullPointerException np) {
                    prognoseDTO.setLine("Fußweg");
                }

                if (averageDelayAtStop >= 6) {
                    prognoseDTO.setState(PrognoseStateEnum.DELAYED);
                } else {
                    prognoseDTO.setState(PrognoseStateEnum.ON_TIME);
                }

                prognoseDTOs.add(prognoseDTO);
            }
        }

        List<Stopover> stops = new ArrayList<>();
        for (Leg leg : ticket.getLegs()) {
            // check if the stopovers are not null
            if (leg.getStopovers() != null) {
                stops.addAll(leg.getStopovers());
            }
        }


        // sort the list of prognoseDTOs by the stopovers of the ticket but keep the order of the lines
        List<PrognoseDTO> sortedPrognoseDTOs = new ArrayList<>();
        for (Leg leg : ticket.getLegs()) {
            if (leg.getStopovers() != null) {
                for (Stopover stopover : leg.getStopovers()) {
                    for (PrognoseDTO prognoseDTO : prognoseDTOs) {
                        if (prognoseDTO.getStop().equals(stopover.getStop().getName()) && prognoseDTO.getLine().equals(leg.getLine().getName())) {
                            sortedPrognoseDTOs.add(prognoseDTO);
                        }
                    }
                }
            }
        }

        // remove all duplicates identified by stop and line
        List<PrognoseDTO> uniquePrognoseDTOs = new ArrayList<>();
        for (PrognoseDTO prognoseDTO : sortedPrognoseDTOs) {
            if (!uniquePrognoseDTOs.contains(prognoseDTO)) {
                uniquePrognoseDTOs.add(prognoseDTO);
            }
        }

        for (int i = 0; i < uniquePrognoseDTOs.size(); i++) {
            // if the departure is null its an arrival
            if (stops.get(i).getPlannedDeparture() == null) {
                uniquePrognoseDTOs.get(i).setPlannedTime(LocalDateTime.parse(stops.get(i).getPlannedArrival(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")));
                uniquePrognoseDTOs.get(i).setPredictedTime(LocalDateTime.parse(stops.get(i).getPlannedArrival(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")).plusMinutes(uniquePrognoseDTOs.get(i).getCalculatedDelayInMinutes()));
            } else {
                uniquePrognoseDTOs.get(i).setPlannedTime(LocalDateTime.parse(stops.get(i).getPlannedDeparture(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")));
                uniquePrognoseDTOs.get(i).setPredictedTime(LocalDateTime.parse(stops.get(i).getPlannedDeparture(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")).plusMinutes(uniquePrognoseDTOs.get(i).getCalculatedDelayInMinutes()));
            }
        }

        // if a prognoseDTO has a stop that exists twice in the list add a transfer
        List<PrognoseDTO> transfers = new ArrayList<>();
        for (PrognoseDTO prognoseDTO : uniquePrognoseDTOs) {
            List<PrognoseDTO> stopsAtStop = uniquePrognoseDTOs.parallelStream().filter(prognoseDTO1 -> prognoseDTO1.getStop().equals(prognoseDTO.getStop())).toList();

            // check if the transfer already exists in the list
            if (transfers.parallelStream().noneMatch(transfer -> transfer.getStop().equals(prognoseDTO.getStop()))) {
                if (stopsAtStop.size() > 1) {
                    PrognoseDTO transfer = new PrognoseDTO();
                    transfer.setStop(prognoseDTO.getStop());
                    transfer.setLine("Umstieg");
                    transfers.add(transfer);
                }
            }
        }

        for (PrognoseDTO transfer : transfers) {
            List<PrognoseDTO> tripsAtStop = uniquePrognoseDTOs.stream().filter(prognoseDTO -> prognoseDTO.getStop().equals(transfer.getStop())).toList();
            LocalDateTime arrivalOfFirstTrain = tripsAtStop.get(0).getPredictedTime();
            LocalDateTime departureOfSecondTrain = tripsAtStop.get(1).getPredictedTime();

            transfer.setPlannedTime(arrivalOfFirstTrain);
            transfer.setPredictedTime(arrivalOfFirstTrain.plusMinutes(5));

            if (departureOfSecondTrain.isBefore(transfer.getPredictedTime())) {
                transfer.setState(PrognoseStateEnum.PROBABLY_NOT_REACHABLE);
            } else {
                if (MINUTES.between(arrivalOfFirstTrain, departureOfSecondTrain) < 5) {
                    transfer.setState(PrognoseStateEnum.LOW_TRANSFER_TIME);
                } else {
                    transfer.setState(PrognoseStateEnum.OK_TRANSFER_TIME);
                }
            }
        }

        // add the transfers to the list at the right position
        for (PrognoseDTO transfer : transfers) {
            for (int i = 0; i < uniquePrognoseDTOs.size(); i++) {
                if (uniquePrognoseDTOs.get(i).getStop().equals(transfer.getStop())) {
                    uniquePrognoseDTOs.add(i + 1, transfer);
                    break;
                }
            }
        }

        // check at each transfer if the previous train is a different train than the train after the transfer if thats not the case remove the transfer
        List<PrognoseDTO> allTransfers = uniquePrognoseDTOs.stream().filter(prognoseDTO -> prognoseDTO.getLine().equals("Umstieg")).toList();
        for (PrognoseDTO transfer : allTransfers) {
            int index = uniquePrognoseDTOs.indexOf(transfer);
            if (uniquePrognoseDTOs.get(index - 1).getLine().equals(uniquePrognoseDTOs.get(index + 1).getLine())) {
                uniquePrognoseDTOs.remove(transfer);
            }
        }

        return uniquePrognoseDTOs;
    }

    private int calculateAverageDelay(List<Trip> list) {
        int sum = 0;
        for (Trip trip : list) {
            try {
                sum += trip.getDelay() / 60;
            } catch (NullPointerException e) {
                sum += 0;
            }
        }
        return sum / list.size();
    }
}
