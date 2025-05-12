package storage;
import java.io.IOException;
import java.util.UUID;

import reminder.IReminder;

public class ReminderStorage extends UltraSimpleStorage<IReminder> {

    public ReminderStorage(UUID userID) throws IOException, ClassNotFoundException {
        super(userID + "reminders.dat");
    }

    
}
