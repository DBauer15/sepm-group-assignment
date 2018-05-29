package at.ac.tuwien.sepm.assignment.groupphase.application.service;

public interface Notifiable {

    /**
     * Called when this {@link Notifiable} is notified of an update
     */
    public void onNotify();
}
