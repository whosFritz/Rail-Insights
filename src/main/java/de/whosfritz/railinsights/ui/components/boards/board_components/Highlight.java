package de.whosfritz.railinsights.ui.components.boards.board_components;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.Set;

public class Highlight extends VerticalLayout {

    /**
     * Creates a new highlight component.
     *
     * @param title       the title of the highlight
     * @param value       the value of the highlight
     * @param percentage  the percentage of the highlight
     * @param explanation the explanation of the percentage value for the highlight
     */
    public Highlight(String title, String value, Double percentage, String explanation, boolean inverted, String unit) {
        VaadinIcon icon = VaadinIcon.ARROW_UP;
        String prefix = "";
        String theme = "badge";

        if (percentage == 0) {
            prefix = "± ";
            icon = VaadinIcon.MINUS;
            theme += " warning";
        } else if (percentage > 0) {
            prefix = "+ ";
            if (inverted) {
                theme += " success";
            } else {
                theme += " error";
            }
        } else if (percentage < 0) {
            if (inverted) {
                icon = VaadinIcon.ARROW_DOWN;
                theme += " error";
            } else {
                icon = VaadinIcon.ARROW_DOWN;
                theme += " success";
            }
        }

        H2 h2 = new H2(title);
        h2.addClassNames(LumoUtility.FontWeight.NORMAL, LumoUtility.Margin.NONE, LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.XSMALL);

        Span span = new Span(value);
        span.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.XXXLARGE);

        Icon i = icon.create();
        i.addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Padding.XSMALL);

        Span badge = new Span(i, new Span(prefix + percentage + " " + unit + " - " + explanation));
        badge.getElement().getThemeList().add(theme);


        add(h2, span, badge);
        addClassName(LumoUtility.Padding.LARGE);
        setPadding(false);
        setSpacing(false);
    }

    public Highlight(String title, String value) {
        VaadinIcon icon = VaadinIcon.ARROW_UP;
        String prefix = "";
        String theme = "badge";
        H2 h2 = new H2(title);
        h2.addClassNames(LumoUtility.FontWeight.NORMAL, LumoUtility.Margin.NONE, LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.XSMALL);

        Span span = new Span(value);
        span.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.XXXLARGE);

        Icon i = icon.create();
        i.addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Padding.XSMALL);

        add(h2, span);
        addClassName(LumoUtility.Padding.LARGE);
        setPadding(false);
        setSpacing(false);
    }

    public Highlight(String title, Set<String> values) {
        H2 h2 = new H2(title);
        h2.addClassNames(LumoUtility.Margin.Bottom.SMALL);
        add(h2);

        for (String value : values) {
            Paragraph p = new Paragraph(value);
            p.addClassNames(LumoUtility.FontWeight.NORMAL, LumoUtility.TextColor.SECONDARY, LumoUtility.Margin.XSMALL);
            add(p);
        }

        addClassName(LumoUtility.Padding.LARGE);
        setPadding(false);
        setSpacing(false);
    }
}
