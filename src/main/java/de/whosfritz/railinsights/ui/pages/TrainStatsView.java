package de.whosfritz.railinsights.ui.pages;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.olech2412.adapter.dbadapter.model.stop.sub.Line;
import de.whosfritz.railinsights.data.services.train_services.TrainStatsService;
import de.whosfritz.railinsights.ui.factories.notification.NotificationFactory;
import de.whosfritz.railinsights.ui.factories.notification.NotificationTypes;
import de.whosfritz.railinsights.ui.layout.MainView;

import java.util.List;

@Route(value = "trainmetrics", layout = MainView.class)
public class TrainStatsView extends HorizontalLayout {

    private final TextField lineSearchField;

    private final OrderedList cardList;


    private Button searchButton;

    public TrainStatsView(TrainStatsService trainStatsService) {
        VerticalLayout searchLayout = new VerticalLayout();

        Scroller scroller = new Scroller();
        scroller.addClassNames(LumoUtility.Border.ALL, LumoUtility.BorderRadius.MEDIUM);
        scroller.setWidthFull();
        scroller.setVisible(false);


        cardList = new OrderedList();
        cardList.setType(OrderedList.NumberingType.LOWERCASE_LETTER);
        cardList.addClassNames(LumoUtility.FlexDirection.COLUMN, LumoUtility.ListStyleType.NONE, LumoUtility.Padding.XSMALL);


        lineSearchField = new TextField();
        lineSearchField.setPlaceholder("Zugnummer");
        lineSearchField.setClearButtonVisible(true);
        lineSearchField.addValueChangeListener(event -> searchButton.setEnabled(!event.getValue().isEmpty()));
        lineSearchField.setWidthFull();

        searchButton = new Button("Suchen");
        searchButton.setEnabled(false);
        searchButton.setWidthFull();
        searchButton.setWidthFull();

        searchButton.addClickListener(event -> {
            List<Line> lines = trainStatsService.getLinesByLineName(lineSearchField.getValue()).get();
            lines.forEach(line -> {
                HorizontalLayout layout = new HorizontalLayout();
                Div div = new Div();
                div.add(new H4(line.getName()), new Text("Fahrt Nr.: " + line.getFahrtNr()));
                layout.setJustifyContentMode(JustifyContentMode.BETWEEN);
                div.setWidthFull();
                layout.setAlignItems(Alignment.CENTER);

                layout.addClassNames(LumoUtility.Border.ALL, LumoUtility.BorderRadius.MEDIUM, LumoUtility.Padding.MEDIUM);
                layout.addClassNames(LumoUtility.Background.CONTRAST_5);
                layout.addClassNames(LumoUtility.Margin.MEDIUM);
                Button infoButton = new Button(VaadinIcon.INFO_CIRCLE.create());

                layout.add(div, infoButton);

                /*
                 */
                cardList.add(new ListItem(layout));
            });
            if (!lines.isEmpty()) {
                scroller.setVisible(true);
                scroller.setContent(cardList);
            } else {
                Notification notification = NotificationFactory.createwNotification(NotificationTypes.ERROR, "Keine Züge gefunden");
                notification.open();
            }

        });
        searchLayout.add(lineSearchField, searchButton, scroller);

        searchLayout.setMinWidth(30f, Unit.PERCENTAGE);
        searchLayout.setMaxWidth(30f, Unit.PERCENTAGE);


        setSizeFull();
        add(searchLayout);
    }
}