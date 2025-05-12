package Factory;

import java.io.IOException;

import budget.Budget;
import controller.Controller;
import controller.IController;
import service.BudgetService;
import storage.BudgetStorage;
import users.User;

public class BudgetControllerFactory implements IControllerFactory<Budget>{
    User user;

    public BudgetControllerFactory(User logedinUser){
        this.user = logedinUser;
    }

    @Override
    public IController<Budget> createController() throws ClassNotFoundException, IOException, Exception {
        return new Controller<Budget>(new BudgetService(), new BudgetStorage(user.getID()));
    }
    
}
