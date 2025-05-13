package Factory;

import java.io.IOException;

import controller.IController;
import controller.UserController;
import models.User;
import service.UserService;
import storage.UserStorage;

/**
 * Factory for creating UserController instances with the correct service and storage.
 */
public class UserControllerFactory implements IControllerFactory<User> {
    /**
     * Creates a UserController with UserService and UserStorage.
     * @return A new UserController instance
     * @throws ClassNotFoundException if storage loading fails
     * @throws IOException if storage loading fails
     * @throws Exception for other errors
     */
    @Override
    public IController<User> createController() throws ClassNotFoundException, IOException, Exception {
        UserStorage userStorage = new UserStorage();
        return new UserController(new UserService(userStorage), userStorage);
    }
    
}
