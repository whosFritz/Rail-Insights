package de.whosfritz.RailInsights.UI.Components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;

import java.util.List;

public class MockupTableChart
        extends Div {

    public MockupTableChart() {
        // tag::snippet[]
        Grid<Person> grid = new Grid<>(Person.class, false);
        grid.addColumn(Person::getWert1).setHeader("Wert1");
        grid.addColumn(Person::getWert2).setHeader("Wert2");
        grid.addColumn(Person::getWert3).setHeader("Wert3");
        grid.addColumn(Person::getWert4).setHeader("Wert4");

        List<Person> people = List.of(
                new Person("ABC", "123", 42, "Xyz"),
                new Person("DEF", "456", 21, "Abc"),
                new Person("GHI", "789", 35, "Mno"),
                new Person("JKL", "012", 28, "Pqr"),
                new Person("MNO", "345", 39, "Def"),
                new Person("PQR", "678", 45, "Jkl"),
                new Person("STU", "901", 32, "Ghi"),
                new Person("VWX", "234", 27, "Uvw"),
                new Person("YZA", "567", 31, "Bcd"),
                new Person("BCD", "890", 36, "Lmn"),
                new Person("EFG", "123", 29, "Ijk"),
                new Person("HIJ", "456", 40, "Efg"),
                new Person("KLM", "789", 33, "Stu"),
                new Person("NOP", "012", 26, "Vwx"),
                new Person("QRS", "345", 37, "Yza"),
                new Person("TUV", "678", 24, "Efg"),
                new Person("WXY", "901", 38, "Rst"),
                new Person("ZAB", "234", 30, "Hij"),
                new Person("CDE", "567", 43, "Klm"),
                new Person("FGH", "890", 22, "Qrs"),
                new Person("IJK", "123", 34, "Nop"),
                new Person("LMN", "456", 41, "Tuv"),
                new Person("OPQ", "789", 25, "Wxy"),
                new Person("RST", "012", 44, "Zab"),
                new Person("UVW", "345", 27, "Cde"),
                new Person("XYZ", "678", 32, "Ijk"),
                new Person("123", "901", 39, "Def"),
                new Person("456", "234", 28, "Pqr"),
                new Person("789", "567", 35, "Mno")
        );


        grid.setItems(people);
        // end::snippet[]

        add(grid);
        setSizeFull();
    }

}
