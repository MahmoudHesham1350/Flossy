package Factory;

import controller.IController;
import models.User;
import controller.Controller;
import reminder.IReminder;
import service.PaymentReminderService;
import storage.ReminderStorage;

/**
 * Factory for creating PaymentReminderController instances for a specific user.
 */
public class PaymentReminderControllerFactory implements IControllerFactory<IReminder> {
    /** The user for whom the controller is created. */
    User user;

    /**
     * Constructs a PaymentReminderControllerFactory for the given user.
     * @param logedinUser The user for whom to create the controller
     */
    public PaymentReminderControllerFactory(User logedinUser) {
        this.user = logedinUser;
    }

    /**
     * Creates a PaymentReminderController for the specified user.
     * @return A new PaymentReminderController instance
     * @throws ClassNotFoundException if storage loading fails
     * @throws Exception for other errors
     */
    @Override
    public IController<IReminder> createController() throws ClassNotFoundException, Exception {
        return new Controller<IReminder>(new PaymentReminderService(), new ReminderStorage(user.getID()));
    }
}
