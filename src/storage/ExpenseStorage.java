package storage;

import java.io.IOException;
import java.util.UUID;

import models.Expense;

/**
 * Storage class for Expense objects for a specific user in the Flossy Personal Finance Manager system.
 * Stores expenses in '<userID>Expense.dat'.
 */
public class ExpenseStorage extends UltraSimpleStorage<Expense>{

    /**
     * Constructs an ExpenseStorage for the given user ID.
     * @param userID The user's UUID
     * @throws IOException if file access fails
     * @throws ClassNotFoundException if deserialization fails
     */
    public ExpenseStorage(UUID userID) throws IOException, ClassNotFoundException {
        super(userID + "Expense.dat");
    }

    /**
     * Gets an expense by its category.
     * @param category The category to search for
     * @return The Expense object if found, null otherwise
     */
    public Expense getIncomeByCategory(String category) {
        for (Expense expense : this.storage) {
            if (expense.getCategory().equals(category)) {
                return expense;
            }
        }
        return null;
    }
}