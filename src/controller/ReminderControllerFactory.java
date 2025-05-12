package controller;
import java.io.IOException;

import reminder.*;
import users.User;

public class ReminderControllerFactory implements IControllerFactory<IReminder>{
    User user;
    ReminderControllerFactory(User logedinUser){
        this.user = logedinUser;
    }
    
    @Override
    public IController<IReminder> createController() throws ClassNotFoundException, IOException, Exception {
        ReminderStorage storage = new ReminderStorage(user.getID());
        
    }
    
}
