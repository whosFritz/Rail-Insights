package de.whosfritz.railinsights.ui.components.dialogs;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ButtonFactory {

    public static Button createInfoButton(String labelText, Paragraph p1, Paragraph p2) {

        Button infoButton = new Button(labelText);
        infoButton.setIcon(new Icon(VaadinIcon.INFO_CIRCLE));
        infoButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        infoButton.setTooltipText("Klicke hier für mehr Informationen zu dieser Seite.");
        infoButton.setAriaLabel("Informationen");
        infoButton.addClickListener(e -> {
            HorizontalLayout infoLayout = new HorizontalLayout(new VerticalLayout(p1, p2));
            infoLayout.setWidth(100f, Unit.PERCENTAGE);
            infoLayout.setMaxWidth(100f, Unit.PERCENTAGE);

            GeneralRailInsightsDialog dialog = new GeneralRailInsightsDialog();
            dialog.setHeaderTitle("Informationen zur Seite");
            dialog.add(infoLayout);
            dialog.open();
        });
        return infoButton;
    }
}
