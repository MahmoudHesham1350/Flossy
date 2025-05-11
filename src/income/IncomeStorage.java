package income;

import storage.UltraSimpleStorage;
import users.User;

import java.io.IOException;
import java.util.List;
public class IncomeStorage extends UltraSimpleStorage<Income>{

    List<Income> income;

    public IncomeStorage() throws IOException, ClassNotFoundException {
        super("Income.dat");
        this.income = this.load();
    }

    public Income getIncomeBySource(String source) {
        for (Income income : income) {
            if (income.getSource().equals(source)) {
                return income;
            }
        }
        return null;
    }
}
