package Factory;

import java.io.IOException;

import controller.Controller;
import controller.IController;
import models.Budget;
import models.User;
import service.BudgetService;
import storage.BudgetStorage;

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
