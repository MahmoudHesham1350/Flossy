package Factory;

import java.io.IOException;

import controller.Controller;
import controller.IController;
import models.Expense;
import service.ExpenseService;
import storage.ExpenseStorage;
import models.User;

/**
 * Factory for creating ExpenseController instances for a specific user.
 */
public class ExpenseControllerFactory implements IControllerFactory<Expense> {
    /** The user for whom the controller is created. */
    User user;

    /**
     * Constructs an ExpenseControllerFactory for the given user.
     * @param loggedInUser The user for whom to create the controller
     */
    public ExpenseControllerFactory(User loggedInUser) {
        user = loggedInUser;
    }

    /**
     * Creates an ExpenseController for the specified user.
     * @return A new ExpenseController instance
     * @throws ClassNotFoundException if storage loading fails
     * @throws IOException if storage loading fails
     * @throws Exception for other errors
     */
    @Override
    public IController<Expense> createController() throws ClassNotFoundException, IOException, Exception {
        return new Controller<Expense>(new ExpenseService(), new ExpenseStorage(user.getID()));
    }
    
}
