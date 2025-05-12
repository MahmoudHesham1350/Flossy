package Factory;

import controller.IController;
import controller.Controller;
import reminder.IReminder;
import service.PaymentReminderService;
import storage.ReminderStorage;
import users.User;

public class PaymentReminderControllerFactory implements IControllerFactory<IReminder> {
    User user;
    public PaymentReminderControllerFactory(User logedinUser) {
        this.user = logedinUser;
    }
    @Override
    public IController<IReminder> createController() throws ClassNotFoundException, Exception {
        return new Controller<IReminder>(new PaymentReminderService(), new ReminderStorage(user.getID()));
    }
    
}
