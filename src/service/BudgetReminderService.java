package service;
import budget.Budget;
import reminder.BudgetReminder;
import reminder.IReminder;
import java.util.Dictionary;


public class BudgetReminderService implements IService<IReminder> {

    @Override
    public BudgetReminder validate(Dictionary<String, String> data) throws IllegalArgumentException {
        // Extract data
        String title = data.get("title");
        String category = data.get("category");
        String amountStr = data.get("amount");
        String recurrenceStr = data.get("recurrence"); // Optional
        
        // Validate title
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Budget reminder title cannot be empty");
        }
        
        // Validate category
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Budget category cannot be empty");
        }
        
        // Parse and validate amount
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                throw new IllegalArgumentException("Budget amount must be greater than zero");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid budget amount format");
        }
        
        // Parse recurrence (defaulting to 0 for one-time budgets)
        int recurrence = 0;
        if (recurrenceStr != null && !recurrenceStr.isEmpty()) {
            try {
                recurrence = Integer.parseInt(recurrenceStr);
                if (recurrence < 0) {
                    throw new IllegalArgumentException("Recurrence cannot be negative");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Recurrence must be a valid number");
            }
        }
        
        // Create the budget
        Budget budget = new Budget(category, amount, recurrence);
        
        // Create and return the budget reminder
        return new BudgetReminder(title, budget);
    }


}