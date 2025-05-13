package models;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents a budget for a specific category in the Flossy Personal Finance Manager system.
 * Tracks the budgeted amount, spent amount, recurrence, and provides analysis.
 */
public class Budget implements java.io.Serializable {
    /** The unique identifier for the budget. */
    private final UUID id;
    /** The category this budget applies to. */
    private String category;
    /** The total budgeted amount. */
    private double amount;
    /** The amount spent so far in this period. */
    private double spentAmount;
    /** The recurrence period in days (0 for one-time budgets). */
    private int recurrence;
    /** The date this budget was created or last reset. */
    private LocalDate createdDate;

    /**
     * Constructs a new Budget with the given category, amount, and recurrence.
     * @param category The budget category
     * @param amount The budgeted amount
     * @param recurrence The recurrence period in days (0 for one-time)
     */
    public Budget(String category, double amount, int recurrence) {
        this.id = UUID.randomUUID();
        this.category = category;
        this.amount = amount;
        this.spentAmount = 0.0;
        this.recurrence = recurrence;
        this.createdDate = LocalDate.now();
    }

    /**
     * Constructs a new one-time Budget with the given category and amount.
     * @param category The budget category
     * @param amount The budgeted amount
     */
    public Budget(String category, double amount) {
        this(category, amount, 0);
    }

    /**
     * Gets the unique identifier for this budget.
     * @return The UUID of the budget
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the next date for this budget period.
     * @return The next date as a LocalDate
     */
    public LocalDate getNextDate() {
        return createdDate.plusDays(recurrence);
    }

    /**
     * Advances the budget to the next period (if recurring).
     */
    public void nextPeriod() {
        if (recurrence > 0) {
            createdDate = createdDate.plusDays(recurrence);
        }
    }

    /**
     * Returns true if this is a one-time budget.
     * @return True if one-time, false if recurring
     */
    public boolean isOneTime() {
        return recurrence == 0;
    }

    /**
     * Adds spending to the spent amount for this period.
     * @param amount The amount to add
     */
    public void addSpending(double amount) {
        this.spentAmount += amount;
    }

    /**
     * Resets the spent amount to zero for a new period.
     */
    public void resetSpentAmount() {
        this.spentAmount = 0.0;
    }

    /**
     * Returns true if the spent amount exceeds the budgeted amount.
     * @return True if exceeded, false otherwise
     */
    public boolean isExceeded() {
        return spentAmount > amount;
    }

    /**
     * Returns a summary analysis of the budget status.
     * @return A string summary of the budget
     */
    public String getAnalysis() {
        return "Category: " + category + "\n" +
               "Budgeted Amount: " + amount + "\n" +
               "Spent Amount: " + spentAmount + "\n" +
               (isExceeded() ? "Budget Exceeded!" : "Budget Not Exceeded");
    }
}
