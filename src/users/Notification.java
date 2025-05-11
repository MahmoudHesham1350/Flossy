package users;

import java.util.List;
import reminder.IReminder;

public class Notification {
    private List<IReminder> reminders;

    Notification() {
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

}
