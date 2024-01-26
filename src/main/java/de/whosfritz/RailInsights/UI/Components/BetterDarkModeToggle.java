package de.whosfritz.RailInsights.UI.Components;

import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.html.Div;

public class BetterDarkModeToggle extends Div {
    ToggleButton darkModeToggle = new ToggleButton();

    public BetterDarkModeToggle() {
        darkModeToggle.addValueChangeListener(evt -> {
            changeThemeAndIcon();
        });
        add(darkModeToggle);
    }

    private String getInitialLabel() {
        if (getElement().getThemeList().contains("dark")) {
            return "Light mode";
        } else {
            return "Dark mode";
        }
    }

    private void changeThemeAndIcon() {
        getUI().ifPresent(ui -> {
            if (ui.getElement().getThemeList().contains("dark")) {
                ui.getElement().getThemeList().remove("dark");
//                darkModeToggle.setLabel("Dark mode");
            } else {
                ui.getElement().getThemeList().add("dark");
//                darkModeToggle.setLabel("Light mode");
            }
        });
    }
}
