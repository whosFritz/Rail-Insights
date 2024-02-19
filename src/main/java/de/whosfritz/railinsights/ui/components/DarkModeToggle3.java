package de.whosfritz.railinsights.ui.components;

import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.whosfritz.railinsights.ui.color_scheme.ThemeUtil;
import de.whosfritz.railinsights.ui.color_scheme.ThemeVariant;
import jakarta.servlet.http.Cookie;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class DarkModeToggle3 extends VerticalLayout {

    public DarkModeToggle3() {
        ToggleButton themeToggleButton = new ToggleButton();
        themeToggleButton.addClickListener(event -> toggleTheme());
        themeToggleButton.setValue(ThemeUtil.getCurrentThemeVariant() == ThemeVariant.DARK);
        themeToggleButton.addClassNames(LumoUtility.Margin.AUTO);

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Icon moon = VaadinIcon.MOON_O.create();
        moon.setSize("20px");
        moon.addClassName(LumoUtility.Margin.AUTO);

        horizontalLayout.add(LineAwesomeIcon.SUN.create(), themeToggleButton, moon);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(horizontalLayout);
    }

    private void toggleTheme() {
        ThemeVariant currentTheme = ThemeUtil.getCurrentThemeVariant();
        if (currentTheme == ThemeVariant.DARK) {
            ThemeUtil.selectThemeVariant(ThemeVariant.LIGHT);

            Cookie cookie = new Cookie("RlThemeCookie", "light");
            cookie.setMaxAge(60 * 60 * 24 * 365);
            cookie.setPath("/");
            VaadinService.getCurrentResponse().addCookie(cookie);

        } else {
            ThemeUtil.selectThemeVariant(ThemeVariant.DARK);

            Cookie cookie = new Cookie("RlThemeCookie", "dark");
            cookie.setMaxAge(60 * 60 * 24 * 365);
            cookie.setPath("/");
            VaadinService.getCurrentResponse().addCookie(cookie);
        }
    }
}