package reminder;
import java.time.LocalDate;

import budget.Budget;

public class BudgetReminder implements IReminder {
    private String title;
    private Budget budget;

    public BudgetReminder(String message, Budget budget) {
        this.title = message;
        this.budget = budget;
    }

    @Override
    public void nextOccurrence() {
        if (budget.getNextDate().isBefore(LocalDate.now())) {
            return;
        }
        budget.nextPeriod();

    }

    @Override
    public boolean isTriggered() {
        return budget.isExceeded();
    }

    @Override
    public boolean isOneTime() {
        return budget.isOneTime();
    }

    @Override
    public String getMessage() {
        return "Budget Reminder: " + title + "\n" +
               budget.getAnalysis() + "\n";
    }
    
}
