package storage;
import java.util.UUID;

import models.Budget;

public class BudgetStorage extends UltraSimpleStorage<Budget> {

    public BudgetStorage(UUID userID) {
        super(userID.toString() + "budget.dat");
    }

    public Budget getById(UUID id) {
        for (Budget budget : this.storage) {
            if (budget.getId().equals(id)) {
                return budget;
            }
        }
        return null;
    }
}
