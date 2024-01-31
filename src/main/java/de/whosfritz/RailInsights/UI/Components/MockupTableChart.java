package de.whosfritz.RailInsights.UI.Components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;

import java.util.List;

public class MockupTableChart extends Div {

    public MockupTableChart() {
        // tag::snippet[]
        Grid<Person> grid = new Grid<>(Person.class, false);
        grid.addColumn(Person::getWert1).setHeader("Abfahrtsbahnhof");
        grid.addColumn(Person::getWert2).setHeader("Ankunftsbahnhof");
        grid.addColumn(Person::getWert3).setHeader("Zugnummer");
        grid.addColumn(Person::getWert4).setHeader("Abfahrtszeit");

        List<Person> people = List.of(
                new Person("Hauptbahnhof", "Stadtmitte", 123, "08:00"),
                new Person("Westbahnhof", "Nordstadt", 456, "10:30"),
                new Person("Ostbahnhof", "Südstadt", 789, "12:45"),
                new Person("Nordbahnhof", "Weststadt", 012, "15:20"),
                new Person("Südbahnhof", "Oststadt", 345, "17:55"),
                new Person("Stadtmitte", "Hauptbahnhof", 678, "19:30"),
                new Person("Nordstadt", "Westbahnhof", 901, "22:15"),
                new Person("Südstadt", "Ostbahnhof", 234, "07:40"),
                new Person("Weststadt", "Nordbahnhof", 567, "09:10"),
                new Person("Oststadt", "Südbahnhof", 890, "11:25"),
                new Person("Zentralbahnhof", "Stadtzentrum", 111, "14:00"),
                new Person("Schnellbahnhof", "Fernstadt", 222, "16:30"),
                new Person("Regionalbahnhof", "Vorort", 333, "18:45"),
                new Person("Fernbahnhof", "Schnellstadt", 444, "21:20"),
                new Person("Stadtzentrum", "Zentralbahnhof", 555, "23:55"),
                new Person("Fernstadt", "Regionalbahnhof", 666, "02:30"),
                new Person("Vorort", "Fernbahnhof", 777, "04:45"),
                new Person("Schnellstadt", "Stadtzentrum", 888, "06:10"),
                new Person("Bahnhof Mitte", "Bahnhof West", 999, "09:30"),
                new Person("Bahnhof West", "Bahnhof Ost", 000, "12:15"),
                new Person("Bahnhof Ost", "Bahnhof Nord", 111, "14:40"),
                new Person("Bahnhof Nord", "Bahnhof Süd", 222, "17:05"),
                new Person("Bahnhof Süd", "Bahnhof Mitte", 333, "19:30"),
                new Person("Zentrum Bahnhof", "Stadtmitte Bahnhof", 444, "21:55"),
                new Person("Mitte Bahnhof", "Zentrum Bahnhof", 555, "00:20"),
                new Person("Süd Bahnhof", "Nord Bahnhof", 666, "03:45"),
                new Person("Ost Bahnhof", "West Bahnhof", 777, "06:10"),
                new Person("West Bahnhof", "Süd Bahnhof", 888, "08:35"),
                new Person("Nord Bahnhof", "Ost Bahnhof", 999, "11:00")
        );

        grid.setItems(people);
        // end::snippet[]

        add(grid);
    }

}
