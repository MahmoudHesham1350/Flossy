package service;
import java.util.Dictionary;

import models.Budget;

/**
 * Service for validating and creating Budget objects in the Flossy Personal Finance Manager system.
 */
public class BudgetService implements IService<Budget> {

    /**
     * Validates budget data and creates a new Budget object if valid.
     * @param data The budget data dictionary
     * @return The created Budget object
     * @throws IllegalArgumentException if validation fails
     */
    @Override
    public Budget validate(Dictionary <String, String> data) {
        String category = data.get("category");
        double amount = Double.parseDouble(data.get("amount"));
        int recurrence = Integer.parseInt(data.get("recurrence"));

        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (recurrence < 0) {
            throw new IllegalArgumentException("Recurrence cannot be negative");
        }

        return new Budget(category, amount, recurrence);
    }

    
}
