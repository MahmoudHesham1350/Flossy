package controller;

import java.io.IOException;

import budget.Budget;
import budget.BudgetService;
import budget.BudgetStorage;
import users.User;

public class BudgetControllerFactory implements IControllerFactory<Budget>{
    User user;

    BudgetControllerFactory(User logedinUser){
        this.user = logedinUser;
    }

    @Override
    public IController<Budget> createController() throws ClassNotFoundException, IOException, Exception {
        return new Controller<Budget>(new BudgetService(), new BudgetStorage(user.getID()));
    }
    
}
