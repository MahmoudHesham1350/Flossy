package service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Dictionary;

import models.PaymentReminder;
import reminder.IReminder;

/**
 * Service for validating and creating PaymentReminder objects in the Flossy Personal Finance Manager system.
 */
public class PaymentReminderService implements IService<IReminder> {
    /**
     * Validates payment reminder data and creates a new PaymentReminder object if valid.
     * @param data The reminder data dictionary
     * @return The created PaymentReminder object
     * @throws IllegalArgumentException if validation fails
     */
    @Override
    public PaymentReminder validate(Dictionary<String, String> data) throws IllegalArgumentException {
        String message = data.get("message");
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }

        String dateString = data.get("date");
        LocalDate date;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            date = LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd.");
        }

        return new PaymentReminder(message, date);
    }

}