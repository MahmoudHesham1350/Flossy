package service;

import storage.IncomeStorage;
import java.util.Dictionary;

import income.Income;

public class IncomeService implements IService<Income> {
    private IncomeStorage incomeStorage;

    public IncomeService() throws Exception {
        this.incomeStorage = new IncomeStorage();
    }

    private boolean isValidAmount(double amount) {
        return amount > 0;
    }

    private boolean isValidSource(String source) {
        return source != null && !source.trim().isEmpty();
    }

    private boolean isValidDate(String date) {
        return date != null && !date.trim().isEmpty();
    }

    @Override
    public Income validate(Dictionary<String, String> data) throws IllegalArgumentException {
        String source = data.get("source");
        String amountStr = data.get("amount");
        String date = data.get("date");

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format");
        }

        if (!isValidSource(source) || !isValidAmount(amount) || !isValidDate(date)) {
            throw new IllegalArgumentException("Invalid income data");
        }

        return new Income(source, amount, date);
    }

    public Income getIncomeBySource(String source) {
        return incomeStorage.getIncomeBySource(source);
    }
}