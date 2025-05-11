package reminder;
import java.io.IOException;
import java.util.List;
import storage.UltraSimpleStorage;
import java.util.UUID;

public class ReminderStorage extends UltraSimpleStorage<IReminder> {
    private List<IReminder> reminders;

    public ReminderStorage(UUID userID) throws IOException, ClassNotFoundException {
        super(userID + "reminders.dat");
        this.reminders = this.load();
    }

    public void addReminder(IReminder reminder) throws IOException {
        this.reminders.add(reminder);
    }

    public void removeReminder(IReminder reminder) throws IOException {
        this.reminders.remove(reminder);
    }

    public List<IReminder> getAllReminders() throws IOException, ClassNotFoundException {
        return this.reminders;
    }
    
}
