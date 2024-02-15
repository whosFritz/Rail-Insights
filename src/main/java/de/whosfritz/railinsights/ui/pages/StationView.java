package de.whosfritz.railinsights.ui.pages;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.whosfritz.railinsights.data.dataprovider.TripDataProvider;
import de.whosfritz.railinsights.data.dataprovider.TripFilter;
import de.whosfritz.railinsights.data.dto.StopDto;
import de.whosfritz.railinsights.data.services.stop_services.StopService;
import de.whosfritz.railinsights.data.services.trip_services.TripService;
import de.whosfritz.railinsights.ui.components.boards.DashboardView;
import de.whosfritz.railinsights.ui.components.dialogs.ArrivalDepartureDialog;
import de.whosfritz.railinsights.ui.components.dialogs.GeneralRailInsightsDialog;
import de.whosfritz.railinsights.ui.components.dialogs.StopInfoDialog;
import de.whosfritz.railinsights.ui.factories.notification.NotificationFactory;
import de.whosfritz.railinsights.ui.factories.notification.NotificationTypes;
import de.whosfritz.railinsights.ui.layout.MainView;
import de.whosfritz.railinsights.ui.services.DataProviderService;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;


@Route(value = "bahnhöfe", layout = MainView.class)
public class StationView extends HorizontalLayout implements BeforeEnterListener {
    private final Map map = new Map();

    private final OrderedList cardList;
    private final java.util.Map<StopDto, Button> stopToCard = new HashMap<>();
    private final java.util.Map<Feature, StopDto> stopToLocation = new HashMap<>();
    private List<StopDto> filteredStops;

    private LocalDateTime whenAfter = LocalDateTime.now().minusDays(1);
    private LocalDateTime whenBefore = LocalDateTime.now();

    public StationView() {
        addClassName("map-view");
        map.getElement().setAttribute("theme", "borderless");
        map.getElement().setAttribute("class", "map");
        map.setHeight(90f, Unit.PERCENTAGE);
        map.setMaxHeight(90f, Unit.PERCENTAGE);
        map.setWidth(100f, Unit.PERCENTAGE);
        map.setMaxWidth(100f, Unit.PERCENTAGE);
        map.setMinHeight(90f, Unit.PERCENTAGE);

        VerticalLayout sidebar = new VerticalLayout();
        sidebar.setSpacing(false);
        sidebar.setPadding(false);

        sidebar.setWidth(25f, Unit.PERCENTAGE);
        sidebar.setHeight(90f, Unit.PERCENTAGE);
        sidebar.setMaxHeight(90f, Unit.PERCENTAGE);
        sidebar.setMinHeight(90f, Unit.PERCENTAGE);
        sidebar.setMaxWidth(25f, Unit.PERCENTAGE);
        sidebar.setMinWidth(25f, Unit.PERCENTAGE);

        sidebar.addClassNames("sidebar");

        TextField searchField = new TextField();
        searchField.setPlaceholder("Suche nach Bahnhof...");
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
        cardList.addClassNames("card-list", LumoUtility.Gap.XSMALL, LumoUtility.FlexDirection.COLUMN, LumoUtility.ListStyleType.NONE, LumoUtility.Padding.XSMALL);
        sidebar.setAlignItems(Alignment.BASELINE);
        sidebar.add(searchField, scroller);
        scroller.setContent(cardList);

        Paragraph infoParagraph = new Paragraph("In der interaktiven Karte findest du alle Fernverkehrsbahnhöfe in Deutschland." +
                " Nutze die Suchfunktion auf der rechten Seite um nach einem bestimmten Bahnhof zu suchen.");

        Paragraph infoCalcParagraph = new Paragraph("Hinweis zur vollständigkeit: Es werden nur Bahnhöfe angezeigt, " +
                "an denen mindestens Nationaler Fernverkehr stattfindet. Es kann sein, dass einzelne Sub-Betriebsstellen " +
                "wie Berlin Hbf (tief) nicht vorkommen, da Sie hier als Berlin Hbf indexiert sind. Es kann ebenfalls sein," +
                " dass Bahnhöfe auftauchen, die nicht vom Fernverkehr bedient werden, jedoch zu einer Betriebsstelle gehören," +
                " die vom Fernverkehr bedient wird (z.B. Leipzig Hbf (tief)). Für diese Fälle steht die Funktion 'Weitere Informationen' nicht Verfügung.");

        Button infoButton = new Button("Informationen");
        infoButton.setIcon(new Icon(VaadinIcon.INFO_CIRCLE));
        infoButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        infoButton.setTooltipText("Klicke hier für mehr Informationen zu dieser Seite.");
        infoButton.setAriaLabel("Informationen");
        infoButton.addClickListener(e -> {
            HorizontalLayout infoLayout = new HorizontalLayout(new VerticalLayout(infoParagraph, infoCalcParagraph));
            infoLayout.setWidth(100f, Unit.PERCENTAGE);
            infoLayout.setMaxWidth(100f, Unit.PERCENTAGE);

            GeneralRailInsightsDialog dialog = new GeneralRailInsightsDialog();
            dialog.setHeaderTitle("Informationen zur Seite");
            dialog.add(infoLayout);
            dialog.add();
            dialog.open();
        });

        DateTimePicker whenAfter = new DateTimePicker();
        whenAfter.setLabel("Ab wann");
        whenAfter.setTooltipText("Wähle den Zeitpunkt ab wann die Daten im Dashboard angezeigt werden sollen.");
        whenAfter.setValue(LocalDateTime.now().minusDays(1));
        whenAfter.addValueChangeListener(e -> {
            this.whenAfter = whenAfter.getValue();
        });

        DateTimePicker whenBefore = new DateTimePicker();
        whenBefore.setLabel("Bis wann");
        whenBefore.setTooltipText("Wähle den Zeitpunkt bis wann die Daten im Dashboard angezeigt werden sollen.");
        whenBefore.setValue(LocalDateTime.now());
        whenBefore.addValueChangeListener(e -> {
            this.whenBefore = whenBefore.getValue();
        });

        HorizontalLayout mapLayout = new HorizontalLayout(map);
        mapLayout.setHeight(100f, Unit.PERCENTAGE);
        mapLayout.setWidth(100f, Unit.PERCENTAGE);
        mapLayout.setMaxHeight(100f, Unit.PERCENTAGE);
        mapLayout.setMaxWidth(100f, Unit.PERCENTAGE);
        mapLayout.add(map, sidebar);

        HorizontalLayout infoLayout = new HorizontalLayout(infoButton, whenAfter, whenBefore);
        infoLayout.setAlignItems(Alignment.BASELINE);
        VerticalLayout content = new VerticalLayout(infoLayout, mapLayout);
        content.setSizeFull();
        content.setMaxHeight(100f, Unit.PERCENTAGE);
        content.setMaxWidth(100f, Unit.PERCENTAGE);

        cardList.setWidth(100f, Unit.PERCENTAGE);
        cardList.setMaxWidth(100f, Unit.PERCENTAGE);
        cardList.setHeight(100f, Unit.PERCENTAGE);
        cardList.setMaxHeight(100f, Unit.PERCENTAGE);

        setFlexGrow(0);
        setFlexShrink(0);

        add(content);

        configureMap();
        updateCardList();

        setSizeFull();
        setMaxHeight(100f, Unit.PERCENTAGE);
        setMaxWidth(100f, Unit.PERCENTAGE);
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
            button.setWidth(100f, Unit.PERCENTAGE);
            button.setAriaLabel("Klick um auf Karte zu zentrieren, Rechtsklick oder langes Drücken für mehr Option");
            button.addClassNames(LumoUtility.Height.AUTO, LumoUtility.Padding.MEDIUM);
            button.addClickListener(e -> {
                centerMapOn(stop);
            });

            ContextMenu contextMenu = new ContextMenu();
            contextMenu.setTarget(button);
            if (stop.isProvidesFurtherInformation()) {
                contextMenu.addItem("Weitere Informationen", e -> {
                    openFullStopInfo(stop);
                });
            }
            contextMenu.addItem("Ankünfte und Abfahrten", e -> {
                openArrivalDepartureDialog(stop);
            });

            contextMenu.addItem("Dashboard", e -> {
                GeneralRailInsightsDialog dialog = new GeneralRailInsightsDialog();
                dialog.add(createDashboardLayout(stop));
                dialog.setHeaderTitle("Dashboard für " + stop.getStopName());
                dialog.open();
            });

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

    private Component createDashboardLayout(StopDto stop) {
        TripService tripService = VaadinService.getCurrent().getInstantiator().getOrCreate(TripService.class);
        StopService stopService = VaadinService.getCurrent().getInstantiator().getOrCreate(StopService.class);
        DataProviderService dataProviderService = VaadinService.getCurrent().getInstantiator().getOrCreate(DataProviderService.class);

        LocalDateTime from = whenAfter;
        LocalDateTime to = whenBefore;

        Stop fullStop = stopService.findStopByStopId(Long.valueOf(stop.getStopId())).getData();

        List<Trip> tripToEvaluate = tripService.findAllByStopAndPlannedWhenAfterAndWhenBefore(fullStop, from, to).getData();

        DecimalFormat df = new DecimalFormat("#.##");
        df.setGroupingUsed(false);

        double percentageCancelled = ((double) tripToEvaluate.stream().filter(trip -> {
            if (trip.getCancelled() != null) {
                return trip.getCancelled();
            }
            return false;
        }).count() / tripToEvaluate.size());

        // round up to 2 decimal places
        percentageCancelled = (Math.round(percentageCancelled * 100.0) / 100.0) * 100;
        String percentageCancelledFormatted = df.format(percentageCancelled);
        percentageCancelled = Double.parseDouble(percentageCancelledFormatted);

        double percentageDelayed = ((double) tripToEvaluate.stream().filter(trip -> {
            if (trip.getCancelled() == null || !trip.getCancelled()) {
                if (trip.getDelay() != null) {
                    return trip.getDelay() >= 300;
                }
            }
            return false;
        }).count() / tripToEvaluate.size());

        // round up to 2 decimal places
        percentageDelayed = (Math.round(percentageDelayed * 100.0) / 100.0) * 100;
        String percantageDelayedFormatted = df.format(percentageDelayed);
        percentageDelayed = Double.parseDouble(percantageDelayedFormatted);

        double percentageOnTime = (100 - percentageCancelled - percentageDelayed);
        String percentageOnTimeFormatted = df.format(percentageOnTime);
        percentageOnTime = Double.parseDouble(percentageOnTimeFormatted);

        int tripCount = tripToEvaluate.size();

        HashMap<LocalDate, Integer> dailyTripCounts = new HashMap<>();
        HashMap<LocalDate, Integer> dailyTripLongDistanceCounts = new HashMap<>();
        HashMap<LocalDate, Integer> dailyTripRegionalCounts = new HashMap<>();

        HashMap<LocalDateTime, Integer> hourlyTripCounts = new HashMap<>();
        HashMap<LocalDateTime, Integer> hourlyTripLongDistanceCounts = new HashMap<>();
        HashMap<LocalDateTime, Integer> hourlyTripRegionalCounts = new HashMap<>();

        for (Trip trip : tripToEvaluate) {
            LocalDateTime date;
            if (trip.getPlannedWhen() != null) {
                date = trip.getPlannedWhen();
            } else if (trip.getWhen() != null) {
                date = trip.getWhen();
            } else {
                date = trip.getCreatedAt();
            }

            if (date != null) {
                LocalDate localDate = date.toLocalDate();
                if (from.toLocalDate().until(to.toLocalDate()).getDays() > 5) {
                    if (!dailyTripCounts.containsKey(localDate)) {
                        dailyTripCounts.put(localDate, 0); // Füge einen neuen Eintrag hinzu, falls keiner vorhanden ist
                    }
                    dailyTripCounts.put(localDate, dailyTripCounts.get(localDate) + 1);
                    if (trip.getLine().getProduct().contains("national")) {
                        if (!dailyTripLongDistanceCounts.containsKey(localDate)) {
                            dailyTripLongDistanceCounts.put(localDate, 0); // Füge einen neuen Eintrag hinzu, falls keiner vorhanden ist
                        }
                        dailyTripLongDistanceCounts.put(localDate, dailyTripLongDistanceCounts.get(localDate) + 1);
                    } else {
                        if (!dailyTripRegionalCounts.containsKey(localDate)) {
                            dailyTripRegionalCounts.put(localDate, 0); // Füge einen neuen Eintrag hinzu, falls keiner vorhanden ist
                        }
                        dailyTripRegionalCounts.put(localDate, dailyTripRegionalCounts.get(localDate) + 1);
                    }
                } else {
                    date = date.withMinute(0).withSecond(0).withNano(0);
                    if (!hourlyTripCounts.containsKey(date)) {
                        hourlyTripCounts.put(date, 0); // Füge einen neuen Eintrag hinzu, falls keiner vorhanden ist
                    }
                    hourlyTripCounts.put(date, hourlyTripCounts.get(date) + 1);
                    if (trip.getLine().getProduct().contains("national")) {
                        if (!hourlyTripLongDistanceCounts.containsKey(date)) {
                            hourlyTripLongDistanceCounts.put(date, 0); // Füge einen neuen Eintrag hinzu, falls keiner vorhanden ist
                        }
                        hourlyTripLongDistanceCounts.put(date, hourlyTripLongDistanceCounts.get(date) + 1);
                    } else {
                        if (!hourlyTripRegionalCounts.containsKey(date)) {
                            hourlyTripRegionalCounts.put(date, 0); // Füge einen neuen Eintrag hinzu, falls keiner vorhanden ist
                        }
                        hourlyTripRegionalCounts.put(date, hourlyTripRegionalCounts.get(date) + 1);
                    }
                }
            }
        }

        DataSeries dailyTripCountSeries = new DataSeries();
        DataSeries dailyTripLongDistanceCountSeries = new DataSeries();
        DataSeries dailyTripRegionalCountSeries = new DataSeries();

        DataSeries hourlyTripCountSeries = new DataSeries();
        DataSeries hourlyTripLongDistanceCountSeries = new DataSeries();
        DataSeries hourlyTripRegionalCountSeries = new DataSeries();

        // check if we have hourly or daily data fill them and sort them after time
        if (dailyTripCounts.isEmpty()) {
            hourlyTripCounts.entrySet().stream().sorted(java.util.Map.Entry.comparingByKey()).forEach(entry -> {
                hourlyTripCountSeries.add(new DataSeriesItem(entry.getKey().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), entry.getValue()));
            });

            hourlyTripLongDistanceCounts.entrySet().stream().sorted(java.util.Map.Entry.comparingByKey()).forEach(entry -> {
                hourlyTripLongDistanceCountSeries.add(new DataSeriesItem(entry.getKey().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), entry.getValue()));
            });

            hourlyTripRegionalCounts.entrySet().stream().sorted(java.util.Map.Entry.comparingByKey()).forEach(entry -> {
                hourlyTripRegionalCountSeries.add(new DataSeriesItem(entry.getKey().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), entry.getValue()));
            });
        } else {
            dailyTripCounts.entrySet().stream().sorted(java.util.Map.Entry.comparingByKey()).forEach(entry -> {
                dailyTripCountSeries.add(new DataSeriesItem(entry.getKey().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), entry.getValue()));
            });

            dailyTripLongDistanceCounts.entrySet().stream().sorted(java.util.Map.Entry.comparingByKey()).forEach(entry -> {
                dailyTripLongDistanceCountSeries.add(new DataSeriesItem(entry.getKey().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), entry.getValue()));
            });

            dailyTripRegionalCounts.entrySet().stream().sorted(java.util.Map.Entry.comparingByKey()).forEach(entry -> {
                dailyTripRegionalCountSeries.add(new DataSeriesItem(entry.getKey().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), entry.getValue()));
            });
        }

        // convert the percentage values to 2 decimal places
        double percentageNationalTrips = (double) (tripToEvaluate.stream().filter(trip -> trip.getLine().getProduct().equals("national")).toList().size() * 100) / tripToEvaluate.size();
        String percentageNationalTripsFormatted = df.format(percentageNationalTrips);
        percentageNationalTrips = Double.parseDouble(percentageNationalTripsFormatted);

        double percentageNationalExpressTrips = (double) (tripToEvaluate.stream().filter(trip -> trip.getLine().getProduct().equals("nationalExpress")).toList().size() * 100) / tripToEvaluate.size();
        String percentageNationalExpressTripsFormatted = df.format(percentageNationalExpressTrips);
        percentageNationalExpressTrips = Double.parseDouble(percentageNationalExpressTripsFormatted);

        double subUrbanTrips = (double) (tripToEvaluate.stream().filter(trip -> trip.getLine().getProduct().contains("suburban")).toList().size() * 100) / tripToEvaluate.size();
        String subUrbanTripsFormatted = df.format(subUrbanTrips);
        subUrbanTrips = Double.parseDouble(subUrbanTripsFormatted);

        double percentageRegionalExpressTrips = (double) (tripToEvaluate.stream().filter(trip -> trip.getLine().getProduct().equals("regionalExpress")).toList().size() * 100) / tripToEvaluate.size();
        String percentageRegionalExpressTripsFormatted = df.format(percentageRegionalExpressTrips);
        percentageRegionalExpressTrips = Double.parseDouble(percentageRegionalExpressTripsFormatted);

        double percentageRegionalTrips = (double) (tripToEvaluate.stream().filter(trip -> trip.getLine().getProduct().equals("regional")).toList().size() * 100) / tripToEvaluate.size();
        String percentageRegionalTripsFormatted = df.format(percentageRegionalTrips);
        percentageRegionalTrips = Double.parseDouble(percentageRegionalTripsFormatted);

        DataSeries nationalRegionalSeries = new DataSeries();
        nationalRegionalSeries.add(new DataSeriesItem("Fernverkehr", percentageNationalTrips));
        nationalRegionalSeries.add(new DataSeriesItem("Fernverkehr (Express)", percentageNationalExpressTrips));
        nationalRegionalSeries.add(new DataSeriesItem("Nahverkehr (Regional)", percentageRegionalTrips));
        nationalRegionalSeries.add(new DataSeriesItem("Nahverkehr (RegionalExpress)", percentageRegionalExpressTrips));
        nationalRegionalSeries.add(new DataSeriesItem("Nahverkehr (S-Bahn)", subUrbanTrips));

        List<Trip> topDelayedTrips = tripToEvaluate.stream().filter(trip -> trip.getDelay() != null).sorted((o1, o2) -> o2.getDelay().compareTo(o1.getDelay())).limit(10).toList();

        DashboardView dashboardView;

        if (dailyTripCounts.isEmpty()) {
            dashboardView = new DashboardView(tripCount, percentageOnTime, dataProviderService.getStopsPercentageOnTime(),
                    percentageDelayed, dataProviderService.getStopsPercentageDelayed(), percentageCancelled,
                    dataProviderService.getStopsPercentageCancelled(), hourlyTripRegionalCountSeries,
                    hourlyTripLongDistanceCountSeries, hourlyTripCountSeries, nationalRegionalSeries, topDelayedTrips);
        } else {
            dashboardView = new DashboardView(tripCount, percentageOnTime, dataProviderService.getStopsPercentageOnTime(),
                    percentageDelayed, dataProviderService.getStopsPercentageDelayed(), percentageCancelled,
                    dataProviderService.getStopsPercentageCancelled(),
                    dailyTripRegionalCountSeries, dailyTripLongDistanceCountSeries, dailyTripCountSeries,
                    nationalRegionalSeries, topDelayedTrips);
        }

        return dashboardView;
    }

    private void openArrivalDepartureDialog(StopDto stop) {
        StopService stopService = VaadinService.getCurrent().getInstantiator().getOrCreate(StopService.class);
        Stop fullStop = stopService.findStopByStopId(Long.valueOf(stop.getStopId())).getData();

        TripFilter tripFilter = new TripFilter();
        TripDataProvider dataProvider = new TripDataProvider(fullStop, LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
        ConfigurableFilterDataProvider<Trip, Void, TripFilter> filterDataProvider = dataProvider
                .withConfigurableFilter();


        TextField searchField = new TextField();
        searchField.setWidth("75%");
        searchField.setPlaceholder("Suche mit TripId, Linie, oder Richtung");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> {
            tripFilter.setSearchTerm(e.getValue());
            filterDataProvider.setFilter(tripFilter);
        });

        DateTimePicker whenBefore = new DateTimePicker();
        whenBefore.setLabel("Bis wann");
        whenBefore.setValue(LocalDateTime.now().plusDays(1));
        whenBefore.setVisible(true);

        DateTimePicker whenAfter = new DateTimePicker();
        whenAfter.setLabel("Ab wann");
        whenAfter.setValue(LocalDateTime.now());
        whenAfter.setVisible(true);

        whenAfter.addValueChangeListener(e -> {
            dataProvider.updateWhenAfterAndWhenBefore(whenAfter.getValue(), whenBefore.getValue());
            filterDataProvider.refreshAll();
        });

        whenBefore.addValueChangeListener(e -> {
            dataProvider.updateWhenAfterAndWhenBefore(whenAfter.getValue(), whenBefore.getValue());
            filterDataProvider.refreshAll();
        });

        ArrivalDepartureDialog arrivalDepartureDialog = new ArrivalDepartureDialog(fullStop.getName(), searchField, whenAfter, whenBefore);
        arrivalDepartureDialog.setWidth(80f, Unit.PERCENTAGE);
        arrivalDepartureDialog.setHeight(80f, Unit.PERCENTAGE);
        arrivalDepartureDialog.setItems(filterDataProvider);
        arrivalDepartureDialog.open();
    }

    private void openFullStopInfo(StopDto stop) {
        StopService stopService = VaadinService.getCurrent().getInstantiator().getOrCreate(StopService.class);
        Stop fullStop = stopService.findStopByStopId(Long.valueOf(stop.getStopId())).getData();

        StopInfoDialog stopInfoDialog = new StopInfoDialog(fullStop);
        stopInfoDialog.open();

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
