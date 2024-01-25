package de.whosfritz.RailInsights.UI.Components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;

public class DarkModeToggle extends Div {
    Button darkmodeButton = new Button(getInitialIcon());

    public DarkModeToggle() {
        darkmodeButton.addClickListener(e -> {
            changeThemeAndIcon();
        });
        add(darkmodeButton);
    }

    private Component getThemeIcon() {
        return VaadinIcon.MOON_O.create();
    }

    private Component getInitialIcon() {
        if (getElement().getThemeList().contains("dark")) {
            return VaadinIcon.MOON_O.create();
        } else {
            return VaadinIcon.SUN_O.create();
        }
    }

    private void changeThemeAndIcon() {
        getUI().ifPresent(ui -> {
            if (ui.getElement().getThemeList().contains("dark")) {
                ui.getElement().getThemeList().remove("dark");
                darkmodeButton.setIcon(VaadinIcon.SUN_O.create());
            } else {
                ui.getElement().getThemeList().add("dark");
                darkmodeButton.setIcon(VaadinIcon.MOON_O.create());
            }
        });
    }
}
