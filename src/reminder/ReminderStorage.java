package reminder;
import java.io.IOException;
import storage.UltraSimpleStorage;
import java.util.UUID;

public class ReminderStorage extends UltraSimpleStorage<IReminder> {

    public ReminderStorage(UUID userID) throws IOException, ClassNotFoundException {
        super(userID + "reminders.dat");
    }

    
}
