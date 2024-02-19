package de.whosfritz.railinsights.ui.components;

import com.vaadin.flow.component.html.Hr;

public class Divider extends Hr {

    public Divider() {
        setClassName("divider");
        getElement().getStyle().setBorder("1px solid #ccc");
    }
}
