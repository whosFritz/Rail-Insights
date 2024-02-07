package de.whosfritz.railinsights.ui.components;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
        toggleGroup.setValue(getInitialIcon());
        toggleGroup.addValueChangeListener(evt -> {
            changeThemeAndIcon();
        });
        add(toggleGroup);
    }

    private Direction getInitialIcon() {
        if (getElement().getThemeList().contains("dark")) {
            return Direction.DARK;
        } else {
            return Direction.LIGHT;
        }
    }

    private void changeThemeAndIcon() {
        getUI().ifPresent(ui -> {
            if (ui.getElement().getThemeList().contains("dark")) {
                ui.getElement().getThemeList().remove("dark");
                toggleGroup.setValue(Direction.LIGHT);
            } else {
                ui.getElement().getThemeList().add("dark");
                toggleGroup.setValue(Direction.DARK);
            }
        });
    }

    enum Direction {
        LIGHT, DARK
    }


}
