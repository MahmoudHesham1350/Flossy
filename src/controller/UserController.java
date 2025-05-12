package controller;

import java.util.Dictionary;
import java.util.List;

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
        if(this.user != null) {
            System.out.println("User logged in successfully");
        } else {
            System.out.println("Invalid email or password");
        }
    }

    @Override
    public void create(Dictionary <String, String> data) {
        try {
            User user = service.validate(data);
            storage.add(user);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
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


    
    
}
