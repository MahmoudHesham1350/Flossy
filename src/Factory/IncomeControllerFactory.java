package Factory;

import java.io.IOException;

import controller.Controller;
import controller.IController;
import models.Income;
import service.IncomeService;
import storage.IncomeStorage;
import models.User;

public class IncomeControllerFactory implements IControllerFactory<Income> {
    User user;

    public IncomeControllerFactory(User loggedInUser) {
        user = loggedInUser;
    }

    @Override
    public IController<Income> createController() throws ClassNotFoundException, IOException, Exception {
        return new Controller<Income>(new IncomeService(), new IncomeStorage(user.getID()));
    }
}