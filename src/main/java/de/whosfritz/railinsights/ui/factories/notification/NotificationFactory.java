package de.whosfritz.railinsights.ui.factories.notification;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

/**
 * Factory for creating notifications
 */
public class NotificationFactory {

    /**
     * Create a notification with the given type and message
     *
     * @param type    The type of the notification (INFO, SUCCESS, WARNING, ERROR, CRITICAL, DEFAULT)
     * @param message The message of the notification
     * @return The created notification
     */
    public static Notification createwNotification(NotificationTypes type, String message) {
        switch (type) {
            case INFO:
                Notification infoNotification = new Notification(message, 5000);
                infoNotification.setPosition(Notification.Position.BOTTOM_START);
                infoNotification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
                return infoNotification;
            case SUCCESS:
                Notification successNotification = new Notification(message, 5000);
                successNotification.setPosition(Notification.Position.BOTTOM_START);
                successNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                return successNotification;
            case WARNING:
                Notification warningNotification = new Notification(message, 5000);
                warningNotification.setPosition(Notification.Position.BOTTOM_START);
                warningNotification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
                return warningNotification;
            case ERROR:
                Notification errorNotification = new Notification(message, 5000);
                errorNotification.setPosition(Notification.Position.BOTTOM_START);
                errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return errorNotification;
            default:
                return new Notification(message, 5000);
        }
    }

}
