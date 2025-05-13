package Factory;

import java.io.IOException;

import controller.Controller;
import controller.IController;
import models.Budget;
import models.User;
import service.BudgetService;
import storage.BudgetStorage;

/**
 * Factory for creating BudgetController instances for a specific user.
 */
public class BudgetControllerFactory implements IControllerFactory<Budget>{
    /** The user for whom the controller is created. */
    User user;

    /**
     * Constructs a BudgetControllerFactory for the given user.
     * @param logedinUser The user for whom to create the controller
     */
    public BudgetControllerFactory(User logedinUser){
        this.user = logedinUser;
    }

    /**
     * Creates a BudgetController for the specified user.
     * @return A new BudgetController instance
     * @throws ClassNotFoundException if storage loading fails
     * @throws IOException if storage loading fails
     * @throws Exception for other errors
     */
    @Override
    public IController<Budget> createController() throws ClassNotFoundException, IOException, Exception {
        return new Controller<Budget>(new BudgetService(), new BudgetStorage(user.getID()));
    }
    
}
