package service;
import reminder.IReminder;
import storage.BudgetStorage;

import java.util.Dictionary;
import java.util.UUID;

import models.Budget;
import models.BudgetReminder;

/**
 * Service for validating and creating BudgetReminder objects in the Flossy Personal Finance Manager system.
 * Links reminders to budgets using BudgetStorage.
 */
public class BudgetReminderService implements IService<IReminder> {
    /** The storage for budgets. */
    private BudgetStorage budgetStorage;

    /**
     * Constructs a BudgetReminderService for the given user ID.
     * @param userId The user's UUID
     */
    public BudgetReminderService(UUID userId) {
        this.budgetStorage = new BudgetStorage(userId);
    }

    /**
     * Validates budget reminder data and creates a new BudgetReminder object if valid.
     * @param data The reminder data dictionary
     * @return The created BudgetReminder object
     * @throws IllegalArgumentException if validation fails
     */
    @Override
    public BudgetReminder validate(Dictionary<String, String> data) throws IllegalArgumentException {
        String message = data.get("message");
        String budgetId = data.get("budgetId");
        
        if(message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }
        if(budgetId == null || budgetId.isEmpty()) {
            throw new IllegalArgumentException("Budget ID cannot be empty");
        }
        
        Budget budget = budgetStorage.getById(UUID.fromString(budgetId));
        if (budget == null) {
            throw new IllegalArgumentException("Budget not found");
        }
        
        return new BudgetReminder(message, budget);
    }
}