package de.whosfritz.railinsights.ui.components;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinService;
import jakarta.servlet.http.Cookie;
import org.vaadin.addons.taefi.component.ToggleButtonGroup;

public class DarkModeToggle extends VerticalLayout {
    ToggleButtonGroup<Direction> toggleGroup = new ToggleButtonGroup<>();

    public DarkModeToggle() {
        toggleGroup.setId("toggleGroup");
        toggleGroup.setItemIconGenerator(direction -> switch (direction) {
            case LIGHT -> VaadinIcon.SUN_O.create();
            case DARK -> VaadinIcon.MOON_O.create();
        });
        toggleGroup.setItems(Direction.values());
        toggleGroup.setItemLabelGenerator(direction -> "");
        toggleGroup.setValue(Direction.LIGHT);
        toggleGroup.addValueChangeListener(this::valueChanged);
        add(toggleGroup);
    }

    private void changeThemeAndIcon() {
        if (UI.getCurrent().getElement().getThemeList().contains("dark")) {
            UI.getCurrent().getElement().getThemeList().remove("dark");
            toggleGroup.setValue(Direction.LIGHT);
            toggleGroup.setItemEnabledProvider(direction -> switch (direction) {
                case LIGHT -> false;
                case DARK -> true;
            });


            Cookie cookie = new Cookie("RlThemeCookie", "light");
            cookie.setMaxAge(60 * 60 * 24 * 365);
            cookie.setPath("/");
            VaadinService.getCurrentResponse().addCookie(cookie);
        } else {
            UI.getCurrent().getElement().getThemeList().add("dark");
            toggleGroup.setValue(Direction.DARK);
            toggleGroup.setItemEnabledProvider(direction -> switch (direction) {
                case LIGHT -> true;
                case DARK -> false;
            });


            Cookie cookie = new Cookie("RlThemeCookie", "dark");
            cookie.setMaxAge(60 * 60 * 24 * 365);
            cookie.setPath("/");
            VaadinService.getCurrentResponse().addCookie(cookie);
        }
    }

    private void valueChanged(AbstractField.ComponentValueChangeEvent<CustomField<Direction>, Direction> evt) {
        changeThemeAndIcon();
    }

    enum Direction {
        LIGHT, DARK
    }


}
