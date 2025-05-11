package Expense;

import income.Income;
import storage.UltraSimpleStorage;

import java.io.IOException;
import java.util.List;

public class ExpenseStorage extends UltraSimpleStorage<Expense>{
    List<Expense> expense;

    public ExpenseStorage() throws IOException, ClassNotFoundException {
        super("Expense.dat");
        this.expense = this.load();
    }

    public Expense getIncomeByCategory(String category) {
        for (Expense expense : expense) {
            if (expense.getCategory().equals(category)) {
                return expense;
            }
        }
        return null;
    }
}