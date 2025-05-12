package controller;

import java.io.IOException;

import storage.UltraSimpleStorage;
import users.User;
import users.UserService;
import users.UserStorage;

public class UserControllerFactory implements IControllerFactory<User> {
    @Override
    public IController<User> createController() throws ClassNotFoundException, IOException, Exception {
        UserStorage userStorage = new UserStorage();
        return new UserController(new UserService(userStorage), userStorage);
    }
    
}
