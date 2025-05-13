package models;
import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Represents an income entry in the Flossy Personal Finance Manager system.
 * Stores source, amount, and date of the income.
 */
public class Income implements Serializable {
    /** The source of the income. */
    private String source;
    /** The amount of the income. */
    private double amount;
    /** The date of the income. */
    private String date;

    /**
     * Constructs a new Income with the given source, amount, and date.
     * @param source The source of the income
     * @param amount The amount of the income
     * @param date The date of the income
     */
    public Income(String source, double amount, String date) {
        this.source = source;
        this.amount = amount;
        this.date = date;
    }

    /**
     * Gets the source of the income.
     * @return The source
     */
    public String getSource() {
        return source;
    }

    /**
     * Gets the amount of the income.
     * @return The amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Gets the date of the income.
     * @return The date
     */
    public String getDate() {
        return date;
    }

    /**
     * Updates the income with new values.
     * @param source The new source
     * @param amount The new amount
     * @param date The new date
     */
    public void addIncome(String source, double amount, String date) {
        this.source = source;
        this.amount = amount;
        this.date = date;
    }

    /**
     * Deletes the income by clearing its fields.
     * @param source The source to clear
     * @param amount The amount to clear
     * @param date The date to clear
     */
   public void deleteIncome(String source, double amount, String date) {
        this.source = null;
        this.amount = 0;
        this.date = null;
    }

    /**
     * Updates the income with new values.
     * @param source The new source
     * @param amount The new amount
     * @param date The new date
     */
    public void updateIncome(String source, double amount, String date) {
        this.source = source;
        this.amount = amount;
        this.date = date;
   }

    /**
     * Gets a dictionary of income information (source, amount, date).
     * @return A dictionary with income info
     */
    public Dictionary<String, String> getIncomeInfo() {
        Dictionary<String, String> incomeInfo = new Hashtable<>();
        incomeInfo.put("source", this.source);
        incomeInfo.put("amount", String.valueOf(this.amount));
        incomeInfo.put("date", this.date);
        return incomeInfo;
    }
}
