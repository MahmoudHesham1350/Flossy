package Factory;

import java.io.IOException;

import controller.Controller;
import controller.IController;
import models.Income;
import service.IncomeService;
import storage.IncomeStorage;
import models.User;

/**
 * Factory for creating IncomeController instances for a specific user.
 */
public class IncomeControllerFactory implements IControllerFactory<Income> {
    /** The user for whom the controller is created. */
    User user;

    /**
     * Constructs an IncomeControllerFactory for the given user.
     * @param loggedInUser The user for whom to create the controller
     */
    public IncomeControllerFactory(User loggedInUser) {
        user = loggedInUser;
    }

    /**
     * Creates an IncomeController for the specified user.
     * @return A new IncomeController instance
     * @throws ClassNotFoundException if storage loading fails
     * @throws IOException if storage loading fails
     * @throws Exception for other errors
     */
    @Override
    public IController<Income> createController() throws ClassNotFoundException, IOException, Exception {
        return new Controller<Income>(new IncomeService(), new IncomeStorage(user.getID()));
    }
}