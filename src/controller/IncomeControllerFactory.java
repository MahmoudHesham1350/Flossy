package controller;

import java.io.IOException;

import Factory.IControllerFactory;
import income.Income;
import income.IncomeService;
import storage.IncomeStorage;

public class IncomeControllerFactory implements IControllerFactory<Income> {
    @Override
    public IController<Income> createController() throws ClassNotFoundException, IOException, Exception {
        return new IncomeController(new IncomeService(), new IncomeStorage());
    }
}