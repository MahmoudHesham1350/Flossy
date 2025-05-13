package models;
import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Represents an expense entry in the Flossy Personal Finance Manager system.
 * Stores category, amount, date, payment method, and recurrence status.
 */
public class Expense implements Serializable {
    /** The category of the expense. */
    private String category;
    /** The amount of the expense. */
    private double amount;
    /** The date of the expense. */
    private String date;
    /** The payment method used for the expense. */
    private String paymentMethod;
    /** Whether the expense is recurring. */
    private boolean isRecurring;

    /**
     * Constructs a new Expense with the given category, amount, and date.
     * @param category The category of the expense
     * @param amount The amount of the expense
     * @param date The date of the expense
     */
    public Expense(String category, double amount, String date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    /**
     * Gets the category of the expense.
     * @return The category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the amount of the expense.
     * @return The amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Gets the date of the expense.
     * @return The date
     */
    public String getDate() {
        return date;
    }

    /**
     * Updates the expense with new values.
     * @param category The new category
     * @param amount The new amount
     * @param date The new date
     */
    public void addExpense(String category, double amount, String date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    /**
     * Deletes the expense by clearing its fields.
     * @param category The category to clear
     * @param amount The amount to clear
     * @param date The date to clear
     */
    public void deleteExpense(String category, double amount, String date) {
        this.category = null;
        this.amount = 0;
        this.date = null;
    }

    /**
     * Updates the expense with new values.
     * @param category The new category
     * @param amount The new amount
     * @param date The new date
     */
    public void updateExpense(String category, double amount, String date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
   }

    /**
     * Sets the payment method for the expense.
     * @param paymentMethod The payment method
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * Sets whether the expense is recurring.
     * @param isRecurring True if recurring, false otherwise
     */
    public void setRecurring(boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    /**
     * Gets the payment method for the expense.
     * @return The payment method
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Returns whether the expense is recurring.
     * @return True if recurring, false otherwise
     */
    public boolean isRecurring() {
        return isRecurring;
    }

    /**
     * Gets a dictionary of expense information (category, amount, date, payment method, recurrence).
     * @return A dictionary with expense info
     */
    public Dictionary<String, String> getExpenseInfo() {
        Dictionary<String, String> expenseInfo = new Hashtable<>();
        expenseInfo.put("category", this.category);
        expenseInfo.put("amount", String.valueOf(this.amount));
        expenseInfo.put("date", this.date);
        expenseInfo.put("paymentMethod", this.paymentMethod);
        expenseInfo.put("isRecurring", String.valueOf(this.isRecurring));
        return expenseInfo;
    }
}
