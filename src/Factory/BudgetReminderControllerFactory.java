package Factory;
import java.io.IOException;

import budget.Budget;
import controller.Controller;
import controller.IController;
import reminder.*;
import service.BudgetReminderService;
import storage.ReminderStorage;
import users.User;

public class BudgetReminderControllerFactory implements IControllerFactory<IReminder>{
    User user;
    Budget budget;
    BudgetReminderControllerFactory(User logedinUser, Budget budget){
        this.user = logedinUser;
        this.budget = budget;
    }
    
    @Override
    public IController<IReminder> createController() throws ClassNotFoundException, IOException, Exception {
        return new Controller<IReminder>(new BudgetReminderService(budget), new ReminderStorage(user.getID()));        
    }
    
}
