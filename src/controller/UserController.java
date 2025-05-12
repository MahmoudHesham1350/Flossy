package controller;

import java.util.Dictionary;
import java.util.List;

import service.UserService;
import storage.UserStorage;
import users.*;

public class UserController implements IController<User> {
    private User user;
    private final UserService service;
    private final UserStorage storage;

    public UserController(UserService service, UserStorage storage) throws Exception {
        this.storage = storage;
        this.service = service;
        this.user = null;
    }

    public void loginUser(String email, String password) throws Exception {
        this.user = service.login(email, password);
        if (this.user == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    @Override
    public void create(Dictionary <String, String> data) {
            User user = service.validate(data);
            storage.add(user);

    }

    @Override
    public List<User> getAll() {
        return storage.getAll();
    }

    public boolean isAuthenticated(){
        return this.user != null;
    }

    public User getUser(){
        return this.user;
    }

    public void remove(User user) {
        storage.remove(user);
    }


    
    
}
