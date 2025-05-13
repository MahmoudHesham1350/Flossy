package storage;

import java.io.IOException;
import java.util.UUID;

import models.Expense;

public class ExpenseStorage extends UltraSimpleStorage<Expense>{

    public ExpenseStorage(UUID userID) throws IOException, ClassNotFoundException {
        super(userID + "Expense.dat");
    }

    public Expense getIncomeByCategory(String category) {
        for (Expense expense : this.storage) {
            if (expense.getCategory().equals(category)) {
                return expense;
            }
        }
        return null;
    }
}