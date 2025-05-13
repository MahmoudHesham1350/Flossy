package Factory;

import java.io.IOException;

import controller.IController;
import controller.UserController;
import models.User;
import service.UserService;
import storage.UserStorage;

public class UserControllerFactory implements IControllerFactory<User> {
    @Override
    public IController<User> createController() throws ClassNotFoundException, IOException, Exception {
        UserStorage userStorage = new UserStorage();
        return new UserController(new UserService(userStorage), userStorage);
    }
    
}
