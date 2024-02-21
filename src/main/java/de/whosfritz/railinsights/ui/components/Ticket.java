package de.whosfritz.railinsights.ui.components;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Define a class for the ticket data
@Getter
@Setter
public class Ticket {
    private String departure;
    private String arrival;
    private String duration;
    private String transfers;
    private String price;
    private List<String> trainDetails; // A string that represents the train types and numbers, separated by commas

    public Ticket(String departure, String arrival, String duration, String transfers,
                  String price, List<String> trainDetails) {
        this.departure = departure;
        this.arrival = arrival;
        this.duration = duration;
        this.transfers = transfers;
        this.price = price;
        this.trainDetails = trainDetails;
    }
}