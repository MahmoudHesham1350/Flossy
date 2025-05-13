package Factory;
import java.io.IOException;

import controller.Controller;
import controller.IController;
import models.User;
import reminder.*;
import service.BudgetReminderService;
import storage.ReminderStorage;

/**
 * Factory for creating BudgetReminderController instances for a specific user.
 */
public class BudgetReminderControllerFactory implements IControllerFactory<IReminder> {
    /** The user for whom the controller is created. */
    private User user;
    
    /**
     * Constructs a BudgetReminderControllerFactory for the given user.
     * @param loggedInUser The user for whom to create the controller
     */
    public BudgetReminderControllerFactory(User loggedInUser) {
        this.user = loggedInUser;
    }
    
    /**
     * Creates a BudgetReminderController for the specified user.
     * @return A new BudgetReminderController instance
     * @throws ClassNotFoundException if storage loading fails
     * @throws IOException if storage loading fails
     * @throws Exception for other errors
     */
    @Override
    public IController<IReminder> createController() throws ClassNotFoundException, IOException, Exception {
        return new Controller<IReminder>(new BudgetReminderService(user.getID()), new ReminderStorage(user.getID())
        );        
    }
}
