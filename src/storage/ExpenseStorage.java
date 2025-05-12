package storage;

import java.io.IOException;

import Expense.Expense;

public class ExpenseStorage extends UltraSimpleStorage<Expense>{

    public ExpenseStorage() throws IOException, ClassNotFoundException {
        super("Expense.dat");
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