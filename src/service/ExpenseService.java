package service;


import java.util.Dictionary;

import models.Expense;

/**
 * Service for validating and creating Expense objects in the Flossy Personal Finance Manager system.
 */
public class ExpenseService implements IService<Expense> {
    /**
     * Checks if the given amount is valid (greater than 0).
     * @param amount The amount to check
     * @return True if valid, false otherwise
     */
    private boolean isValidAmount(double amount) {
        return amount > 0;
    }

    /**
     * Checks if the given category is valid (not null or empty).
     * @param category The category to check
     * @return True if valid, false otherwise
     */
    private boolean isValidCategory(String category) {
        return category != null && !category.trim().isEmpty();
    }

    /**
     * Checks if the given date is valid (not null or empty).
     * @param date The date to check
     * @return True if valid, false otherwise
     */
    private boolean isValidDate(String date) {
        // Basic validation - can be enhanced with proper date format checking
        return date != null && !date.trim().isEmpty();
    }

    /**
     * Checks if the given payment method is valid (null or not empty).
     * @param paymentMethod The payment method to check
     * @return True if valid, false otherwise
     */
    private boolean isValidPaymentMethod(String paymentMethod) {
        return paymentMethod == null || !paymentMethod.trim().isEmpty();
    }

    /**
     * Validates expense data and creates a new Expense object if valid.
     * @param data The expense data dictionary
     * @return The created Expense object
     * @throws IllegalArgumentException if validation fails
     */
    @Override
    public Expense validate(Dictionary<String, String> data) throws IllegalArgumentException {
        String category = data.get("category");
        String amountStr = data.get("amount");
        String date = data.get("date");
        String paymentMethod = data.get("paymentMethod");
        String isRecurringStr = data.get("isRecurring");

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format");
        }

        if (!isValidCategory(category) || !isValidAmount(amount) || !isValidDate(date)) {
            throw new IllegalArgumentException("Invalid expense data");
        }

        Expense expense = new Expense(category, amount, date);

        if (paymentMethod != null) {
            if (!isValidPaymentMethod(paymentMethod)) {
                throw new IllegalArgumentException("Invalid payment method");
            }
            expense.setPaymentMethod(paymentMethod);
        }

        if (isRecurringStr != null) {
            expense.setRecurring(Boolean.parseBoolean(isRecurringStr));
        }

        return expense;
    }
}