package de.whosfritz.railinsights.ui.components.boards.board_components;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

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
            icon = VaadinIcon.ARROW_UP;
        } else if (percentage < 0) {
            // if its inverted, it means that the lower the value, the better it is if its not inverted, the higher the value, the better it is
            if (!inverted) {
                theme += " success";
            } else {
                theme += " error";
            }
            icon = VaadinIcon.ARROW_DOWN;
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
        addClassNames(LumoUtility.Padding.LARGE, LumoUtility.Border.ALL, LumoUtility.BorderColor.CONTRAST_10);
        setPadding(false);
        setSpacing(false);
    }

    public Highlight(String title, String value) {
        VaadinIcon icon = VaadinIcon.ARROW_UP;
        H2 h2 = new H2(title);
        h2.addClassNames(LumoUtility.FontWeight.NORMAL, LumoUtility.Margin.NONE, LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.XSMALL);

        Span span = new Span(value);
        span.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.XXXLARGE);

        Icon i = icon.create();
        i.addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Padding.XSMALL);

        add(h2, span);
        addClassNames(LumoUtility.Padding.LARGE, LumoUtility.Border.ALL, LumoUtility.BorderColor.CONTRAST_10);
        setPadding(false);
        setSpacing(false);
    }

    public Highlight(String title, String from, String to) {
        H2 h2 = new H2(title);
        h2.addClassNames(LumoUtility.FontWeight.NORMAL, LumoUtility.Margin.NONE, LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.XSMALL);

        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setPadding(false);
        wrapper.setSpacing(false);
        wrapper.setMargin(false);
        wrapper.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        Span fromSpan = new Span(from);
        fromSpan.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.XXXLARGE);
        SvgIcon icon = LineAwesomeIcon.ANGLE_DOUBLE_DOWN_SOLID.create();
        icon.addClassNames(LumoUtility.IconSize.LARGE, LumoUtility.Margin.Vertical.SMALL);
        Span toSpan = new Span(to);
        toSpan.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.XXXLARGE);

        wrapper.add(fromSpan, icon, toSpan);

        add(h2, wrapper);
        addClassNames(LumoUtility.Padding.LARGE, LumoUtility.Border.ALL, LumoUtility.BorderColor.CONTRAST_10);
        setPadding(false);
        setSpacing(false);
    }
}
