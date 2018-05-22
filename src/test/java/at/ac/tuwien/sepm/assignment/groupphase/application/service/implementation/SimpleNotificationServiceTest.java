package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.service.Notifiable;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.NotificationService;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.BaseTest;
import org.junit.Assert;
import org.junit.Test;

public class SimpleNotificationServiceTest extends BaseTest {

    NotificationService notificationService = new SimpleNotificationService();

    @Test
    public void testSubscribeTo_multipleNotifiables_receiveMultipleNotifications() {
        TestNotifiable notifiable1 = new TestNotifiable();
        TestNotifiable notifiable2 = new TestNotifiable();
        TestNotifiable notifiable3 = new TestNotifiable();

        notificationService.subscribeTo(SimpleNotificationServiceTest.class, notifiable1);
        notificationService.subscribeTo(SimpleNotificationServiceTest.class, notifiable2);
        notificationService.subscribeTo(SimpleNotificationServiceTest.class, notifiable3);

        notificationService.notify(SimpleNotificationServiceTest.class);

        Assert.assertEquals(notifiable1.notification_count, 1);
        Assert.assertEquals(notifiable2.notification_count, 1);
        Assert.assertEquals(notifiable3.notification_count, 1);
    }

    @Test
    public void testSubscribeToAndNotify_default_receiveNotification() {
        TestNotifiable succeedingNotifiable = new TestNotifiable();
        succeedingNotifiable.setFailing(false);
        notificationService.subscribeTo(SimpleNotificationServiceTest.class, succeedingNotifiable);

        notificationService.notify(SimpleNotificationServiceTest.class);

        Assert.assertEquals(succeedingNotifiable.notification_count, 1);
    }

    @Test
    public void testUnsubscribeFromAndNotify_withFailingNotifiable_notReceiveNotification() {
        TestNotifiable notifiable = new TestNotifiable();
        notifiable.setFailing(false);
        notificationService.subscribeTo(SimpleNotificationServiceTest.class, notifiable);
        notificationService.notify(SimpleNotificationServiceTest.class);

        notifiable.setFailing(true);
        notificationService.unsubscribeFrom(SimpleNotificationServiceTest.class, notifiable);
        notificationService.notify(SimpleNotificationServiceTest.class);

        Assert.assertEquals(notifiable.notification_count, 1);
    }


    class TestNotifiable implements Notifiable {
        private boolean failing = false;
        public int notification_count = 0;

        public void setFailing (boolean failing) {
            this.failing = failing;
        }

        @Override
        public void onNotify() {
            notification_count++;
            if (failing) {
                Assert.fail("Notification should not be received.");
            } else {
                Assert.assertEquals(Thread.currentThread().getStackTrace()[2].getClassName(), notificationService.getClass().getName());
            }
        }
    }
}
