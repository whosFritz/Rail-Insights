package de.whosfritz.RailInsights.UI.Layout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.AnchorTarget;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
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
    Notification cookieNotification = new Notification();

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

    private Component getWrapper() {
        Div wrapper = new Div();
        Text text1 = new Text("Wir benötigen Ihre Zustimmung, bevor Sie unsere Website weiter besuchen können." +
                " Wir verwenden Cookies und andere Technologien auf unserer Webseite. Diese sind essenziell für die Funktion" +
                " der Webseite. Durch Klick auf Akzeptieren sind Sie damit einverstanden." +
                " Mehr Informationen finden Sie in unserer "
        );
        Anchor datenschutzLink = new Anchor("/datenschutzerklaerung", "Datenschutzerklärung.", AnchorTarget.BLANK);
        Paragraph text2 = new Paragraph("Sie können Ihre Auswahl jederzeit unter Ihren Browser-Einstellungen widerrufen oder anpassen.");
        wrapper.add(text1, datenschutzLink, text2);
        wrapper.addClassName(LumoUtility.Padding.MEDIUM);
        wrapper.add(new Button("Akzeptieren", evt -> {
            setCookieConsent();
            cookieNotification.close();
        }));
        return new Scroller(wrapper);
    }

    private VerticalLayout getNavigationBar() {
        VerticalLayout wrapper = new VerticalLayout();
        SideNav mainSideNav = getMainSideNav();
        SideNav subSideNav = getSubSideNav();
        wrapper.add(mainSideNav, subSideNav);
        wrapper.setSizeFull();
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
            cookieNotification.setPosition(Notification.Position.MIDDLE);
            cookieNotification.setDuration(0);
            cookieNotification.add(getWrapper());
            cookieNotification.open();
        }
    }

    private void setCookieConsent() {
        Cookie consentCookie = new Cookie("RlConsentCookie", "true");
        consentCookie.setMaxAge(60 * 60 * 24 * 365);
        consentCookie.setPath("/");
        VaadinService.getCurrentResponse().addCookie(consentCookie);
    }

    private SideNav getMainSideNav() {
        SideNav mainSideNav = new SideNav();
        mainSideNav.addItem(
                createNavItem("Trips", "/trips", VaadinIcon.SUITCASE.create(), LumoUtility.FontSize.MEDIUM),
                createNavItem("Bahnhöfe", "/stations", LineAwesomeIcon.CITY_SOLID.create(), LumoUtility.FontSize.MEDIUM),
                createNavItem("ICE", "/ice", LineAwesomeIcon.TRAIN_SOLID.create(), LumoUtility.FontSize.MEDIUM),
                createNavItem("IC", "/ic", VaadinIcon.TRAIN.create(), LumoUtility.FontSize.MEDIUM),
                createNavItem("Regionalbahn", "/regionalbahn", LineAwesomeIcon.SUBWAY_SOLID.create(), LumoUtility.FontSize.MEDIUM),
                createNavItem("Interaktive Karte", "/map", LineAwesomeIcon.ROUTE_SOLID.create(), LumoUtility.FontSize.MEDIUM)
        );
        mainSideNav.setSizeFull();
        return mainSideNav;
    }

    private SideNav getSubSideNav() {
        SideNav subSideNav = new SideNav();
        subSideNav.addItem(
                createNavItem("About", "https://github.com/whosFritz/Rail-Insights", LineAwesomeIcon.USER_SOLID.create(), LumoUtility.FontSize.XSMALL),
                createNavItem("Impressum", "/impressum", LineAwesomeIcon.INFO_CIRCLE_SOLID.create(), LumoUtility.FontSize.XSMALL),
                createNavItem("Datenschutzerklärung", "/datenschutzerklaerung", LineAwesomeIcon.SHIELD_ALT_SOLID.create(), LumoUtility.FontSize.XSMALL)
        );
        return subSideNav;
    }

    private SideNavItem createNavItem(String title, String route, Component icon, String className) {
        SideNavItem item = new SideNavItem(title, route, icon);
        item.addClassName(className);
        return item;
    }

}
