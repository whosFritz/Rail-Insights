package de.whosfritz.railinsights.ui.factories;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.whosfritz.railinsights.ui.components.dialogs.GeneralRailInsightsDialog;

public class ButtonFactory {

    public static Button createInfoButton(String label, Paragraph infoParagraph, Paragraph infoCalcParagraph) {
        Button infoButton = new Button(label);
        infoButton.setIcon(new Icon(VaadinIcon.INFO_CIRCLE));
        infoButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        infoButton.setAriaLabel("Informationen");
        infoButton.addClickListener(e -> {
            HorizontalLayout infoLayout = new HorizontalLayout(new VerticalLayout(infoParagraph, infoCalcParagraph));
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
