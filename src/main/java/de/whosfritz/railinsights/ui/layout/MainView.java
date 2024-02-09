package de.whosfritz.railinsights.ui.layout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.whosfritz.railinsights.ui.components.DarkModeToggle;
import de.whosfritz.railinsights.ui.factories.notification.NotificationFactory;
import de.whosfritz.railinsights.ui.factories.notification.NotificationTypes;
import de.whosfritz.railinsights.ui.services.DataProviderService;
import jakarta.servlet.http.Cookie;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * MainView class extends AppLayout and represents the main layout of the application.
 * It includes the navigation bar, cookie consent banner, and other UI components.
 */
public class MainView extends AppLayout {
    Notification cookieNotification = new Notification();

    public MainView() {

        VerticalLayout views = getNavigationBar();

        Scroller scroller = new Scroller(views);

        DrawerToggle drawerToggle = new DrawerToggle();

        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.add(drawerToggle);
        wrapper.setSizeFull();
        wrapper.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout wrapper2 = new HorizontalLayout();
        wrapper2.addClassName(LumoUtility.Margin.XSMALL);
        DarkModeToggle myToggleButton = new DarkModeToggle();
        wrapper2.add(createUpdateBadge());
        wrapper2.add(myToggleButton);
        addToDrawer(scroller);
        addToNavbar(wrapper, wrapper2);
        setPrimarySection(Section.DRAWER);
        getCookieConsentBanner();
    }

    /**
     * Creates a wrapper component for the cookie consent banner.
     *
     * @return Scroller containing the cookie consent banner.
     */
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

    /**
     * Creates the navigation bar.
     *
     * @return VerticalLayout containing the main and sub navigation bars.
     */
    private VerticalLayout getNavigationBar() {
        VerticalLayout wrapper = new VerticalLayout();
        SideNav mainSideNav = getMainSideNav();
        SideNav subSideNav = getSubSideNav();
        wrapper.add(mainSideNav, subSideNav);
        wrapper.setSizeFull();
        return wrapper;
    }

    /**
     * Retrieves a cookie by its name.
     * @return Cookie if found, null otherwise.
     */
    private Cookie getCookieByName() {
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("RlConsentCookie")) {
                return cookie;
            }
        }
        return null;
    }

    /**
     * Displays the cookie consent banner if the consent cookie is not set or its value is not true.
     */
    private void getCookieConsentBanner() {
        Cookie rlConsentCookie = getCookieByName();
        if (rlConsentCookie == null || (rlConsentCookie.getValue() != null && !rlConsentCookie.getValue().equals("true"))) {
            cookieNotification.setPosition(Notification.Position.MIDDLE);
            cookieNotification.setDuration(0);
            cookieNotification.add(getWrapper());
            cookieNotification.open();
        }
    }

    /**
     * Sets the consent cookie with a value of true and a max age of one year.
     */
    private void setCookieConsent() {
        Cookie consentCookie = new Cookie("RlConsentCookie", "true");
        consentCookie.setMaxAge(60 * 60 * 24 * 365);
        consentCookie.setPath("/");
        VaadinService.getCurrentResponse().addCookie(consentCookie);
    }

    /**
     * Creates the main navigation bar.
     * @return SideNav containing the main navigation items.
     */
    private SideNav getMainSideNav() {
        SideNav mainSideNav = new SideNav();
        mainSideNav.addItem(
                createNavItem("Home", "/", VaadinIcon.HOME.create(), LumoUtility.FontSize.MEDIUM),
                createNavItem("Verspätungen", "/verspätungen", VaadinIcon.TIMER.create(), LumoUtility.FontSize.MEDIUM),
                createNavItem("Ausfälle", "/ausfälle", LineAwesomeIcon.BAN_SOLID.create(), LumoUtility.FontSize.MEDIUM),
                createNavItem("Bahnhöfe", "/bahnhöfe", LineAwesomeIcon.BUILDING.create(), LumoUtility.FontSize.MEDIUM),
                createNavItem("Verbindungsprognose", "/verbindungsprognose", VaadinIcon.TIME_BACKWARD.create(), LumoUtility.FontSize.MEDIUM),
                createNavItem("CSV-Export", "/csv-export", LineAwesomeIcon.FILE_CSV_SOLID.create(), LumoUtility.FontSize.MEDIUM)
        );
        mainSideNav.setSizeFull();
        return mainSideNav;
    }

    /**
     * Creates the sub navigation bar.
     * @return SideNav containing the sub navigation items.
     */
    private SideNav getSubSideNav() {
        SideNav subSideNav = new SideNav();
        subSideNav.addItem(
                createNavItem("About", "https://github.com/whosFritz/Rail-Insights", LineAwesomeIcon.GITHUB.create(), LumoUtility.FontSize.XSMALL),
                createNavItem("Impressum", "/impressum", LineAwesomeIcon.INFO_CIRCLE_SOLID.create(), LumoUtility.FontSize.XSMALL),
                createNavItem("Datenschutzerklärung", "/datenschutzerklaerung", LineAwesomeIcon.SHIELD_ALT_SOLID.create(), LumoUtility.FontSize.XSMALL)
        );
        return subSideNav;
    }

    /**
     * Creates the update badge.
     * @return VerticalLayout containing the update badge.
     */
    private VerticalLayout createUpdateBadge() {
        // get Spring bean
        DataProviderService dataProviderService = VaadinService.getCurrent().getInstantiator().getOrCreate(DataProviderService.class);
        Span span = new Span();
        Notification notification;
        VerticalLayout iconLayout = new VerticalLayout();
        iconLayout.addClassName(LumoUtility.Padding.XSMALL);
        iconLayout.addClassName(LumoUtility.BorderRadius.MEDIUM);
        switch (dataProviderService.getState()) {
            case READY -> {
                iconLayout.add(VaadinIcon.CHECK.create());
                span.getElement().getThemeList().add("badge success pill");
                span.setTitle("Du bist auf dem neusten Stand");
            }
            case PENDING -> {
                iconLayout.add(VaadinIcon.CLOCK.create());
                span.getElement().getThemeList().add("badge pill");
                span.setTitle("Es werden gerade Daten aktualisiert");
                notification = NotificationFactory.createwNotification(NotificationTypes.INFO, "Wir aktualisieren " +
                        "gerade die Daten, aktualisiere die Seite in ein paar Sekunden");
                notification.open();
            }
        }
        span.add(iconLayout);
        VerticalLayout status = new VerticalLayout();
        status.add(span);
        status.setAlignItems(FlexComponent.Alignment.CENTER);
        status.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        status.setAlignSelf(FlexComponent.Alignment.CENTER);

        return status;
    }

    /**
     * Creates a navigation item.
     * @param title The title of the navigation item.
     * @param route The route of the navigation item.
     * @param icon The icon of the navigation item.
     * @param className The class name to be added to the navigation item.
     * @return SideNavItem representing the navigation item.
     */
    private SideNavItem createNavItem(String title, String route, Component icon, String className) {
        SideNavItem item = new SideNavItem(title, route, icon);
        item.addClassName(className);
        return item;
    }

}
