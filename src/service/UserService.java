package service;
import storage.UserStorage;

import java.util.Dictionary;

import models.User;

/**
 * Service for validating and managing users in the Flossy Personal Finance Manager system.
 * Handles user registration, validation, and login logic.
 */
public class UserService implements IService<User> {
    /** The storage for user data. */
    UserStorage userStorage;

    /**
     * Constructs a UserService with the given user storage.
     * @param storage The user storage
     * @throws Exception if storage initialization fails
     */
    public UserService(UserStorage storage) throws Exception {
        this.userStorage = storage;
    }

    /**
     * Checks if the given email is valid and not already used.
     * @param email The email to check
     * @return True if valid, false otherwise
     * @throws IllegalArgumentException if the email already exists
     */
    private boolean isValidEmail(String email) {
        if(this.userStorage.getUserByEmail(email) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        return email.contains("@");
    }

    /**
     * Checks if the given password is valid (at least 8 characters).
     * @param password The password to check
     * @return True if valid, false otherwise
     */
    public boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    /**
     * Validates user data and creates a new User object if valid.
     * @param data The user data dictionary
     * @return The created User object
     * @throws IllegalArgumentException if validation fails
     */
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

    /**
     * Attempts to log in a user with the given email and password.
     * @param email The user's email
     * @param password The user's password
     * @return The User object if login is successful
     * @throws IllegalArgumentException if login fails
     */
    public User login(String email, String password) throws IllegalArgumentException {
        User user = userStorage.getUserByEmail(email);
        if (user != null && user.checkPassword(password)) {
            return user;
        }
        throw new IllegalArgumentException("Invalid email or password");
    }
}
