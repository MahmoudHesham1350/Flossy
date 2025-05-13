package Factory;

import java.io.IOException;

import controller.Controller;
import controller.IController;
import models.Expense;
import service.ExpenseService;
import storage.ExpenseStorage;
import models.User;

public class ExpenseControllerFactory implements IControllerFactory<Expense> {
    User user;

    public ExpenseControllerFactory(User loggedInUser) {
        user = loggedInUser;
    }

    @Override
    public IController<Expense> createController() throws ClassNotFoundException, IOException, Exception {
        return new Controller<Expense>(new ExpenseService(), new ExpenseStorage(user.getID()));
    }
    
}
