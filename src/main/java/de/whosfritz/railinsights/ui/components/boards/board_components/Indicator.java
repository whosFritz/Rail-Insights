package de.whosfritz.railinsights.ui.components.boards.board_components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@Tag("indicator")
public class Indicator extends VerticalLayout {

    public Indicator(String title, String current, String change, int development) {
        H4 header = new H4(title);
        Paragraph currentValue = new Paragraph(current);

        Icon icon = null;
        Span changeValue = new Span();

        icon = switch (development) {
            case 0 -> {
                changeValue.getElement().getThemeList().add("badge");
                yield VaadinIcon.MINUS_CIRCLE_O.create();
            }
            case 1 -> {
                changeValue.getElement().getThemeList().add("badge success");
                yield VaadinIcon.ARROW_CIRCLE_UP_O.create();
            }
            case 2 -> {
                changeValue.getElement().getThemeList().add("badge error");
                yield VaadinIcon.ARROW_CIRCLE_UP_O.create();
            }
            default -> {
                changeValue.getElement().getThemeList().add("badge contrast");
                yield VaadinIcon.PLUS_MINUS.create();
            }
        };

        changeValue.add(icon, new Paragraph(change));

        add(header, currentValue, changeValue);
    }

    public Indicator(String title, String current) {
        H4 header = new H4(title);
        Paragraph paragraph = new Paragraph(current);
        add(header, paragraph);
    }
}