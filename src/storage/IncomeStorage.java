package storage;
import java.io.IOException;
import java.util.UUID;

import models.Income;

/**
 * Storage class for Income objects for a specific user in the Flossy Personal Finance Manager system.
 * Stores incomes in '<userID>Income.dat'.
 */
public class IncomeStorage extends UltraSimpleStorage<Income>{

    /**
     * Constructs an IncomeStorage for the given user ID.
     * @param userID The user's UUID
     * @throws IOException if file access fails
     * @throws ClassNotFoundException if deserialization fails
     */
    public IncomeStorage(UUID userID) throws IOException, ClassNotFoundException {
        super(userID + "Income.dat");
    }

    /**
     * Gets an income by its source.
     * @param source The source to search for
     * @return The Income object if found, null otherwise
     */
    public Income getIncomeBySource(String source) {
        for (Income income : this.storage) {
            if (income.getSource().equals(source)) {
                return income;
            }
        }
        return null;
    }
}
