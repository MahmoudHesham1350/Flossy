package service;


import java.util.Dictionary;

import models.Expense;

public class ExpenseService implements IService<Expense> {
    private boolean isValidAmount(double amount) {
        return amount > 0;
    }

    private boolean isValidCategory(String category) {
        return category != null && !category.trim().isEmpty();
    }

    private boolean isValidDate(String date) {
        // Basic validation - can be enhanced with proper date format checking
        return date != null && !date.trim().isEmpty();
    }

    private boolean isValidPaymentMethod(String paymentMethod) {
        return paymentMethod == null || !paymentMethod.trim().isEmpty();
    }

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