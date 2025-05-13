package models;
import java.time.LocalDate;

import reminder.IReminder;

/**
 * Represents a budget reminder in the Flossy Personal Finance Manager system.
 * Implements the IReminder interface and is linked to a Budget object.
 */
public class BudgetReminder implements IReminder {
    /** The reminder message/title. */
    private String title;
    /** The associated budget for this reminder. */
    private Budget budget;

    /**
     * Constructs a new BudgetReminder with the given message and budget.
     * @param message The reminder message
     * @param budget The associated budget
     */
    public BudgetReminder(String message, Budget budget) {
        this.title = message;
        this.budget = budget;
    }

    /**
     * Advances the reminder to the next occurrence if the budget is recurring.
     */
    @Override
    public void nextOccurrence() {
        if (budget.getNextDate().isBefore(LocalDate.now())) {
            return;
        }
        budget.nextPeriod();
    }

    /**
     * Returns true if the associated budget is exceeded.
     * @return True if budget is exceeded, false otherwise
     */
    @Override
    public boolean isTriggered() {
        return budget.isExceeded();
    }

    /**
     * Returns true if the associated budget is one-time.
     * @return True if one-time, false if recurring
     */
    @Override
    public boolean isOneTime() {
        return budget.isOneTime();
    }

    /**
     * Gets the reminder message and budget analysis.
     * @return The reminder message
     */
    @Override
    public String getMessage() {
        return "Budget Reminder: " + title + "\n" +
               budget.getAnalysis() + "\n";
    }
}
