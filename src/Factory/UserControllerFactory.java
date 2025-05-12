package Factory;

import java.io.IOException;

import controller.IController;
import controller.UserController;
import service.UserService;
import storage.UserStorage;
import users.User;

public class UserControllerFactory implements IControllerFactory<User> {
    @Override
    public IController<User> createController() throws ClassNotFoundException, IOException, Exception {
        UserStorage userStorage = new UserStorage();
        return new UserController(new UserService(userStorage), userStorage);
    }
    
}
