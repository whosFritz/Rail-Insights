package de.whosfritz.railinsights.ui.pages;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.map.Map;
import com.vaadin.flow.component.map.configuration.Coordinate;
import com.vaadin.flow.component.map.configuration.Feature;
import com.vaadin.flow.component.map.configuration.View;
import com.vaadin.flow.component.map.configuration.feature.MarkerFeature;
import com.vaadin.flow.component.map.configuration.layer.FeatureLayer;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.whosfritz.railinsights.data.dto.StopDto;
import de.whosfritz.railinsights.data.services.stop_services.StopService;
import de.whosfritz.railinsights.ui.components.Divider;
import de.whosfritz.railinsights.ui.factories.notification.NotificationFactory;
import de.whosfritz.railinsights.ui.factories.notification.NotificationTypes;
import de.whosfritz.railinsights.ui.layout.MainView;
import de.whosfritz.railinsights.ui.services.DataProviderService;

import java.util.HashMap;
import java.util.List;


@Route(value = "bahnhöfe", layout = MainView.class)
public class StationView extends HorizontalLayout implements BeforeEnterListener {
    private Map map = new Map();

    private OrderedList cardList;
    private java.util.Map<StopDto, Button> stopToCard = new HashMap<>();

    private List<StopDto> filteredStops;
    private java.util.Map<Feature, StopDto> stopToLocation = new HashMap<>();

    public StationView() {
        addClassName("map-view");
        setSizeFull();
        map.getElement().setAttribute("theme", "borderless");
        map.getElement().setAttribute("class", "map");
        map.setHeight(100f, Unit.PERCENTAGE);
        map.setWidth(75f, Unit.PERCENTAGE);

        VerticalLayout sidebar = new VerticalLayout();
        sidebar.setSpacing(false);
        sidebar.setPadding(false);

        sidebar.setWidth(25f, Unit.PERCENTAGE);
        sidebar.addClassNames("sidebar");

        TextField searchField = new TextField();
        searchField.setPlaceholder("Suche nach Haltepunkt...");
        searchField.setWidthFull();
        searchField.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER);
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> {
            updateFilter(searchField.getValue().toLowerCase());
        });
        searchField.setClearButtonVisible(true);
        searchField.setSuffixComponent(new Icon("lumo", "search"));

        Scroller scroller = new Scroller();
        scroller.addClassNames(LumoUtility.Padding.Horizontal.MEDIUM, LumoUtility.Width.FULL, LumoUtility.BoxSizing.BORDER);

        cardList = new OrderedList();
        cardList.setType(OrderedList.NumberingType.LOWERCASE_LETTER);
        cardList.addClassNames("card-list", LumoUtility.Gap.XSMALL, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.ListStyleType.NONE,
                LumoUtility.Margin.AUTO, LumoUtility.Padding.XSMALL);
        sidebar.add(searchField, scroller);
        scroller.setContent(cardList);

        Paragraph infoParagraph = new Paragraph("In der interaktiven Karte findest du alle Fernverkehrsbahnhöfe in Deutschland." +
                " Nutze die Suchfunktion auf der rechten Seite um nach einem bestimmten Bahnhof zu suchen.");

        Paragraph infoCalcParagraph = new Paragraph("Hinweis zur vollständigkeit: Es werden nur Bahnhöfe angezeigt, " +
                "an denen mindestens Nationaler Fernverkehr stattfindet. Es kann sein, dass einzelne Sub-Betriebsstellen " +
                "wie Berlin Hbf (tief) nicht vorkommen, da Sie hier als Berlin Hbf indexiert sind. Es kann ebenfalls sein," +
                " dass Bahnhöfe auftauchen, die nicht vom Fernverkehr bedient werden, jedoch zu einer Betriebsstelle gehören," +
                " die vom Fernverkehr bedient wird (z.B. Leipzig Hbf (tief)).");

        HorizontalLayout infoLayout = new HorizontalLayout(new VerticalLayout(infoParagraph, infoCalcParagraph));
        infoLayout.setSpacing(false);
        infoLayout.setPadding(false);

        Scroller scrollerSidebar = new Scroller(
                new Div(sidebar));
        scrollerSidebar.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        scrollerSidebar.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding", "var(--lumo-space-m)");

        HorizontalLayout mapAndSidebar = new HorizontalLayout(map, scrollerSidebar);
        mapAndSidebar.setHeight(100f, Unit.PERCENTAGE);
        mapAndSidebar.setWidth(100f, Unit.PERCENTAGE);
        mapAndSidebar.setMaxHeight(100f, Unit.PERCENTAGE);
        mapAndSidebar.setMaxWidth(100f, Unit.PERCENTAGE);
        mapAndSidebar.add(map, sidebar);
        VerticalLayout content = new VerticalLayout(infoLayout, mapAndSidebar);

        add(content);

        configureMap();
        updateCardList();
    }

    private void centerMapOn(StopDto stop) {
        View view = map.getView();
        view.setCenter(new Coordinate(stop.getLongitude(), stop.getLatitude()));
        view.setZoom(14);
    }

    private void scrollToCard(StopDto stop) {
        stopToCard.get(stop).scrollIntoView();
    }

    private void centerMapDefault() {
        View view = new View();
        view.setCenter(new Coordinate(9.588, 51.28));
        view.setZoom(5.4242f);
        map.setView(view);
    }

    private void configureMap() {

        this.centerMapDefault();

        this.map.addFeatureClickListener(e -> {
            Feature feature = e.getFeature();
            StopDto stop = stopToLocation.get(feature);
            this.centerMapOn(stop);
            this.scrollToCard(stop);
        });

        this.updateFilter("");
    }

    private void updateCardList() {
        cardList.removeAll();
        stopToCard.clear();
        for (StopDto stop : filteredStops) {
            Button button = new Button();
            button.setAriaLabel("Klick um auf Karte zu zentrieren, Rechtsklick oder langes Drücken für mehr Option");
            button.addClassNames(LumoUtility.Height.AUTO, LumoUtility.Padding.MEDIUM);
            button.addClickListener(e -> {
                centerMapOn(stop);
            });

            if (stop.isProvidesFurtherInformation()) {
                ContextMenu contextMenu = new ContextMenu();
                contextMenu.setTarget(button);
                contextMenu.addItem("Weitere Informationen", e -> {
                    openFullStopInfo(stop);
                });
            }

            Span card = new Span();
            card.addClassNames("card", LumoUtility.Width.FULL, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.AlignItems.START, LumoUtility.Gap.XSMALL);
            Span city = new Span(stop.getStopName());
            city.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.SEMIBOLD, LumoUtility.TextColor.HEADER, LumoUtility.Padding.Bottom.XSMALL);
            Span place = new Span("Haltepunkt ID: " + stop.getStopId());
            place.addClassNames(LumoUtility.TextColor.SECONDARY);

            card.add(city, place);

            button.getElement().appendChild(card.getElement());
            cardList.add(new ListItem(button));
            stopToCard.put(stop, button);
        }
    }

    private void openFullStopInfo(StopDto stop) {
        StopService stopService = VaadinService.getCurrent().getInstantiator().getOrCreate(StopService.class);
        Stop fullStop = stopService.findStopByStopId(Long.valueOf(stop.getStopId())).getData();

        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        VerticalLayout layout = new VerticalLayout();

        layout.add(new H2("Informationen zu " + stop.getStopName()));
        layout.add(new Divider());
        layout.add(new H3("Allgemeine Informationen"));
        layout.add(new Divider());
        layout.add(new Span("Name: " + fullStop.getName()));
        layout.add(new Span("ID: " + fullStop.getStopId()));
        layout.add(new Span("Addresse: " + fullStop.getStation().getAddress().getCity()));

        dialog.add(layout);
        dialog.setVisible(true);
        dialog.open();
    }

    private void updateFilter(String filter) {
        stopToLocation.clear();
        DataProviderService dataProviderService = VaadinService.getCurrent().getInstantiator().getOrCreate(DataProviderService.class);
        filteredStops = dataProviderService.getAllNationalStopsConvertedToDto()
                .stream()
                .filter(stop -> stop.getStopName().toLowerCase().contains(filter)
                        || stop.getStopId().toLowerCase().contains(filter)
                ).toList();
        FeatureLayer featureLayer = this.map.getFeatureLayer();

        for (Feature f : featureLayer.getFeatures().toArray(Feature[]::new)) {
            featureLayer.removeFeature(f);
        }

        this.filteredStops.forEach((stop) -> {
            MarkerFeature feature = new MarkerFeature(new Coordinate(stop.getLongitude(), stop.getLatitude()));
            stopToLocation.put(feature, stop);
            featureLayer.addFeature(feature);
        });
        updateCardList();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user, that mobile devices are currently not supported if he is using one
        if (UI.getCurrent().getSession().getBrowser().isAndroid() || UI.getCurrent().getSession().getBrowser().isIPhone()) {
            Notification mobileDeviceNotification = NotificationFactory.createwNotification(NotificationTypes.CRITICAL,
                    "Mobile Geräte werden aktuell nicht unterstützt. Es kommt zu Darstellungsproblemen. Bitte benutze einen Desktop-Browser.");
            mobileDeviceNotification.open();
        }
    }
}
