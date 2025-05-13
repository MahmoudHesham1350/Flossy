package service;
import reminder.IReminder;
import storage.BudgetStorage;

import java.util.Dictionary;
import java.util.UUID;

import models.Budget;
import models.BudgetReminder;


public class BudgetReminderService implements IService<IReminder> {
    private BudgetStorage budgetStorage;

    public BudgetReminderService(UUID userId) {
        this.budgetStorage = new BudgetStorage(userId);
    }

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