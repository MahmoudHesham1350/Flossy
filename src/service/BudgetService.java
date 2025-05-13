package service;
import java.util.Dictionary;

import models.Budget;

public class BudgetService implements IService<Budget> {

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
