package storage;
import java.util.UUID;

import budget.Budget;

public class BudgetStorage extends UltraSimpleStorage<Budget> {

    public BudgetStorage(UUID userID) {
        super(userID.toString() + "budget.dat");
    }
}
