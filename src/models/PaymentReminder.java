package models;
import java.time.LocalDate;

import reminder.IReminder;

/**
 * Represents a payment reminder in the Flossy Personal Finance Manager system.
 * Implements the IReminder interface and stores message, date, and recurrence.
 */
public class PaymentReminder implements IReminder {
    /** The reminder message. */
    private String message;
    /** The date for the reminder. */
    private LocalDate reminderDate;
    /** The recurrence period in days (0 for one-time reminders). */
    private int recurrence;

    /**
     * Constructs a new PaymentReminder with the given message and date.
     * @param message The reminder message
     * @param reminderDate The date for the reminder
     */
    public PaymentReminder(String message, LocalDate reminderDate) {
        this.message = message;
        this.reminderDate = reminderDate;
    }

    /**
     * Advances the reminder to the next occurrence if recurring.
     */
    @Override
    public void nextOccurrence() {
        if(reminderDate.isBefore(LocalDate.now())) {
            return;
        }
        LocalDate nextDate = reminderDate.plusDays(recurrence);
        while (nextDate.isBefore(LocalDate.now())) {
            reminderDate = reminderDate.plusDays(recurrence);
        }
    }

    /**
     * Returns true if the reminder is triggered (today or earlier).
     * @return True if triggered, false otherwise
     */
    @Override
    public boolean isTriggered() {
        return reminderDate.isEqual(LocalDate.now()) || reminderDate.isBefore(LocalDate.now());
    }
    
    /**
     * Returns true if this is a one-time reminder.
     * @return True if one-time, false if recurring
     */
    @Override
    public boolean isOneTime() {
        return recurrence == 0;
    }

    /**
     * Gets the reminder message with the date.
     * @return The reminder message
     */
    @Override
    public String getMessage() {
        return message + " on " + reminderDate;
    }
}
