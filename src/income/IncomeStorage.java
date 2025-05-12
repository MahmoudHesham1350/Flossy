package income;

import storage.UltraSimpleStorage;
import users.User;

import java.io.IOException;
import java.util.List;
public class IncomeStorage extends UltraSimpleStorage<Income>{

    public IncomeStorage() throws IOException, ClassNotFoundException {
        super("Income.dat");
    }

    public Income getIncomeBySource(String source) {
        for (Income income : this.storage) {
            if (income.getSource().equals(source)) {
                return income;
            }
        }
        return null;
    }
}
