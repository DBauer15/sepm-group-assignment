package at.ac.tuwien.sepm.assignment.groupphase.application.service;

public interface Notifiable {

    /**
     * Called when this {@link Notifiable} is notified of an update
     * @param notifier {@link Class} that has sent the notification
     */
    public void onNotify(Class notifier);
}
