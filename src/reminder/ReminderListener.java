package reminder;
import java.util.List;

public class ReminderListener  {
    private Notification notification;
    private List<IReminder> reminders;

    public ReminderListener(Notification notification, List<IReminder> reminders) {
        this.notification = notification;
        this.reminders = reminders;
    }

    public void checkReminders() {
        for (IReminder reminder : reminders) {
            if (reminder.isTriggered()) {
                notification.addReminder(reminder);
                reminder.nextOccurrence();
                if (reminder.isOneTime()) {
                    reminders.remove(reminder);
                }
            }
        }
    }
    
}
