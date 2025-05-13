package reminder;
import java.util.List;

/**
 * Observes reminders, checks if they are triggered, and adds them to notifications in the Flossy system.
 */
public class ReminderListener  {
    /** The notification object to update with triggered reminders. */
    private Notification notification;
    /** The list of reminders to observe. */
    private List<IReminder> reminders;

    /**
     * Constructs a ReminderListener with the given notification and reminders list.
     * @param notification The notification object to update
     * @param reminders The list of reminders to observe
     */
    public ReminderListener(Notification notification, List<IReminder> reminders) {
        this.notification = notification;
        this.reminders = reminders;
    }

    /**
     * Checks all reminders, adds triggered ones to notifications, and advances their occurrence.
     * Removes one-time reminders after they are triggered.
     */
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
