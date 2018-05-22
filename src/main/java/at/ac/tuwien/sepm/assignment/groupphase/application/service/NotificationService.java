package at.ac.tuwien.sepm.assignment.groupphase.application.service;

public interface NotificationService {

    /**
     * Subscribes to notifications of a certain class
     * @param notifier The class to subscribe
     * @param notifiable {@link Notifiable} Whom to notify in that case
     */
    public void subscribeTo(Class notifier, Notifiable notifiable);

    /**
     * Unsubscribes from notifications of a certain class
     * @param notifier The class from which to unsubscribe
     * @param notifiable {@link Notifiable} Whom to unsubscribe
     */
    public void unsubscribeFrom(Class notifier, Notifiable notifiable);

    /**
     * Notify all {@link Notifiable} that are subscribed to this notifier
     * @param notifier Who sent the notification
     */
    public void notify(Class notifier);
}
