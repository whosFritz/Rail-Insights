package de.whosfritz.railinsights.ui.components;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.customfield.CustomField;
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
        toggleGroup.setValue(Direction.LIGHT);
        toggleGroup.addValueChangeListener(this::valueChanged);
        add(toggleGroup);
    }


    private void changeThemeAndIcon() {
        getUI().ifPresent(ui -> {
            if (ui.getElement().getThemeList().contains("dark")) {
                ui.getElement().getThemeList().remove("dark");
                toggleGroup.setValue(Direction.LIGHT);
                toggleGroup.setItemEnabledProvider(direction -> switch (direction) {
                    case LIGHT -> false;
                    case DARK -> true;
                });
            } else {
                ui.getElement().getThemeList().add("dark");
                toggleGroup.setValue(Direction.DARK);
                toggleGroup.setItemEnabledProvider(direction -> switch (direction) {
                    case LIGHT -> true;
                    case DARK -> false;
                });
            }
        });
    }

    private void valueChanged(AbstractField.ComponentValueChangeEvent<CustomField<Direction>, Direction> evt) {
        changeThemeAndIcon();
    }

    enum Direction {
        LIGHT, DARK
    }


}
