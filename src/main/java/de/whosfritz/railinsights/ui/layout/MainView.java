package de.whosfritz.railinsights.ui.layout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.WebBrowser;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.whosfritz.railinsights.ui.color_scheme.ThemeUtil;
import de.whosfritz.railinsights.ui.color_scheme.ThemeVariant;
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
public class MainView extends AppLayout implements BeforeEnterObserver {
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
        DarkModeToggle darkModeToggle = new DarkModeToggle();
        wrapper2.add(createUpdateBadge());
        wrapper2.add(darkModeToggle);
        addToDrawer(scroller);
        addToNavbar(wrapper, wrapper2);
        setPrimarySection(Section.DRAWER);
        getCookieConsentBanner();
        getCookieAndSetPreferredTheme();
    }

    private void getCookieAndSetPreferredTheme() {
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("RlThemeCookie")) {
                if (cookie.getValue().equals("dark")) {
                    ThemeUtil.selectThemeVariant(ThemeVariant.DARK);
                }
            }
        }
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
        Anchor datenschutzLink = new Anchor("/datenschutzerklaerung", "Datenschutzerklärung.");
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
        wrapper.add(createDrawerContent(), mainSideNav, subSideNav);
        wrapper.setSizeFull();
        return wrapper;
    }

    private Component createDrawerContent() {
        VerticalLayout logoLayout = new VerticalLayout();
        Image logo = new Image(
                getLogoSrc(ThemeUtil.getCurrentThemeVariant()), "Dynamic Theme Demo logo");
        ThemeUtil.addThemeChangedListener(
                UI.getCurrent(),
                e -> logo.setSrc(getLogoSrc(e.getThemeVariant()))
        );
        logo.setWidth(150, Unit.PIXELS);
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(logo);
        return logoLayout;
    }

    private String getLogoSrc(ThemeVariant themeVariant) {
        if (themeVariant == ThemeVariant.DARK) {
            return "images/darkmode.png";
        }
        return "images/lightmode.png";
    }

    /**
     * Retrieves a cookie by its name.
     *
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
     *
     * @return SideNav containing the main navigation items.
     */
    private SideNav getMainSideNav() {
        SideNav mainSideNav = new SideNav();
        mainSideNav.addItem(
                createNavItem("Home", "/", VaadinIcon.HOME.create(), LumoUtility.FontSize.MEDIUM),
                createNavItem("DE-Insight", "/delays", LineAwesomeIcon.EYE.create(), LumoUtility.FontSize.MEDIUM),
                createNavItem("Zugstatistiken", "/zugstatistiken", LineAwesomeIcon.SUBWAY_SOLID.create(), LumoUtility.FontSize.MEDIUM),
                createNavItem("Bahnhöfe", "/stationsView", LineAwesomeIcon.BUILDING.create(), LumoUtility.FontSize.MEDIUM),
                createNavItem("Verbindungsprognose", "/verbindungsprognose", VaadinIcon.SEARCH.create(), LumoUtility.FontSize.MEDIUM),
                createNavItem("CSV-Export", "/csv-export", LineAwesomeIcon.FILE_DOWNLOAD_SOLID.create(), LumoUtility.FontSize.MEDIUM)
        );
        mainSideNav.setSizeFull();
        return mainSideNav;
    }

    /**
     * Creates the sub navigation bar.
     *
     * @return SideNav containing the sub navigation items.
     */
    private SideNav getSubSideNav() {
        SideNav subSideNav = new SideNav();
        subSideNav.addItem(
                createNavItem("About", "https://github.com/whosFritz/Rail-Insights", LineAwesomeIcon.GITHUB.create(), LumoUtility.FontSize.XSMALL),
                createNavItem("Impressum", "/impressum", LineAwesomeIcon.INFO_CIRCLE_SOLID.create(), LumoUtility.FontSize.XSMALL),
                createNavItem("Datenschutzerklärung", "/datenschutzerklaerung", LineAwesomeIcon.SHIELD_ALT_SOLID.create(), LumoUtility.FontSize.XSMALL),
                createNavItem("Security-Policy", "https://github.com/whosFritz/Rail-Insights/blob/master/SECURITY.md", LineAwesomeIcon.LOCK_SOLID.create(), LumoUtility.FontSize.XSMALL),
                createNavItem("API-Dokumentation", "https://github.com/whosFritz/Rail-Insights/tree/master/RailInsights-API-Documentation", LineAwesomeIcon.CODE_SOLID.create(), LumoUtility.FontSize.XSMALL)
        );
        return subSideNav;
    }

    /**
     * Creates the update badge.
     *
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
                notification = NotificationFactory.createNotification(NotificationTypes.INFO, "Wir aktualisieren " +
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
     *
     * @param title     The title of the navigation item.
     * @param route     The route of the navigation item.
     * @param icon      The icon of the navigation item.
     * @param className The class name to be added to the navigation item.
     * @return SideNavItem representing the navigation item.
     */
    private SideNavItem createNavItem(String title, String route, Component icon, String className) {
        icon.addClassNames(LumoUtility.Padding.NONE, LumoUtility.IconSize.SMALL);
        SideNavItem item = new SideNavItem(title, route, icon);
        item.addClassNames(className, LumoUtility.Margin.Vertical.XSMALL);
        return item;
    }

    /**
     * Notify the user if its an mobile device that the application is not optimized for mobile devices.
     *
     * @param beforeEnterEvent The event before the user enters the view.
     */
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        WebBrowser webBrowser = UI.getCurrent().getSession().getBrowser();
        if (webBrowser.isIPhone() || webBrowser.isAndroid() || webBrowser.isWindowsPhone()) {
            Notification notification = NotificationFactory.createNotification(NotificationTypes.WARNING, "Die " +
                    "Anwendung ist nicht für mobile Geräte optimiert. " +
                    "Es wird empfohlen die Anwendung auf einem Desktop-Gerät zu verwenden.");
            notification.open();
        }

    }
}
