package de.whosfritz.railinsights.ui.components.boards.board_components;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class HighlightSmall extends VerticalLayout {

    public HighlightSmall(String title, String value) {
        VaadinIcon icon = VaadinIcon.ARROW_UP;
        H2 h2 = new H2(title);
        h2.addClassNames(LumoUtility.FontWeight.NORMAL, LumoUtility.Margin.NONE, LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.XSMALL);

        Span span = new Span(value);
        span.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.XLARGE);

        Icon i = icon.create();
        i.addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Padding.XSMALL);

        add(h2, span);
        addClassNames(LumoUtility.Padding.LARGE, LumoUtility.Border.ALL, LumoUtility.BorderColor.CONTRAST_10);
        setPadding(false);
        setSpacing(false);
    }

}
