package reminder;

import service.IService;
import budget.Budget;
import budget.BudgetStorage;

import java.io.IOException;
import java.util.Dictionary;
import java.util.UUID;

/**
 * Service for creating and validating BudgetReminder objects.
 */
public class BudgetReminderService implements IService<IReminder> {

    @Override
    public IReminder validate(Dictionary<String, String> data) throws IllegalArgumentException {
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
    
    /**
     * Saves a budget reminder and its associated budget to storage.
     * 
     * @param reminder The BudgetReminder to save
     */
    public void saveReminder(IReminder reminder) {
        if (reminder instanceof BudgetReminder) {
            BudgetReminder budgetReminder = (BudgetReminder) reminder;
            
            // Save the reminder
            reminderStorage.add(budgetReminder);
            try {
                reminderStorage.save(reminderStorage.getAll());
            } catch (IOException e) {
                throw new RuntimeException("Failed to save budget reminder", e);
            }
            
            // TODO: Extract budget and save it to budget storage
            // This would require adding a method to get the budget from BudgetReminder
            // Budget budget = budgetReminder.getBudget();
            // budgetStorage.add(budget);
        } else {
            throw new IllegalArgumentException("Expected a BudgetReminder instance");
        }
    }
}