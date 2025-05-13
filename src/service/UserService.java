package service;
import storage.UserStorage;

import java.util.Dictionary;

import models.User;

public class UserService implements IService<User> {
    UserStorage userStorage;


    public UserService(UserStorage storage) throws Exception {
        this.userStorage = storage;
    }

    private boolean isValidEmail(String email) {
        if(this.userStorage.getUserByEmail(email) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        return email.contains("@");
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    @Override
    public User validate(Dictionary<String, String> data) throws IllegalArgumentException {
        String email = data.get("email");
        String username = data.get("username");
        String password = data.get("password");
        String phoneNumber = data.get("phoneNumber");

        if (!isValidEmail(email) || username == null || !isValidPassword(password)) {
            throw new IllegalArgumentException("Invalid user data");
        }
        try {
            User existingUser = userStorage.getUserByEmail(email);
            if (existingUser != null) {
                throw new IllegalArgumentException("Email already exists");
            }
            else {
                User newUser = new User(email, username, password, phoneNumber);
                return newUser;
            }
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Error creating user", e);
        }
         catch (Exception e) {
            throw new RuntimeException("Error creating user", e);
        }
    }

    public User login(String email, String password) throws IllegalArgumentException {
        User user = userStorage.getUserByEmail(email);
        if (user != null && user.checkPassword(password)) {
            return user;
        }
        throw new IllegalArgumentException("Invalid email or password");
    }
}
