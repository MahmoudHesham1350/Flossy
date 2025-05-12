package controller;

import java.util.Dictionary;
import java.util.List;

import income.Income;
import income.IncomeService;
import income.IncomeStorage;

public class IncomeController implements IController<Income> {
    private final IncomeService incomeService;
    private final IncomeStorage incomeStorage;

    public IncomeController(IncomeService service, IncomeStorage storage) {
        this.incomeService = service;
        this.incomeStorage = storage;
    }

    @Override
    public void create(Dictionary<String, String> data) {
        try {
            Income income = incomeService.validate(data);
            incomeStorage.add(income);
            System.out.println("Income added successfully");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    @Override
    public List<Income> getAll() {
        return incomeStorage.getAll();
    }

    public String addIncome(Dictionary<String, String> data) {
        try {
            Income income = incomeService.validate(data);
            incomeStorage.add(income);
            return "Income added successfully";
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "An unexpected error occurred: " + e.getMessage();
        }
    }
}