package service;
import reminder.IReminder;
import java.util.Dictionary;

import models.Budget;
import models.BudgetReminder;


public class BudgetReminderService implements IService<IReminder> {
    Budget budget;

    public BudgetReminderService(Budget budget) {
        this.budget = budget;
    }

    @Override
    public BudgetReminder validate(Dictionary<String, String> data) throws IllegalArgumentException {
        String message = data.get("message");
        if(message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }
        return new BudgetReminder(message, budget);
    }


}