package Factory;
import java.io.IOException;

import controller.Controller;
import controller.IController;
import models.User;
import reminder.*;
import service.BudgetReminderService;
import storage.ReminderStorage;

public class BudgetReminderControllerFactory implements IControllerFactory<IReminder> {
    private User user;
    
    public BudgetReminderControllerFactory(User loggedInUser) {
        this.user = loggedInUser;
    }
    
    @Override
    public IController<IReminder> createController() throws ClassNotFoundException, IOException, Exception {
        return new Controller<IReminder>(new BudgetReminderService(user.getID()), new ReminderStorage(user.getID())
        );        
    }
}
