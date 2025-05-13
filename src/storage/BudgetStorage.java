package storage;
import java.util.UUID;

import models.Budget;

public class BudgetStorage extends UltraSimpleStorage<Budget> {

    public BudgetStorage(UUID userID) {
        super(userID.toString() + "budget.dat");
    }
}
