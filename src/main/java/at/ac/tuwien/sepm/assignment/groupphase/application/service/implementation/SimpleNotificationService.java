package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.service.Notifiable;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.*;

@Service
public class SimpleNotificationService implements NotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private Map<Class, List<Notifiable>> notificationMapping;

    public SimpleNotificationService() {
        notificationMapping = new HashMap<>();
    }

    @Override
    public void subscribeTo(Class notifier, Notifiable notifiable) {
        LOG.debug("{} subscribed to {}", notifiable, notifier);
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
        LOG.debug("{} unsubscribed from {}", notifiable, notifier);
        if (notificationMapping.containsKey(notifier)) {
            notificationMapping.get(notifier).remove(notifiable);
        }
    }

    @Override
    public void notify(Class notifier) {
        LOG.debug("{} sent notification", notifier);
        if (notificationMapping.containsKey(notifier)) {
            for (Notifiable n : notificationMapping.get(notifier)) {
                n.onNotify(notifier);
            }
        }
    }
}
