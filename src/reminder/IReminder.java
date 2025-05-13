package reminder;
import java.io.Serializable;

/**
 * Interface for reminders in the Flossy Personal Finance Manager system.
 * Provides methods for message retrieval, occurrence management, and trigger checks.
 */
public interface IReminder extends Serializable {
    /**
     * Gets the reminder message.
     * @return The reminder message
     */
    String getMessage();
    /**
     * Advances the reminder to the next occurrence (if recurring).
     */
    void nextOccurrence();
    /**
     * Checks if the reminder is triggered (should notify the user).
     * @return True if triggered, false otherwise
     */
    boolean isTriggered();
    /**
     * Checks if the reminder is a one-time reminder.
     * @return True if one-time, false if recurring
     */
    boolean isOneTime();
}