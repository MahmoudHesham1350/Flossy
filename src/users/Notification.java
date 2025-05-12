package users;

import java.util.ArrayList;
import java.util.List;
import reminder.IReminder;

public class Notification {
    private List<IReminder> reminders;

    Notification() {
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

}
