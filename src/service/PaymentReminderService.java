package service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Dictionary;

import models.PaymentReminder;
import reminder.IReminder;

public class PaymentReminderService implements IService<IReminder> {
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