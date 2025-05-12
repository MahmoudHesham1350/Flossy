package controller;

import java.io.IOException;

import income.Income;
import income.IncomeService;
import income.IncomeStorage;

public class IncomeControllerFactory implements IControllerFactory<Income> {
    @Override
    public IController<Income> createController() throws ClassNotFoundException, IOException, Exception {
        IncomeStorage incomeStorage = new IncomeStorage();
        return new IncomeController(new IncomeService(), incomeStorage);
    }
}