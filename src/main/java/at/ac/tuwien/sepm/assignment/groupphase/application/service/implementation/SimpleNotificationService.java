package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.service.Notifiable;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SimpleNotificationService implements NotificationService {

    private Map<Class, List<Notifiable>> notificationMapping;

    public SimpleNotificationService() {
        notificationMapping = new HashMap<>();
    }

    @Override
    public void subscribeTo(Class notifier, Notifiable notifiable) {
        if (notificationMapping.containsKey(notifier)) {
            List<Notifiable> notifiables = notificationMapping.get(notifier);
            if (!notifiables.contains(notifiable)) {
                notifiables.add(notifiable);
            }
        } else {
            notificationMapping.put(notifier, new ArrayList<>(Arrays.asList(notifiable)));
        }
    }

    @Override
    public void unsubscribeFrom(Class notifier, Notifiable notifiable) {
        if (notificationMapping.containsKey(notifier)) {
            notificationMapping.get(notifier).remove(notifiable);
        }
    }

    @Override
    public void notify(Class notifier) {
        if (notificationMapping.containsKey(notifier)) {
            for (Notifiable n : notificationMapping.get(notifier)) {
                n.onNotify();
            }
        }
    }
}
