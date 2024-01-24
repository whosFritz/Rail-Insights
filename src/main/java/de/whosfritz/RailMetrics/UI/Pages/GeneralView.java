package de.whosfritz.RailMetrics.UI.Pages;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.whosfritz.RailMetrics.UI.Layout.MainView;

@Route(value = "/", layout = MainView.class)
public class GeneralView extends VerticalLayout {

    public GeneralView() {

        add(new H1("This is the GeneralDashboard"));

        Notification notification = Notification
                .show("Warning: Application");
        notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.setDuration(0);

        Notification notification2 = Notification
                .show("Error: Application");
        notification2.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification2.setPosition(Notification.Position.MIDDLE);
        notification2.setDuration(0);

        Notification notification3 = Notification
                .show("Success: Application");
        notification3.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification3.setPosition(Notification.Position.MIDDLE);
        notification3.setDuration(0);
            
    }
}
