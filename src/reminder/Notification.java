package reminder;

import java.util.ArrayList;
import java.util.List;

public class Notification {
    private List<IReminder> reminders;

    public Notification() {
        this.reminders = new ArrayList<IReminder>();
    }

    public int getNumberOfReminders() {
        return reminders.size();
    }

    public void addReminder(IReminder reminder) {
        reminders.add(reminder);
    }
    
    public List<IReminder> getReminders(){
        return reminders;
    }

    public List<String> getNotifications() {
        List<String> notifications = new ArrayList<>();
        for (IReminder reminder : reminders) {
            notifications.add(reminder.getMessage());
        }
        return notifications;
    }

}
