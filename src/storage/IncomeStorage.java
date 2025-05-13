package storage;
import java.io.IOException;

import models.Income;
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
