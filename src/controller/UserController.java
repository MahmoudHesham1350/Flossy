package controller;

import java.util.Dictionary;
import java.util.List;

import models.User;
import service.UserService;
import storage.UserStorage;

/**
 * Specialized controller for user authentication and management in the Flossy Personal Finance Manager system.
 * Handles login, registration, authentication checks, and user retrieval.
 */
public class UserController implements IController<User> {
    /** The currently authenticated user. */
    private User user;
    /** The user validation service. */
    private final UserService service;
    /** The user storage. */
    private final UserStorage storage;

    /**
     * Constructs a UserController with the given service and storage.
     * @param service The user validation service
     * @param storage The user storage
     * @throws Exception if initialization fails
     */
    public UserController(UserService service, UserStorage storage) throws Exception {
        this.storage = storage;
        this.service = service;
        this.user = null;
    }

    /**
     * Attempts to log in a user with the given email and password.
     * @param email The user's email
     * @param password The user's password
     * @throws Exception if login fails
     */
    public void loginUser(String email, String password) throws Exception {
        this.user = service.login(email, password);
        if (this.user == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    /**
     * Creates a new user from the provided data and adds it to storage.
     * @param data The user data dictionary
     */
    @Override
    public void create(Dictionary <String, String> data) {
        User user = service.validate(data);
        storage.add(user);
    }

    /**
     * Gets all users managed by the controller.
     * @return A list of all users
     */
    @Override
    public List<User> getAll() {
        return storage.getAll();
    }

    /**
     * Checks if a user is currently authenticated.
     * @return True if authenticated, false otherwise
     */
    public boolean isAuthenticated(){
        return this.user != null;
    }

    /**
     * Gets the currently authenticated user.
     * @return The authenticated User object, or null if not authenticated
     */
    public User getUser(){
        return this.user;
    }

    /**
     * Removes the specified user from storage.
     * @param user The user to remove
     */
    public void remove(User user) {
        storage.remove(user);
    }

    /**
     * Saves all users to persistent storage.
     * Throws RuntimeException if saving fails.
     */
    public void save() {
        try {
            storage.save();
        } catch (Exception e) {
            throw new RuntimeException("Error saving data", e);
        }
    }
}
