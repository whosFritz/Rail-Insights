package de.whosfritz.railinsights.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinService;
import de.whosfritz.railinsights.ui.color_scheme.ThemeUtil;
import de.whosfritz.railinsights.ui.color_scheme.ThemeVariant;
import jakarta.servlet.http.Cookie;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class DarkModeToggle2 extends VerticalLayout {

    private Button themeToggleButton;

    public DarkModeToggle2() {
        themeToggleButton = new Button(getIconForButton(ThemeUtil.getCurrentThemeVariant()));
        themeToggleButton.addClickListener(event -> toggleTheme());
        add(themeToggleButton);
    }

    private Component getIconForButton(ThemeVariant currentThemeVariant) {
        if (currentThemeVariant == ThemeVariant.DARK) {
            return LineAwesomeIcon.SUN.create();
        } else {
            return VaadinIcon.MOON_O.create();
        }
    }

    private void toggleTheme() {
        ThemeVariant currentTheme = ThemeUtil.getCurrentThemeVariant();
        if (currentTheme == ThemeVariant.DARK) {
            ThemeUtil.selectThemeVariant(ThemeVariant.LIGHT);
            themeToggleButton.setIcon(VaadinIcon.MOON_O.create());

            Cookie cookie = new Cookie("RlThemeCookie", "light");
            cookie.setMaxAge(60 * 60 * 24 * 365);
            cookie.setPath("/");
            VaadinService.getCurrentResponse().addCookie(cookie);
        } else {
            ThemeUtil.selectThemeVariant(ThemeVariant.DARK);
            themeToggleButton.setIcon(LineAwesomeIcon.SUN.create());

            Cookie cookie = new Cookie("RlThemeCookie", "dark");
            cookie.setMaxAge(60 * 60 * 24 * 365);
            cookie.setPath("/");
            VaadinService.getCurrentResponse().addCookie(cookie);
        }
    }
}