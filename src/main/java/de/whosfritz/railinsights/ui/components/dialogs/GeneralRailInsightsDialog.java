package de.whosfritz.railinsights.ui.components.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;

/**
 * General dialog for rail insights.
 * <p>
 * Should be used as a base class for all dialogs in the rail insights application.
 */
public class GeneralRailInsightsDialog extends Dialog {

    public GeneralRailInsightsDialog() {
        Button closeButton = new Button(new Icon("lumo", "cross"),
                (e) -> close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        getHeader().add(closeButton);
    }

}
