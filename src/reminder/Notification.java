package reminder;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a list of triggered reminders and provides notification messages in the Flossy system.
 */
public class Notification {
    /** The list of triggered reminders. */
    private List<IReminder> reminders;

    /**
     * Constructs a new Notification object with an empty list of reminders.
     */
    public Notification() {
        this.reminders = new ArrayList<IReminder>();
    }

    /**
     * Gets the number of triggered reminders.
     * @return The number of reminders
     */
    public int getNumberOfReminders() {
        return reminders.size();
    }

    /**
     * Adds a triggered reminder to the notification list.
     * @param reminder The reminder to add
     */
    public void addReminder(IReminder reminder) {
        reminders.add(reminder);
    }
    
    /**
     * Gets the list of triggered reminders.
     * @return The list of reminders
     */
    public List<IReminder> getReminders(){
        return reminders;
    }

    /**
     * Gets the notification messages for all triggered reminders.
     * @return A list of notification messages
     */
    public List<String> getNotifications() {
        List<String> notifications = new ArrayList<>();
        for (IReminder reminder : reminders) {
            notifications.add(reminder.getMessage());
        }
        return notifications;
    }
}
