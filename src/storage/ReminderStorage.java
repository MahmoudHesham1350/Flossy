package storage;
import java.io.IOException;
import java.util.UUID;

import reminder.IReminder;

/**
 * Storage class for Reminder objects for a specific user in the Flossy Personal Finance Manager system.
 * Stores reminders in '<userID>reminders.dat'.
 */
public class ReminderStorage extends UltraSimpleStorage<IReminder> {

    /**
     * Constructs a ReminderStorage for the given user ID.
     * @param userID The user's UUID
     * @throws IOException if file access fails
     * @throws ClassNotFoundException if deserialization fails
     */
    public ReminderStorage(UUID userID) throws IOException, ClassNotFoundException {
        super(userID + "reminders.dat");
    }

    
}
