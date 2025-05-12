package budget;
import storage.UltraSimpleStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BudgetStorage extends UltraSimpleStorage<Budget> {
    private List<Budget> budgets;

    public BudgetStorage(UUID userID) {
        super(userID.toString() + "budget.dat");
        try {
            this.budgets = this.load();
        } catch (Exception e) {
            System.out.println("No previous budget data found. Starting fresh.");
            this.budgets = new ArrayList<>();
        }
    }

    public void addBudget(Budget budget) {
        this.budgets.add(budget);
    }

    public void removeBudget(Budget budget) {
        this.budgets.remove(budget);
    }
    public List<Budget> getAllBudgets() {
        return this.budgets;
    }

    
    
}
