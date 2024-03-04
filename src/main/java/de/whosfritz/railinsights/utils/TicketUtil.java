package de.whosfritz.railinsights.utils;

import de.olech2412.adapter.dbadapter.model.journey.Journey;
import de.olech2412.adapter.dbadapter.model.stop.sub.Line;
import de.whosfritz.railinsights.data.Ticket;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketUtil {

    public static List<Ticket> convertJourneysToTickets(List<Journey> journeys) {
        List<Ticket> tickets = new ArrayList<>();

        for (Journey journey : journeys) {
            String departure = journey.getLegs().get(0).getDeparture();
            // get the arrival of the last leg
            String arrival = journey.getLegs().get(journey.getLegs().size() - 1).getArrival();

            ZonedDateTime arrivalDateTime = ZonedDateTime.parse(arrival);
            ZonedDateTime departureDateTime = ZonedDateTime.parse(departure);
            String durationInMinutes = Duration.between(departureDateTime, arrivalDateTime).toMinutes() + " Minuten";

            // convert ZonedDateTime to format "HH:mm dd.MM.yyyy"
            arrival = arrivalDateTime.toLocalDateTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));
            departure = departureDateTime.toLocalDateTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));

            String transfers = String.valueOf(journey.getLegs().size() - 1);
            String price = " unbekannt";
            if (journey.getPrice() != null)
                price = journey.getPrice().getAmount() + " " + journey.getPrice().getCurrency();
            List<String> trainDetails = new ArrayList<>();

            for (int i = 0; i < journey.getLegs().size(); i++) {
                StringBuilder trainDetail = new StringBuilder();

                // check if line is null if thats the case, the leg is a walk so create a custom line name
                if (journey.getLegs().get(i).getLine() == null) {
                    Line line = new Line();
                    line.setName("Fußweg");
                    journey.getLegs().get(i).setLine(line);
                }

                String arrivalTime = ZonedDateTime.parse(journey.getLegs().get(i).getArrival()).toLocalDateTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
                trainDetail.append(journey.getLegs().get(i).getLine().getName() + " bis " + journey.getLegs().get(i).getDestination().getName() + " | " + "Ankunft um " + arrivalTime);

                if (i < journey.getLegs().size() - 1) {
                    String changeTime = Duration.between(ZonedDateTime.parse(journey.getLegs().get(i).getArrival()), ZonedDateTime.parse(journey.getLegs().get(i + 1).getDeparture())).toMinutes() + " Minuten";
                    trainDetail.append(" | Umstiegszeit: " + changeTime);
                }

                trainDetails.add(trainDetail.toString());
            }

            Ticket ticket = new Ticket(departure, arrival, durationInMinutes, transfers, price, trainDetails, journey);
            tickets.add(ticket);
        }

        return tickets;
    }
}