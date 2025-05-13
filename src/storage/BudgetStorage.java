package storage;
import java.util.UUID;

import models.Budget;

/**
 * Storage class for Budget objects for a specific user in the Flossy Personal Finance Manager system.
 * Stores budgets in '<userID>budget.dat'.
 */
public class BudgetStorage extends UltraSimpleStorage<Budget> {

    /**
     * Constructs a BudgetStorage for the given user ID.
     * @param userID The user's UUID
     */
    public BudgetStorage(UUID userID) {
        super(userID.toString() + "budget.dat");
    }

    /**
     * Gets a budget by its unique identifier.
     * @param id The UUID of the budget
     * @return The Budget object if found, null otherwise
     */
    public Budget getById(UUID id) {
        for (Budget budget : this.storage) {
            if (budget.getId().equals(id)) {
                return budget;
            }
        }
        return null;
    }
}
