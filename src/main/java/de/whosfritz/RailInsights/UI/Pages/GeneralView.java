package de.whosfritz.RailInsights.UI.Pages;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.whosfritz.RailInsights.UI.Components.MockupLineChart;
import de.whosfritz.RailInsights.UI.Components.MockupPieChart;
import de.whosfritz.RailInsights.UI.Layout.MainView;

import java.util.Locale;

@Route(value = "/", layout = MainView.class)
public class GeneralView extends VerticalLayout {

    public GeneralView() {
        getStyle().set("padding-left", "8rem");

        Locale finnishLocale = new Locale("fi", "FI");

        DatePicker datePicker = new DatePicker("Beginn");
        datePicker.setLocale(finnishLocale);

        DatePicker datePicker2 = new DatePicker("Ende");
        datePicker2.setLocale(finnishLocale);

        setAlignItems(Alignment.CENTER);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(Alignment.BASELINE);
        horizontalLayout.setAlignSelf(Alignment.CENTER);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        horizontalLayout.add(datePicker, datePicker2, new Button("Suchen"), new Button(VaadinIcon.REFRESH.create()));
        add(horizontalLayout);
        add(new MockupLineChart());
        add(new MockupPieChart());

    }
}
