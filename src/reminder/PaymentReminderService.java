package reminder;

import service.IService;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Dictionary;
import java.util.UUID;

/**
 * Service for creating and validating PaymentReminder objects.
 */
public class PaymentReminderService implements IService<IReminder> {
    private final ReminderStorage reminderStorage;
    
    /**
     * Creates a new PaymentReminderService with storage for the given user.
     * 
     * @param userID ID of the user who owns these reminders
     * @throws IOException if there's an I/O error
     * @throws ClassNotFoundException if a class is not found during loading
     */
    public PaymentReminderService(UUID userID) throws IOException, ClassNotFoundException {
        this.reminderStorage = new ReminderStorage(userID);
    }

    /**
     * Validates and creates a PaymentReminder from provided data.
     * 
     * @param data Dictionary containing "message", "date", and optionally "recurrence"
     * @return Created PaymentReminder
     * @throws IllegalArgumentException if data is invalid
     */
    @Override
    public IReminder validate(Dictionary<String, String> data) throws IllegalArgumentException {
        // Extract data
        String message = data.get("message");
        String dateStr = data.get("date");
        String recurrenceStr = data.get("recurrence"); // Optional
        
        // Validate message
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Reminder message cannot be empty");
        }
        
        // Parse and validate date
        LocalDate reminderDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            reminderDate = LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd");
        }
        
        if (reminderDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Reminder date cannot be in the past");
        }
        
        // Create the reminder - this is a simplified version
        PaymentReminder reminder = new PaymentReminder(message, reminderDate);
        
        // Set recurrence if provided
        if (recurrenceStr != null && !recurrenceStr.isEmpty()) {
            try {
                // TODO: Add field or method to set recurrence in PaymentReminder
                // For now, PaymentReminder doesn't have a setter for recurrence
                // reminder.setRecurrence(Integer.parseInt(recurrenceStr));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Recurrence must be a valid number");
            }
        }
        
        return reminder;
    }
    
    /**
     * Saves a reminder to storage.
     * 
     * @param reminder The reminder to save
     */
    public void saveReminder(IReminder reminder) {
        reminderStorage.add(reminder);
        try {
            reminderStorage.save(reminderStorage.getAll());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save reminder", e);
        }
    }
}