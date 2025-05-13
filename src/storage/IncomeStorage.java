package storage;
import java.io.IOException;
import java.util.UUID;

import models.Income;
public class IncomeStorage extends UltraSimpleStorage<Income>{

    public IncomeStorage(UUID userID) throws IOException, ClassNotFoundException {
        super(userID + "Income.dat");
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
