package de.whosfritz.RailInsights.UI.Layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.whosfritz.RailInsights.UI.Components.EvenBetterDarkModeToggle;
import de.whosfritz.RailInsights.UI.Pages.GeneralView;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class MainView extends AppLayout {

    public MainView() {

        SideNav views = getPrimaryNavigation();

        Scroller scroller = new Scroller(views);
        scroller.addClassName(LumoUtility.Padding.SMALL);

        DrawerToggle drawerToggle = new DrawerToggle();

        RouterLink viewTitle = new RouterLink("Rail Insights", GeneralView.class);
        viewTitle.getStyle().set("font-size", "var(--lumo-font-size-xl)")
                .set("margin", "0");


        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.add(drawerToggle, viewTitle);
        wrapper.setSizeFull();
        wrapper.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout wrapper2 = new HorizontalLayout();
        wrapper2.addClassName(LumoUtility.Margin.MEDIUM);
        EvenBetterDarkModeToggle myToggleButton = new EvenBetterDarkModeToggle();
        wrapper2.add(myToggleButton);
        addToDrawer(scroller);
        addToNavbar(wrapper, wrapper2);
        setPrimarySection(Section.DRAWER);
    }

    private SideNav getPrimaryNavigation() {
        SideNav sideNav = new SideNav();


        sideNav.addItem(
                new SideNavItem("Trips", "/trips",
                        VaadinIcon.SUITCASE.create()),
                new SideNavItem("Bahnhöfe", "/stations",
                        LineAwesomeIcon.CITY_SOLID.create()),
                new SideNavItem("ICE", "/ice",
                        LineAwesomeIcon.TRAIN_SOLID.create()),
                new SideNavItem("IC", "/ic",
                        VaadinIcon.TRAIN.create()),
                new SideNavItem("Regionalbahn", "/regionalbahn",
                        LineAwesomeIcon.SUBWAY_SOLID.create()),
                new SideNavItem("Interaktive Karte", "/map",
                        LineAwesomeIcon.ROUTE_SOLID.create())
        );

//        sideNav.addItem(
//                new SideNavItem("Menüpunkt1", "/m1", VaadinIcon.MENU.create()),
//                new SideNavItem("Menüpunkt2", "/m1", VaadinIcon.MENU.create()),
//                new SideNavItem("Menüpunkt3", "/m1", VaadinIcon.MENU.create()),
//                new SideNavItem("Menüpunkt4", "/m1", VaadinIcon.MENU.create()),
//                new SideNavItem("Menüpunkt5", "/stations", VaadinIcon.MENU.create()),
//                new SideNavItem("Menüpunkt6", "/m1", VaadinIcon.MENU.create()),
//                new SideNavItem("Menüpunkt7", "/m1", VaadinIcon.MENU.create()),
//                new SideNavItem("Menüpunkt8", "/m1", VaadinIcon.MENU.create())
//        );
        return sideNav;
    }
}
