package de.whosfritz.RailInsights.UI.Layout;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.AnchorTarget;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.whosfritz.RailInsights.UI.Components.EvenBetterDarkModeToggle;
import de.whosfritz.RailInsights.UI.Pages.GeneralView;
import jakarta.servlet.http.Cookie;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class MainView extends AppLayout {

    public MainView() {
        VerticalLayout views = getNavigationBar();

        Scroller scroller = new Scroller(views);

        DrawerToggle drawerToggle = new DrawerToggle();

        RouterLink viewTitle = new RouterLink("Rail Insights", GeneralView.class);
        viewTitle.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.BOLD);

        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.add(drawerToggle, viewTitle);
        wrapper.setSizeFull();
        wrapper.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout wrapper2 = new HorizontalLayout();
        wrapper2.addClassName(LumoUtility.Margin.XSMALL);
        EvenBetterDarkModeToggle myToggleButton = new EvenBetterDarkModeToggle();
        wrapper2.add(myToggleButton);
        addToDrawer(scroller);
        addToNavbar(wrapper, wrapper2);
        setPrimarySection(Section.DRAWER);
        getCookieConsentBanner();
    }

    private static Div getWrapper() {
        Div wrapper = new Div();
        Text text1 = new Text("Wir benötigen Ihre Zustimmung, bevor Sie unsere Website weiter besuchen können." +
                " Wenn Sie unter 16 Jahre alt sind und Ihre Zustimmung zu freiwilligen Diensten geben möchten," +
                " müssen Sie Ihre Erziehungsberechtigten um Erlaubnis bitten. Wir verwenden Cookies und andere" +
                " Technologien auf unserer Webseite. Einige von ihnen sind essenziell, während andere uns helfen," +
                " diese Webseite und Ihre Erfahrung zu verbessern. Personenbezogene Daten können verarbeitet werden" +
                " (z. B. IP-Adressen), z. B. für personalisierte Anzeigen und Inhalte oder Anzeigen- und Inhaltsmessung." +
                " Weitere Informationen über die Verwendung Ihrer Daten finden Sie in unserer "
        );
        Anchor datenschutzLink = new Anchor("/datenschutzerklaerung", "Datenschutzerklärung", AnchorTarget.BLANK);
        Text text2 = new Text(". Sie können Ihre Auswahl jederzeit unter Ihren Browser-Einstellungen widerrufen oder anpassen.");
        wrapper.add(text1, datenschutzLink, text2);
        return wrapper;
    }

    private VerticalLayout getNavigationBar() {
        VerticalLayout wrapper = new VerticalLayout();
        SideNav mainSideNav = new SideNav();
        mainSideNav.addItem(
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
        SideNav subSideNav = new SideNav();
        subSideNav.addItem(
                new SideNavItem("Impressum", "/impressum",
                        LineAwesomeIcon.INFO_CIRCLE_SOLID.create()),
                new SideNavItem("Datenschutzerklärung", "/datenschutzerklaerung",
                        LineAwesomeIcon.SHIELD_ALT_SOLID.create())
        );
        wrapper.add(mainSideNav, subSideNav);
        wrapper.setSizeFull();
        mainSideNav.setSizeFull();
        return wrapper;
    }

    private Cookie getCookieByName() {
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("RlConsentCookie")) {
                return cookie;
            }
        }
        return null;
    }

    private void getCookieConsentBanner() {
        Cookie rlConsentCookie = getCookieByName();
        if (rlConsentCookie == null || (rlConsentCookie.getValue() != null && !rlConsentCookie.getValue().equals("true"))) {
            Notification cookieNotification = new Notification();
            cookieNotification.setPosition(Notification.Position.BOTTOM_STRETCH);
            cookieNotification.setDuration(0);
            Div wrapper = getWrapper();
            cookieNotification.add(wrapper);
            cookieNotification.add(new Button("Akzeptieren", evt -> {
                Cookie consentCookie = new Cookie("RlConsentCookie", "true");
                consentCookie.setMaxAge(60 * 60 * 24 * 365);
                consentCookie.setPath("/");
                VaadinService.getCurrentResponse().addCookie(consentCookie);
                cookieNotification.close();
            }));
            cookieNotification.open();
        }
    }
}

