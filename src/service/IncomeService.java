package service;
import java.util.Dictionary;

import models.Income;

/**
 * Service for validating and creating Income objects in the Flossy Personal Finance Manager system.
 */
public class IncomeService implements IService<Income> {

    /**
     * Checks if the given amount is valid (greater than 0).
     * @param amount The amount to check
     * @return True if valid, false otherwise
     */
    private boolean isValidAmount(double amount) {
        return amount > 0;
    }

    /**
     * Checks if the given source is valid (not null or empty).
     * @param source The source to check
     * @return True if valid, false otherwise
     */
    private boolean isValidSource(String source) {
        return source != null && !source.trim().isEmpty();
    }

    /**
     * Checks if the given date is valid (not null or empty).
     * @param date The date to check
     * @return True if valid, false otherwise
     */
    private boolean isValidDate(String date) {
        return date != null && !date.trim().isEmpty();
    }

    /**
     * Validates income data and creates a new Income object if valid.
     * @param data The income data dictionary
     * @return The created Income object
     * @throws IllegalArgumentException if validation fails
     */
    @Override
    public Income validate(Dictionary<String, String> data) throws IllegalArgumentException {
        String source = data.get("source");
        String amountStr = data.get("amount");
        String date = data.get("date");

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format");
        }

        if (!isValidSource(source) || !isValidAmount(amount) || !isValidDate(date)) {
            throw new IllegalArgumentException("Invalid income data");
        }

        return new Income(source, amount, date);
    }

}