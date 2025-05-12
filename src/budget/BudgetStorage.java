package budget;
import storage.UltraSimpleStorage;

import java.util.UUID;

public class BudgetStorage extends UltraSimpleStorage<Budget> {

    public BudgetStorage(UUID userID) {
        super(userID.toString() + "budget.dat");
    }
}
